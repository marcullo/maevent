using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Models
{
    public class UserModel
    {
        public int Uid { get; set; }
        public string Name { get; set; }
        public string Title { get; set; }
        public string Pose { get; set; }
        public string Headline { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Location { get; set; }
        public string Tags { get; set; }
    }
}
