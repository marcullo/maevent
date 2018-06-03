using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Maevent.Data.Entities;
using Microsoft.EntityFrameworkCore;

namespace Maevent.Data
{
    public class MaeventRepository : IMaeventRepository
    {
        private MaeventContext _context;

        public MaeventRepository(MaeventContext context)
        {
            _context = context;
        }

        public void Add<T>(T entity) where T : class
        {
            _context.Add(entity);
        }

        public void Delete<T>(T entity) where T : class
        {
            _context.Remove(entity);
        }
        public void Update<T>(T entity) where T : class
        {
            _context.Update(entity);
        }
        public void UpdateMany<T>(IEnumerable<T> entities) where T : class
        {
            foreach (var entity in entities)
            {
                _context.Update(entity);
            }
        }

        public IEnumerable<Entities.Event> GetAllEvents()
        {
            return _context.Events
                .OrderBy(c => c.BeginTime)
                .ToList();
        }
        public IEnumerable<Entities.Event> GetAllEventsOfHost(User host)
        {
            return _context.Events
                .OrderBy(c => c.BeginTime)
                .Where(c => c.HostFirstName == host.FirstName 
                    && c.HostLastName == host.LastName)
                .ToList();
        }

        public Event GetEvent(int id)
        {
            return _context.Events
                .Where(c => c.Id == id)
                .FirstOrDefault();
        }

        public Event GetEventByName(string name)
        {
            return _context.Events
                .Where(c => c.Name.Equals(name, StringComparison.CurrentCultureIgnoreCase))
                .FirstOrDefault();
        }

        public int GetEventInviteesNumber(int id)
        {
            return _context.Invitations
                .Where(c => c.Id == id)
                .Count();
        }

        public User GetUser(int id)
        {
            return _context.Users
                .Where(u => u.Id == id)
                .FirstOrDefault();
        }

        public User GetUserByName(string firstName, string lastName)
        {
            return _context.Users
                .Where(v => v.LastName == lastName)
                .Where(u => u.FirstName == firstName)
                .FirstOrDefault();
        }

        public IEnumerable<Entities.User> GetAllUsers()
        {
            return _context.Users
                .OrderBy(c => c.LastName)
                .ToList();
        }

        public async Task<bool> SaveAllAsync()
        {
            return (await _context.SaveChangesAsync()) > 0;
        }

        public IEnumerable<Invitation> GetAllInvitations()
        {
            return _context.Invitations
                .OrderBy(c => c.EventId)
                .ToList();
        }        

        public IEnumerable<Invitation> GetInvitationsByInviter(int inviterId)
        {
            return _context.Invitations
                .Where(c => c.InviterId == inviterId)
                .ToList();
        }

        public IEnumerable<Invitation> GetInvitationsByInvitee(int inviteeId)
        {
            return _context.Invitations
                .Where(c => c.InviteeId == inviteeId)
                .ToList();
        }

        public IEnumerable<Invitation> GetInvitationsByInviterAndEvent(int inviterId, int eventId)
        {
            return _context.Invitations
                .Where(c => c.InviterId == inviterId)
                .Where(c => c.EventId == eventId)
                .ToList();
        }

        public IEnumerable<Invitation> GetInvitationsByInviteeAndEvent(int inviteeId, int eventId)
        {
            return _context.Invitations
                .Where(c => c.InviteeId == inviteeId)
                .Where(c => c.EventId == eventId)
                .ToList();
        }

        public Invitation GetInvitation(int id)
        {
            return _context.Invitations
                .Where(c => c.Id == id)
                .FirstOrDefault();
        }

        public Invitation GetInvitationByIds(int inviterId, int inviteeId, int eventId)
        {
            return _context.Invitations
                .Where(c => c.InviterId == inviterId)
                .Where(c => c.InviteeId == inviteeId)
                .Where(c => c.EventId == eventId)
                .FirstOrDefault();
        }
    }
}
