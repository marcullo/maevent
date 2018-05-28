using AutoMapper;
using Maevent.API.Models;
using Maevent.Data;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Controllers
{
    [Route("api/users")]
    public class UsersController : BaseController
    {
        private ILogger<UsersController> _logger;
        private IMaeventRepository _repo;

        public UsersController(IMaeventRepository repo,
                ILogger<UsersController> logger)
        {
            _logger = logger;
            _repo = repo;
        }

        [HttpGet("{name}")]
        public IActionResult GetUser(string name)
        {
            try
            {
                var user = _repo.GetUser(name);
                if (user == null)
                {
                    return NotFound($"Requested user not found");
                }

                var model = Mapper.Map<UserModel>(user);
                return Ok(model);
            }
            catch (Exception)
            {
            }

            return BadRequest();
        }
    }
}
