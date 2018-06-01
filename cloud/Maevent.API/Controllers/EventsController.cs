using AutoMapper;
using Maevent.API.Models;
using Maevent.Data;
using Maevent.Data.Entities;
using Microsoft.AspNetCore.JsonPatch;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Controllers
{
    [Route("api/events")]
    public class EventsController : BaseController
    {
        private ILogger<EventsController> _logger;
        private IMaeventRepository _repo;

        public EventsController(IMaeventRepository repo,
                ILogger<EventsController> logger)
        {
            _logger = logger;
            _repo = repo;
        }

        [HttpGet]
        public IActionResult Get()
        {
            var events = _repo.GetAllEvents();

            return Ok(Mapper.Map<IEnumerable<EventModel>>(events));
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAll()
        {
            try
            {
                foreach (var ev in _repo.GetAllEvents())
                {
                    _repo.Delete(ev);

                    if (!await _repo.SaveAllAsync())
                    {
                        return BadRequest("Could not delete a event");
                    }
                }

                return Ok();
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting Events: {ex}");
            }

            return BadRequest();
        }

        [HttpGet("{id}", Name = "EventGet")]
        public IActionResult Get(int id)
        {
            try
            {
                var ev = _repo.GetEvent(id);
                if (ev == null)
                {
                    return NotFound($"Requested event not found");
                }

                var model = Mapper.Map<EventModel>(ev);
                return Ok(model);

            }
            catch (Exception)
            {
            }

            return BadRequest();
        }

        [HttpPost]
        public async Task<IActionResult> Post(
            [FromBody]EventModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest();
                }

                var ev = Mapper.Map<Event>(model);

                var seeked = _repo.GetEventByName(model.Name);
                if (seeked != null)
                {
                    _logger.LogWarning($"Event with name {model.Name} already exists");
                    return BadRequest();
                }

                _logger.LogInformation("Creating new Event");

                _repo.Add(ev);
                if (await _repo.SaveAllAsync())
                {
                    ev.Id = _repo.GetEventByName(ev.Name).Id;
                    _repo.Update(ev);

                    if (await _repo.SaveAllAsync())
                    {
                        var newUri = Url.Link("EventGet", new { id = ev.Id });
                        return Created(newUri, Mapper.Map<EventModel>(ev));
                    }
                    else
                    {
                        _logger.LogWarning("Could not save User to the database");
                    }
                }
                else
                {
                    _logger.LogWarning("Could not save Event to the database");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while saving Event: {ex}");
            }

            return BadRequest();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                var oldEvent = _repo.GetEvent(id);
                if (oldEvent == null)
                {
                    return NotFound("Could not find requested Event");
                }

                _repo.Delete(oldEvent);

                if (await _repo.SaveAllAsync())
                {
                    return Ok();
                }

            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while deleting event: {ex}");
            }

            return BadRequest();
        }
        
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id,
            [FromBody] EventModel model)
        {
            try
            {
                var ev = _repo.GetEvent(id);
                if (ev == null)
                {
                    return NotFound();
                }

                if (ev.Name.Equals(model.Name) && id != ev.Id)
                {
                    return BadRequest("Event with requested name exists.");
                }

                ev.Name = model.Name;
                ev.HostId = model.HostId;
                ev.Place = model.Place;
                ev.AddressStreet = model.AddressStreet;
                ev.AddressPostCode = model.AddressPostCode;
                ev.BeginTime = model.BeginTime;
                ev.EndTime = model.EndTime;
                ev.Rsvp = model.Rsvp;
                ev.AttendeesIds = model.AttendeesIds;
                ev.InviteesNumber = model.InviteesNumber;

                _repo.Update(ev);

                if (await _repo.SaveAllAsync())
                {
                    return Ok(Mapper.Map<EventModel>(ev));
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while updating event: {ex}");
            }

            return BadRequest("Could not update event");
        }
    }
}
