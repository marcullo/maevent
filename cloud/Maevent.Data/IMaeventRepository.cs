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
        IEnumerable<Entities.Event> GetAllEvents();
        IEnumerable<Entities.Event> GetAllEventsOfHost(User host);
        Event GetEvent(int id);
        Event GetEventByName(string name);
        bool EventExists(string name);

        // Users
        User GetUser(int id);
        User GetUserByName(string firstName, string lastName);
        IEnumerable<Entities.User> GetAllUsers();
    }
}
