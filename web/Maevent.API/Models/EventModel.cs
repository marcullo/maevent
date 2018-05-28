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
        [MinLength(5)]
        [MaxLength(30)]
        public string Name { get; set; }
        [Required]
        public string Place { get; set; }
        public string AddressStreet { get; set; }
        public string AddressPostCode { get; set; }
        [Required]
        public DateTime BeginTime { get; set; }
        [Required]
        public DateTime EndTime { get; set; }
        public Boolean Rsvp { get; set; }

        [Required]
        public int hostId { get; set; }
    }
}
