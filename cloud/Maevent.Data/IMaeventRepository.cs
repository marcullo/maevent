using Maevent.Data.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data
{
    public interface IMaeventRepository
    {
        // Basic DB Operations
        void Add<T>(T entity) where T : class;
        void Delete<T>(T entity) where T : class;
        void Update<T>(T entity) where T : class;
        void UpdateMany<T>(IEnumerable<T> entities) where T : class;
        Task<bool> SaveAllAsync();

        // Events
        IEnumerable<Event> GetAllEvents();
        IEnumerable<Event> GetAllEventsOfHost(User host);
        IEnumerable<Event> GetAllEventsByAttendee(int attendeeId);
        Event GetEvent(int id);
        Event GetEventByName(string name);
        int GetEventInviteesNumber(int id);

        // Users
        IEnumerable<User> GetUsersByQuery(string query);
        User GetUser(int id);
        User GetUserByName(string firstName, string lastName);
        IEnumerable<User> GetAllUsers();
        IEnumerable<User> GetUsersByEvent(int eventId);

        // Invitations
        IEnumerable<Invitation> GetAllInvitations();
        IEnumerable<Invitation> GetInvitationsByInviter(int inviteeId);
        IEnumerable<Invitation> GetInvitationsByInvitee(int inviteeId);
        IEnumerable<Invitation> GetInvitationsByInviterAndEvent(int inviterId, int eventId);
        IEnumerable<Invitation> GetInvitationsByInviteeAndEvent(int inviteeId, int eventId);
        Invitation GetInvitation(int id);
        Invitation GetInvitationByIds(int inviterId, int inviteeId, int eventId);
    }
}
