using AutoMapper;
using Maevent.API.Models;
using Maevent.Data;
using Maevent.Data.Entities;
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

        private UserModel GetHostModel(Event ev)
        {
            if (ev == null)
            {
                return null;
            }
            var firstName = ev.HostFirstName;
            var lastName = ev.HostLastName;
            var host = _repo.GetUserByName(firstName, lastName);
            return Mapper.Map<UserModel>(host);
        }

        private User GetEventHost(EventModel model)
        {
            if (model == null)
            {
                return null;
            }
            var hostFirstName = model.Host.FirstName;
            var hostLastName = model.Host.LastName;
            var seekedHost = _repo.GetUserByName(hostFirstName, hostLastName);
            return seekedHost;
        }

        public EventsController(IMaeventRepository repo,
                ILogger<EventsController> logger)
        {
            _logger = logger;
            _repo = repo;
        }

        [HttpGet]
        public IActionResult Get()
        {
            try
            {
                var events = _repo.GetAllEvents();
                var eventsModel = Mapper.Map<IEnumerable<EventModel>>(events);

                for (int i = 0; i < events.Count(); i++)
                {
                    var ev = events.ElementAt(i);
                    var host = GetHostModel(ev);
                    eventsModel.ElementAt(i).Host = host;
                }

                return Ok(eventsModel);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while fetching events: {ex}");
            }

            return BadRequest("Could not fetch events");
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
                        return BadRequest("Could not delete events");
                    }
                }

                return NoContent();
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting events: {ex}");
            }

            return BadRequest("Could not delete events");
        }

        [HttpGet("{id}", Name = "EventGet")]
        public IActionResult Get(int id)
        {
            try
            {
                var ev = _repo.GetEvent(id);
                if (ev == null)
                {
                    return NotFound();
                }

                var model = Mapper.Map<EventModel>(ev);
                model.Host = GetHostModel(ev);
                
                return Ok(model);

            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while fetching event: {ex}");
            }

            return BadRequest("Could not fetch event");
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody]EventModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var seekedHost = GetEventHost(model);
                if (seekedHost == null)
                {
                    _logger.LogWarning($"User which which wants to be a host is not registered in the database");
                    return Unauthorized();
                }

                var seeked = _repo.GetEventByName(model.Name);
                if (seeked != null)
                {
                    _logger.LogWarning($"Event with name {model.Name} already exists");
                    return BadRequest("Could not create event");
                }

                _logger.LogInformation($"Creating new event {model.Name}");

                var ev = Mapper.Map<Event>(model);

                ev.HostFirstName = seekedHost.FirstName;
                ev.HostLastName = seekedHost.LastName;

                _repo.Add(ev);
                if (await _repo.SaveAllAsync())
                {
                    ev.Id = _repo.GetEventByName(ev.Name).Id;
                    _repo.Update(ev);

                    if (await _repo.SaveAllAsync())
                    {
                        var newUri = Url.Link("EventGet", new { id = ev.Id });

                        EventModel mapped = Mapper.Map<EventModel>(ev);
                        mapped.Host = Mapper.Map<UserModel>(seekedHost);
                        return Created(newUri, mapped);
                    }
                    else
                    {
                        _logger.LogWarning("Event created but could not be updated with its host");
                    }
                }
                else
                {
                    _logger.LogWarning("Could not save event to the database");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while saving event: {ex}");
            }

            return BadRequest("Could not create event");
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                var oldEvent = _repo.GetEvent(id);
                if (oldEvent == null)
                {
                    return NotFound();
                }

                _repo.Delete(oldEvent);

                if (await _repo.SaveAllAsync())
                {
                    return NoContent();
                }

            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while deleting event: {ex}");
            }

            return BadRequest("Could not delete event");
        }
        
        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id, [FromBody] EventModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var ev = _repo.GetEvent(id);
                if (ev == null)
                {
                    return NotFound();
                }

                var seekedHost = GetEventHost(model);
                if (seekedHost == null)
                {
                    _logger.LogWarning($"User which which wants to be a host is not registered in the database");
                    return Unauthorized();
                }

                if (!ev.Name.Equals(model.Name))
                {
                    var seeked = _repo.GetEventByName(model.Name);
                    if (seeked != null)
                    {
                        return BadRequest("Event with requested name exists.");
                    }
                }

                ev.Name = model.Name;
                ev.HostFirstName = model.Host.FirstName;
                ev.HostLastName = model.Host.LastName;
                ev.Place = model.Place;
                ev.AddressStreet = model.AddressStreet;
                ev.AddressPostCode = model.AddressPostCode;
                ev.BeginTime = model.BeginTime;
                ev.EndTime = model.EndTime;
                ev.Rsvp = model.Rsvp;
                ev.AttendeesIds = model.AttendeesIds;

                _repo.Update(ev);

                if (await _repo.SaveAllAsync())
                {
                    EventModel mapped = Mapper.Map<EventModel>(ev);
                    mapped.Host = Mapper.Map <UserModel>(seekedHost);
                    return Ok(mapped);
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
