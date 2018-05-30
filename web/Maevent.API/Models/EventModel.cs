using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Models
{
    public class EventModel
    {
        [Required]
        public int Uid { get; set; }
        [Required]
        [MinLength(5)]
        [MaxLength(50)]
        public string Name { get; set; }
        public string HostUid { get; set; }
        public string Place { get; set; }
        public string AddressStreet { get; set; }
        public string AddressPostCode { get; set; }
        public DateTime BeginTime { get; set; }
        public DateTime EndTime { get; set; }
        public Boolean Rsvp { get; set; }
        public string AttendeesUids { get; set; }
        public int InviteesNumber { get; set; }
    }
}
