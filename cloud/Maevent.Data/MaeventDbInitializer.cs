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
                _ctx.AddRange(_sampleEvents);
                _ctx.AddRange(_sampleUsers);
                await _ctx.SaveChangesAsync();
            }
        }

        List<Event> _sampleEvents = new List<Event>
        {
            new Event()
            {
                Uid = 2000000000,
                Name = "Presidential Banquet",
                HostUid = 2000000005,
                Place = "Belveder",
                AddressStreet = "ul. Belwederska 54/56",
                AddressPostCode = "00-001 Warszawa",
                BeginTime = new DateTime(2018, 05, 30, 13, 30, 00),
                EndTime = new DateTime(2018, 05, 30, 18, 30, 00),
                Rsvp = true,
                AttendeesUids = null,
                InviteesNumber = 10
            },
            new Event()
            {
                Uid = 2000000001,
                Name = "Young Leaders Conference",
                HostUid = 2000000006,
                Place = "Palladium",
                AddressStreet = "ul. Zlota 9",
                AddressPostCode = "00-019 Warszawa",
                BeginTime = new DateTime(2018, 05, 28, 08, 30, 00),
                EndTime = new DateTime(2018, 05, 30, 18, 30, 00),
                Rsvp = false,
                AttendeesUids = "2000000006",
                InviteesNumber = 5
            },
            new Event()
            {
                Uid = 2000000002,
                Name = "Google IO",
                HostUid = 2000000007,
                Place = "Shoreline Amphitheatre",
                AddressStreet = "1 Amphitheatre Pkwy",
                AddressPostCode = "Mountain View. Ca 94043",
                BeginTime = new DateTime(2018, 05, 20, 10, 30, 00),
                EndTime = new DateTime(2018, 05, 22, 10, 30, 00),
                Rsvp = false,
                AttendeesUids = "2000000006",
                InviteesNumber = 20
            },
            new Event()
            {
                Uid = 2000000003,
                Name = "Will It Fly?",
                HostUid = 2000000008,
                Place = "Boston City Centre Poland",
                AddressStreet = "Marszalka 23/11",
                AddressPostCode = "33-002 Zlotnik",
                BeginTime = new DateTime(2018, 05, 15, 11, 30, 00),
                EndTime = new DateTime(2018, 05, 15, 14, 30, 00),
                Rsvp = false,
                InviteesNumber = 2000
            },
            new Event()
            {
                Uid = 2000000004,
                Name = "Neu Ride Conference",
                HostUid = 2000000005,
                Place = "Warsaw Expo",
                AddressStreet = "Admirala 32",
                AddressPostCode = "00-241 Warszawa",
                BeginTime = new DateTime(2018, 05, 15, 11, 30, 00),
                EndTime = new DateTime(2018, 05, 15, 14, 30, 00),
                Rsvp = false,
                AttendeesUids = "2000000009;2000000005",
                InviteesNumber = 20
            }
        };

        List<User> _sampleUsers = new List<User>
        {
            new User()
            {
                Uid = 2000000005,
                Name = "Andrew Block",
                Title = "Professor",
                Pose = "Embedded Software Developer",
                Headline = "Nevermind!",
                Phone = "+34123456789",
                Email = "blockandre@block.com",
                Linkedin = "blockandre",
                Location = "Warsaw",
                Tags = "SOLID;TDD"
            },
            new User()
            {
                Uid = 2000000006,
                Name = "Vincent Company",
                Title = "Eng.",
                Pose = "Footballer",
                Headline = "Always positive",
                Phone = "+110988766443",
                Email = "vincent@company.com",
                Linkedin = "companyvincent",
                Location = "France",
                Tags = "EURO"
            },
            new User()
            {
                Uid = 2000000007,
                Name = "Matthiew Mc Molan",
                Title = "B.Sc.",
                Pose = "Physics",
                Headline = "Nucleons rulez",
                Phone = "+47112300555",
                Email = "matt@nolan.com",
                Linkedin = "matthiewmcc",
                Location = "Belgium",
                Tags = null
            },
            new User()
            {
                Uid = 2000000008,
                Name = "Morgan Freeman",
                Title = "B.Sc.",
                Pose = "Actor",
                Headline = "Universe expands...",
                Phone = "+33 441 05 342",
                Email = "morgan@freeman.com",
                Linkedin = null,
                Location = "United States",
                Tags = "Knowledge;Science;Black hole"
            },
        };
    }
}
