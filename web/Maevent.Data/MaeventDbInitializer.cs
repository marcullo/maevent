using Maevent.Data.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data
{
    public class MaeventDbInitializer
    {
        private MaeventContext _ctx;

        public MaeventDbInitializer(MaeventContext ctx)
        {
            _ctx = ctx;
        }

        public async Task Seed()
        {
            if (!_ctx.Events.Any())
            {
                _ctx.AddRange(_sample);
                await _ctx.SaveChangesAsync();
            }
        }

        List<Event> _sample = new List<Event>
        {
            new Event()
            {
                Name = "Presidential Banquet",
                Place = "Belveder",
                AddressStreet = "ul. Belwederska 54/56",
                AddressPostCode = "00-001 Warszawa",
                BeginTime = DateTime.Now,
                EndTime = DateTime.Now.AddHours(2),
                Rsvp = true,
                hostId = 100
            },
            new Event()
            {
                Name = "Young Leaders Conference",
                Place = "Palladium",
                AddressStreet = "ul. Zlota 9",
                AddressPostCode = "00-019 Warszawa",
                BeginTime = DateTime.Now.AddDays(1),
                EndTime = DateTime.Now.AddDays(1).AddMinutes(50),
                Rsvp = false,
                hostId = 101
            },
            new Event()
            {
                Name = "Google I/O",
                Place = "Shoreline Amphitheatre",
                AddressStreet = "1 Amphitheatre Pkwy",
                AddressPostCode = "Mountain View. Ca 94043",
                BeginTime = DateTime.Now.AddDays(10),
                EndTime = DateTime.Now.AddDays(10).AddDays(2),
                Rsvp = false,
                hostId = 102
            }
        };
    }
}
