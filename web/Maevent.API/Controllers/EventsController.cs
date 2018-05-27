using AutoMapper;
using Maevent.API.Models;
using Maevent.Data;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Controllers
{
    [Route("api/events")]
    public class EventsController : BaseController
    {
        private IMaeventRepository _repo;

        public EventsController(IMaeventRepository repo)
        {
            _repo = repo;
        }

        [HttpGet("")]
        public IActionResult Get()
        {
            var events = _repo.GetAllEvents();

            return Ok(Mapper.Map<IEnumerable<EventModel>>(events));
        }
    }
}
