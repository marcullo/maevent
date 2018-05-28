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

        [HttpGet("{id}")]
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
            catch
            {
            }

            return BadRequest();
        }
    }
}
