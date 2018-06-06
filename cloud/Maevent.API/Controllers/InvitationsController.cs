using AutoMapper;
using Maevent.API.Constraints;
using Maevent.API.Models;
using Maevent.Data;
using Maevent.Data.Entities;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Maevent.API.Controllers
{
    [Route("api/invitations")]
    public class InvitationsController : Controller
    {
        private ILogger<InvitationsController> _logger;
        private IMaeventRepository _repo;

        private UserModel GetUserModel(int id)
        {
            if (id == 0)
            {
                return null;
            }
            var usr = _repo.GetUser(id);
            return Mapper.Map<UserModel>(usr);
        }

        private EventModel GetEventModel(int id)
        {
            if (id == 0)
            {
                return null;
            }
            var ev = _repo.GetEvent(id);
            var firstName = ev.HostFirstName;
            var lastName = ev.HostLastName;
            var host = _repo.GetUserByName(firstName, lastName);
            var hostModel = Mapper.Map<UserModel>(host);

            var eventModel = Mapper.Map<EventModel>(ev);
            eventModel.Host = hostModel;
            return eventModel;
        }

        private User GetInvitee(InvitationModel model)
        {
            if (model == null)
            {
                return null;
            }
            var id = model.Id;
            var seekedInvitee = _repo.GetUser(id);
            return seekedInvitee;
        }

        private List<InvitationModel> GetInvitationModels(IEnumerable<Invitation> invitations)
        {
            if (invitations == null)
            {
                return null;
            }

            var invitationModels = new List<InvitationModel>();

            for (int i = 0; i < invitations.Count(); i++)
            {
                var inv = invitations.ElementAt(i);

                var invitationId = inv.Id;
                var invitee = GetUserModel(inv.InviteeId);
                var inviter = GetUserModel(inv.InviterId);
                var eventModel = GetEventModel(inv.EventId);
                var message = inv.Message;

                InvitationModel model = new InvitationModel
                {
                    Id = invitationId,
                    Invitee = invitee,
                    Inviter = inviter,
                    Event = eventModel,
                    Message = message
                };

                invitationModels.Add(model);
            }

            return invitationModels;
        }

        public InvitationsController(IMaeventRepository repo,
                ILogger<InvitationsController> logger)
        {
            _logger = logger;
            _repo = repo;
        }

        [HttpGet]
        public IActionResult Get()
        {
            try
            {
                var invitations = _repo.GetAllInvitations();
                var invitationModels = new List<InvitationModel>();

                for (int i = 0; i < invitations.Count(); i++)
                {
                    var inv = invitations.ElementAt(i);

                    var id = inv.Id;
                    var invitee = GetUserModel(inv.InviteeId);
                    var inviter = GetUserModel(inv.InviterId);
                    var eventModel = GetEventModel(inv.EventId);
                    var message = inv.Message;

                    InvitationModel model = new InvitationModel
                    {
                        Id = id,
                        Invitee = invitee,
                        Inviter = inviter,
                        Event = eventModel,
                        Message = message
                    };

                    invitationModels.Add(model);
                }

                return Ok(invitationModels);
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while fetching users: {ex}");
            }

            return BadRequest("Could not fetch users");
        }

        [HttpDelete]
        public async Task<IActionResult> DeleteAll()
        {
            try
            {
                foreach (var inv in _repo.GetAllInvitations())
                {
                    _repo.Delete(inv);

                    if (!await _repo.SaveAllAsync())
                    {
                        return BadRequest("Could not delete invitations");
                    }
                }

                return NoContent();
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting invitations: {ex}");
            }

            return BadRequest("Could not delete invitations");
        }

        [HttpGet("{id}", Name = "InvitationGet")]
        public IActionResult Get(int id)
        {
            try
            {
                var inv = _repo.GetInvitation(id);
                if (inv == null)
                {
                    return NotFound();
                }

                var model = Mapper.Map<InvitationModel>(inv);
                model.Inviter = GetUserModel(inv.InviterId);
                model.Invitee = GetUserModel(inv.InviteeId);
                model.Event = GetEventModel(inv.EventId);

                return Ok(model);

            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while fetching event: {ex}");
            }

            return BadRequest("Could not fetch event");
        }

        [HttpGet("inviter")]
        public IActionResult GetByInviter([RequiredFromQuery]string id, [RequiredFromQuery]string ev)
        {
            try
            {
                if (!int.TryParse(id, out int inviterId))
                {
                    return BadRequest("Invalid inviter id");
                }

                if (ev == null)
                {
                    return BadRequest("Invalid event id");
                }

                if (ev.Equals("all"))
                {
                    var invitations = _repo.GetInvitationsByInviter(inviterId);
                    var invitationModels = GetInvitationModels(invitations);
                    return Ok(invitationModels);
                }
                else if (int.TryParse(ev, out int eventId))
                {
                    var invitations = _repo.GetInvitationsByInviterAndEvent(inviterId, eventId);
                    var invitationModels = GetInvitationModels(invitations);
                    return Ok(invitationModels);
                }

                return BadRequest("Invalid event id");
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting invitations: {ex}");
            }

            return BadRequest("Could not fetch invitations");
        }

        [HttpGet("invitee")]
        public IActionResult GetByInvitee([RequiredFromQuery]string id, [RequiredFromQuery]string ev)
        {
            try
            {
                if (!int.TryParse(id, out int inviteeId))
                {
                    return BadRequest("Invalid invitee id");
                }

                if (ev == null)
                {
                    return BadRequest("Invalid event id");
                }

                if (ev.Equals("all"))
                {
                    var invitations = _repo.GetInvitationsByInvitee(inviteeId);
                    var invitationModels = GetInvitationModels(invitations);
                    return Ok(invitationModels);
                }
                else if (int.TryParse(ev, out int eventId))
                {
                    var invitations = _repo.GetInvitationsByInviteeAndEvent(inviteeId, eventId);
                    var invitationModels = GetInvitationModels(invitations);
                    return Ok(invitationModels);
                }

                return BadRequest("Invalid event id");
            }
            catch (Exception ex)
            {
                _logger.LogError($"Threw exception while deleting invitations: {ex}");
            }

            return BadRequest("Could not fetch invitations");
        }

        [HttpPost]
        public async Task<IActionResult> Post([FromBody]InvitationModel model)
        {
            try
            {
                if (!ModelState.IsValid)
                {
                    return BadRequest(ModelState);
                }

                var seekedInviter = _repo.GetUser(model.Inviter.Id);
                if (seekedInviter == null)
                {
                    _logger.LogWarning($"User which which wants to be inviter is not registered in the database");
                    return Unauthorized();
                }

                var seekedInvitee = _repo.GetUser(model.Invitee.Id);
                if (seekedInvitee == null)
                {
                    return BadRequest("Invitee does not exists");
                }

                var seekedEvent = _repo.GetEvent(model.Event.Id);
                if (seekedEvent == null)
                {
                    return BadRequest("Event does not exists");
                }

                StringBuilder sb = new StringBuilder();

                sb.Append(model.Inviter.FirstName).Append(" ").Append(model.Inviter.LastName);
                string inviterName = sb.ToString();
                sb.Clear();

                sb.Append(model.Invitee.FirstName).Append(" ").Append(model.Invitee.LastName);
                string inviteeName = sb.ToString();
                sb.Clear();

                string eventName = model.Event.Name;

                var seeked = _repo.GetInvitationByIds(seekedInviter.Id, seekedInvitee.Id, seekedEvent.Id);
                if (seeked != null)
                {
                    _logger.LogWarning($"{inviteeName} has already been invited by {inviterName} for {eventName} ");
                    return BadRequest("Could not create invitation");
                }

                _logger.LogInformation($"Creating new invitation {inviterName} -> {inviteeName}. Event: {eventName}");

                var inv = Mapper.Map<Invitation>(model);
                inv.Id = 0;
                inv.InviterId = seekedInviter.Id;
                inv.InviteeId = seekedInvitee.Id;
                inv.EventId = seekedEvent.Id;

                _repo.Add(inv);
                if (await _repo.SaveAllAsync())
                {
                    var newUri = Url.Link("InvitationGet", new { id = inv.Id });

                    InvitationModel mapped = Mapper.Map<InvitationModel>(inv);
                    mapped.Inviter = Mapper.Map<UserModel>(seekedInviter);
                    mapped.Invitee = Mapper.Map<UserModel>(seekedInvitee);
                    mapped.Event = GetEventModel(seekedEvent.Id);
                    return Created(newUri, mapped);
                }
            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while saving invitation: {ex}");
            }

            return BadRequest("Could not create invitation");
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> Delete(int id)
        {
            try
            {
                var item = _repo.GetInvitation(id);
                if (item == null)
                {
                    return NotFound();
                }

                _repo.Delete(item);

                if (await _repo.SaveAllAsync())
                {
                    return NoContent();
                }

            }
            catch (Exception ex)
            {
                _logger.LogError($"Exception thrown while deleting invitation: {ex}");
            }

            return BadRequest("Could not delete invitation");
        }
    }
}