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

        public IEnumerable<Entities.Event> GetAllEvents()
        {
            return _context.Events
                .OrderBy(c => c.BeginTime)
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

        public User GetUser(string name)
        {
            return _context.Users
                .Where(u => u.Name == name)
                .Cast<User>()
                .FirstOrDefault();
        }

        public async Task<bool> SaveAllAsync()
        {
            return (await _context.SaveChangesAsync()) > 0;
        }
    }
}
