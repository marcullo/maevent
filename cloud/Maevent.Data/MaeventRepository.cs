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

        public bool EventExists(string name)
        {
            return GetEventByName(name) != null;
        }

        public User GetUser(int id)
        {
            return _context.Users
                .Where(u => u.Id == id)
                .Cast<User>()
                .FirstOrDefault();
        }

        public User GetUserByName(string firstName, string lastName)
        {
            return _context.Users
                .Where(v => v.LastName == lastName)
                .Where(u => u.FirstName == firstName)
                .Cast<User>()
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

    }
}
