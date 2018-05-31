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

        [HttpGet]
        public IActionResult Get()
        {
            var users = _repo.GetAllUsers();

            return Ok(Mapper.Map<IEnumerable<UserModel>>(users));
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAll()
        {
            try
            {
                foreach (var usr in _repo.GetAllUsers())
                {
                    _repo.Delete(usr);

                    if (!await _repo.SaveAllAsync())
                    {
                        return BadRequest("Could not delete a user");
                    }
                }

                return Ok();
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting Users: {ex}");
            }

            return BadRequest();
        }

        [HttpGet("{uid}", Name = "UserGet")]
        public IActionResult GetUser(int uid)
        {
            try
            {
                var user = _repo.GetUser(uid);
                if (user == null)
                {
                    return NotFound($"Requested user not found");
                }

                var model = Mapper.Map<UserModel>(user);
                return Ok(model);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting Users: {ex}");
            }

            return BadRequest();
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody]UserModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest();
                }

                var user = Mapper.Map<User>(model);

                var seeked = _repo.GetUserByName(model.Name);
                if (seeked != null)
                {
                    _logger.LogWarning($"User {model.Name} already exists");
                    return BadRequest();
                }

                _logger.LogInformation("Creating new User");

                _repo.Add(user);
                if (await _repo.SaveAllAsync())
                {
                    user.Uid = _repo.GetUserByName(user.Name).Id;
                    _repo.Update(user);

                    if (await _repo.SaveAllAsync())
                    {
                        var newUri = Url.Link("UserGet", new { uid = user.Uid });
                        return Created(newUri, Mapper.Map<UserModel>(user));
                    }
                    else
                    {
                        _logger.LogWarning("Could not save User to the database");
                    }
                }
                else
                {
                    _logger.LogWarning("Could not save User to the database");
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while saving User: {ex}");
            }

            return BadRequest();
        }

        [HttpDelete("{uid}")]
        public async Task<IActionResult> Delete(int uid)
        {
            try
            {
                var user = _repo.GetUser(uid);
                if (user == null)
                {
                    return NotFound("Could not find requested User");
                }

                _repo.Delete(user);

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

        [HttpPut("{uid}")]
        public async Task<IActionResult> Put(int uid,
            [FromBody] UserModel model)
        {
            try
            {
                var usr = _repo.GetUser(uid);
                if (usr == null)
                {
                    return NotFound();
                }

                if (usr.Name.Equals(model.Name) && uid != usr.Uid)
                {
                    return BadRequest("User with requested name exists.");
                }

                var id = usr.Id;
                usr = Mapper.Map<User>(model);
                usr.Uid = id;

                _repo.Update(usr);

                if (await _repo.SaveAllAsync())
                {
                    return Ok(Mapper.Map<UserModel>(usr));
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while updating user: {ex}");
            }

            return BadRequest("Could not update user");
        }
    }
}
