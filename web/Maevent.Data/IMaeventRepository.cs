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
        Task<bool> SaveAllAsync();

        // Events
        IEnumerable<Entities.Event> GetAllEvents();
        Event GetEvent(int id);

        // MaeventUser
        User GetUser(string userName);
    }
}
