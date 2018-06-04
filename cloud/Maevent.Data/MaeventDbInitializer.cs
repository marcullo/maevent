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
                _ctx.AddRange(_sampleInvitations);
                await _ctx.SaveChangesAsync();
            }
        }

        List<Event> _sampleEvents = new List<Event>
        {
            new Event() // id = 1
            {
                Name = "Presidential Banquet",
                HostFirstName = "Andrew",
                HostLastName = "Block",
                Place = "Pałac Belwederski",
                AddressStreet = "ul. Belwederska 54/56",
                AddressPostCode = "00-001 Warszawa",
                BeginTime = new DateTime(2018, 06, 07, 18, 00, 00),
                EndTime = new DateTime(2018, 06, 07, 23, 00, 00),
                Rsvp = true,
                AttendeesIds = ";13;4;5;6;7;8;9;10;11;14;15;",
            },
            new Event() // id = 2
            {
                Name = "Science club",
                HostFirstName = "Albert",
                HostLastName = "Einstein",
                Place = "ICE",
                AddressStreet = "Marii Konopnickiej 17",
                AddressPostCode = "30-302 Kraków",
                BeginTime = new DateTime(2018, 06, 06, 17, 00, 00),
                EndTime = new DateTime(2018, 06, 06, 22, 30, 00),
                Rsvp = false,
                AttendeesIds = ";12;2;3;9;",
            },
            new Event() // id = 3
            {
                Name = "Football science club",
                HostFirstName = "Albert",
                HostLastName = "Einstein",
                Place = "Stadion Narodowy",
                AddressStreet = "aleja Poniatowskiego 1",
                AddressPostCode = "03-901 Warszawa",
                BeginTime = new DateTime(2018, 05, 20, 10, 30, 00),
                EndTime = new DateTime(2018, 05, 22, 10, 30, 00),
                Rsvp = false,
                AttendeesIds = ";7;9;10;11;",
            },
            new Event() // id = 4
            {
                Name = "Relax",
                HostFirstName = "Andrew",
                HostLastName = "Block",
                Place = "Łazienki Królewskie",
                AddressStreet = "ul. Agrykola 1",
                AddressPostCode = "00-460 Warszawa",
                BeginTime = new DateTime(2018, 06, 06, 18, 00, 00),
                EndTime = new DateTime(2018, 06, 06, 20, 00, 00),
                Rsvp = false,
                AttendeesIds = ";9;13;8;9;",
            },
            new Event() // id = 5
            {
                Name = "Blog Forum Gdańsk",
                HostFirstName = "Andrew",
                HostLastName = "Block",
                Place = "ECS",
                AddressStreet = "Główna 80",
                AddressPostCode = "80-863 Gdańsk",
                BeginTime = new DateTime(2018, 09, 29, 08, 00, 00),
                EndTime = new DateTime(2018, 09, 30, 22, 00, 00),
                Rsvp = false,
                AttendeesIds = ";5;13;6;7;8;12;14;15",
            },
            new Event() // id = 6
            {
                Name = "Android Hackathlon",
                HostFirstName = "Andrew",
                HostLastName = "Block",
                Place = "WUT",
                AddressStreet = "pl. Politechniki 1",
                AddressPostCode = "00-661 Warszawa",
                BeginTime = new DateTime(2018, 06, 06, 23, 00, 00),
                EndTime = new DateTime(2018, 06, 07, 23, 00, 00),
                Rsvp = false,
                AttendeesIds = ";8;2;3;4;5;",
            },
        };

        List<User> _sampleUsers = new List<User>
        {
            new User() // id = 1
            {
                FirstName = "Andrew",
                LastName = "Block",
                Title = "Ph.d.",
                Pose = "Software Oracle",
                Headline = "I am the King of the World!",
                Phone = "+34123456789",
                Email = "blockandre@block.com",
                Linkedin = "blockandre",
                Location = "Universe",
                Tags = "SOLID;TDD"
            },
            new User() // id = 2
            {
                FirstName = "Albert",
                LastName = "Einstein",
                Title = "Ph.d.",
                Pose = "Physicist",
                Headline = "Imagination is more important than knowledge.",
                Phone = "+001122334455",
                Email = "albert@einstein.com",
                Linkedin = "bigalbert",
                Location = "Princeton",
                Tags = "Nobel;Relativity;Gravitational wave;EPR Paradox"
            },
            new User() // id = 3
            {
                FirstName = "Isaac",
                LastName = "Newton",
                Title = "Ph.d.",
                Pose = "Physicist, Mathematician, Astronomer",
                Headline = "Genius is patience.",
                Phone = "+001122334456",
                Email = "isaac@newton.com",
                Linkedin = "newton",
                Location = "Kensigton",
                Tags = "Laws of motion;FRS;Optics"
            },
            new User() // id = 4
            {
                FirstName = "Stephen",
                LastName = "Hawking",
                Title = "Ph.d.",
                Pose = "Physicist, Cosmologist",
                Headline = "Intelligence is the ability to adapt to change.",
                Phone = "+001122334457",
                Email = "step@freeman.com",
                Linkedin = null,
                Location = "Cambridge",
                Tags = "Time;Space;Black holes;Quantum gravity"
            },
            new User() // id = 5
            {
                FirstName = "Galileo",
                LastName = "Galilei",
                Title = "Ph.d.",
                Pose = "Astronomer, Wise man, Mathematician, Physicist",
                Headline = "Mathematics is the key and door to the sciences.",
                Phone = "+001122334459",
                Email = "galileusz@world.com",
                Linkedin = null,
                Location = "Pisa",
                Tags = "Astro;Philo;Inertia; Leaning Tower"
            },
            new User() // id = 6
            {
                FirstName = "Blaise",
                LastName = "Pascal",
                Title = "Ph.d.",
                Pose = "Mathematician, Physicist, Philosopher",
                Headline = "I have made this letter longer than usual beacause I lack the time to make it shorter.",
                Phone = "+001122334460",
                Email = "blaise@pascal.com",
                Linkedin = null,
                Location = "Paris",
                Tags = "Prawo Pascala;Triangle;Wager"
            },
            new User() // id = 7
            {
                FirstName = "George",
                LastName = "Washington",
                Title = "Gen.",
                Pose = "First President of the U.S.A.",
                Headline = "The harder the conflict, the greater the triumph.",
                Phone = "+001122334463",
                Email = "george@washington.com",
                Linkedin = "washington",
                Location = "Mount Vernon",
                Tags = "Nonpartisan;Independence;U.S. Army"
            },
            new User() // id = 8
            {
                FirstName = "Paul",
                LastName = "Dirac",
                Title = "Ph.d.",
                Pose = "Physicist",
                Headline = "If there is a God, he's a great mathematician.",
                Phone = "+001122334462",
                Email = "paul@dirac.com",
                Linkedin = null,
                Location = "Bristol",
                Tags = "Dirac's delta;Cambridge;Ox"
            },
            new User() // id = 9
            {
                FirstName = "Józef",
                LastName = "Piłsudski",
                Title = "Head",
                Pose = "Soldier, Politician",
                Headline = "Racja jest jak dupa - każdy ma swoją.",
                Phone = "+001122334461",
                Email = "jozef@pilsudski.com",
                Linkedin = "pilsudski",
                Location = "Warszawa",
                Tags = "Legiony;Headquarters;Chief of State;Komendant"
            },
            new User() // id = 10
            {
                FirstName = "Nikola",
                LastName = "Tesla",
                Title = "Eng.",
                Pose = "Physicist, Engineer, Inventor",
                Headline = "If your hate could be turned into electricity, it would likgt up the whole world.",
                Phone = "+001122334464",
                Email = "nicola@tesla.com",
                Linkedin = "tesla",
                Location = "Smijana",
                Tags = "Dynamo;Radio;Tesla coil;Solar battery"
            },
            new User() // id = 11
            {
                FirstName = "Morgan",
                LastName = "Freeman",
                Title = "Act.",
                Pose = "Actor, Director, Narrator",
                Headline = "Challenge yourself - it's the only path which leads to growth.",
                Phone = "+001122334465",
                Email = "morgan@freeman.com",
                Linkedin = null,
                Location = "Memphis",
                Tags = "Dynamo;Radio;Tesla coil;Solar battery"
            },
            new User() // id = 12
            {
                FirstName = "Kevin",
                LastName = "Spacey",
                Title = "Act.",
                Pose = "Actor, Director, Producer",
                Headline = "If you don't like how the table is set, turn over the table.",
                Phone = "+001122334466",
                Email = "kevin@spacey.com",
                Linkedin = null,
                Location = "South Orange",
                Tags = "House of cards;Oscar"
            },
            new User() // id = 13
            {
                FirstName = "Jennifer",
                LastName = "Lawrence",
                Title = "Act.",
                Pose = "Actress",
                Headline = "Things can happen to you, but they don't have to happen to your soul.",
                Phone = "+001122334467",
                Email = "jenny@lawrence.com",
                Linkedin = null,
                Location = "Louisville",
                Tags = "Oscar;Golden Globe"
            },
            new User() // id = 14
            {
                FirstName = "Johnny",
                LastName = "Depp",
                Title = "Act.",
                Pose = "Actor, Director, Narrator",
                Headline = "The thing to do is enjoy the ride while you are on it.",
                Phone = "+001122334468",
                Email = "johnny@depp.com",
                Linkedin = null,
                Location = "Owensboro",
                Tags = "Jack Sparrow;Golden Globe;Oscar;"
            },
            new User() // id = 15
            {
                FirstName = "Christoph",
                LastName = "Waltz",
                Title = "Act.",
                Pose = "Actor",
                Headline = "I have always been so interested in film as a medium.",
                Phone = "+001122334469",
                Email = "christoph@waltz.com",
                Linkedin = null,
                Location = "Vienna",
                Tags = "Django;Inglorious Basterds"
            },
        };

        List<Invitation> _sampleInvitations = new List<Invitation>
        {
            // eventId = 1
            new Invitation()
            {
                InviteeId = 12,
                InviterId = 13,
                EventId = 1,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 2,
                InviterId = 13,
                EventId = 1,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 3,
                InviterId = 13,
                EventId = 1,
                Message = "You are welcome!"
            },
            // eventId = 2
            new Invitation()
            {
                InviteeId = 13,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 4,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 5,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 6,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 7,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 8,
                InviterId = 12,
                EventId = 2,
                Message = "Guys, come in!"
            },
            // eventId = 3
            new Invitation()
            {
                InviteeId = 3,
                InviterId = 7,
                EventId = 3,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 4,
                InviterId = 7,
                EventId = 3,
                Message = "Guys, come in!"
            },
            new Invitation()
            {
                InviteeId = 8,
                InviterId = 7,
                EventId = 3,
                Message = "Guys, come in!"
            },
            // eventId = 4
            new Invitation()
            {
                InviteeId = 10,
                InviterId = 9,
                EventId = 4,
                Message = "You are welcome!"
            },
            // eventId = 5
            new Invitation()
            {
                InviteeId = 2,
                InviterId = 13,
                EventId = 5,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 3,
                InviterId = 13,
                EventId = 5,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 4,
                InviterId = 13,
                EventId = 5,
                Message = "You are welcome!"
            },
            // eventId = 6
            new Invitation()
            {
                InviteeId = 10,
                InviterId = 8,
                EventId = 6,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 11,
                InviterId = 8,
                EventId = 6,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 12,
                InviterId = 8,
                EventId = 6,
                Message = "You are welcome!"
            },
             new Invitation()
            {
                InviteeId = 13,
                InviterId = 8,
                EventId = 6,
                Message = "You are welcome!"
            },
            new Invitation()
            {
                InviteeId = 14,
                InviterId = 8,
                EventId = 6,
                Message = "You are welcome!"
            },
        };
    }
}
