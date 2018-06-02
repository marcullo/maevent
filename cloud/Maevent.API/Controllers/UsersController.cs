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

        [HttpGet("{id}", Name = "UserGet")]
        public IActionResult GetUser(int id)
        {
            try
            {
                var user = _repo.GetUser(id);
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

                model.Id = 0;
                var user = Mapper.Map<User>(model);

                var seeked = _repo.GetUserByName(model.FirstName, model.LastName);
                if (seeked != null)
                {
                    _logger.LogWarning($"User {model.FirstName} {model.LastName} already exists");
                    return BadRequest();
                }

                _logger.LogInformation("Creating new User");

                _repo.Add(user);
                if (await _repo.SaveAllAsync())
                {
                    user.Id = _repo.GetUserByName(user.FirstName, user.LastName).Id;
                    _repo.Update(user);

                    if (await _repo.SaveAllAsync())
                    {
                        var newUri = Url.Link("UserGet", new { id = user.Id });
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

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                var user = _repo.GetUser(id);
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

        [HttpPut("{id}")]
        public async Task<IActionResult> Put(int id,
            [FromBody] UserModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }
                if (model.Id != id)
                {
                    return BadRequest("Changing id is impossible");
                }

                var usr = _repo.GetUser(id);
                if (usr == null)
                {
                    return NotFound();
                }

                if (usr.FirstName.Equals(model.FirstName)
                    && usr.LastName.Equals(model.LastName)
                    && id != usr.Id)
                {
                    return BadRequest("User with requested name exists.");
                }

                usr.FirstName = model.FirstName;
                usr.LastName = model.LastName;
                usr.Title = model.Title;
                usr.Pose = model.Pose;
                usr.Headline = model.Headline;
                usr.Phone = model.Phone;
                usr.Email = model.Email;
                usr.Linkedin = model.Linkedin;
                usr.Location = model.Location;
                usr.Tags = model.Tags;

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
