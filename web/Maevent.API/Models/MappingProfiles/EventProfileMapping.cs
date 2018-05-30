using AutoMapper;
using Maevent.Data.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Models.MappingProfiles
{
    public class EventProfileMapping : Profile
    {
        public EventProfileMapping()
        {
            CreateMap<EventModel, Event>()
                .ReverseMap();
        }
    }
}
