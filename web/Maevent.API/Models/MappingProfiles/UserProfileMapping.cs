using AutoMapper;
using Maevent.Data.Entities;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Models.MappingProfiles
{
    public class UserProfileMapping : Profile
    {
        public UserProfileMapping()
        {
            CreateMap<UserModel, User>()
                .ReverseMap();
        }
    }
}
