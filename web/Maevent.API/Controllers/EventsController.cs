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

        [HttpGet("")]
        public IActionResult Get()
        {
            var events = _repo.GetAllEvents();

            return Ok(Mapper.Map<IEnumerable<EventModel>>(events));
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
        public async Task<IActionResult> Post([FromBody]EventModel model)
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
                    var newUri = Url.Link("EventGet", new { id = ev.Id});

                    return Created(newUri, Mapper.Map<EventModel>(ev));
                }
                else
                {
                    _logger.LogWarning("Could not save Event to the database");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while saving Event: {ex}");
            }

            return BadRequest();
        }

        [HttpDelete("{name}")]
        public async Task<IActionResult> Delete(string name)
        {
            try
            {
                var oldEvent = _repo.GetEventByName(name);
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
            catch (Exception)
            {
            }

            return BadRequest();
        }
    }
}
