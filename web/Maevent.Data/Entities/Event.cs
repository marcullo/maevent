using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data.Entities
{
    public class Event
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public int Uid { get; set; }
        public string Name { get; set; }
        public int HostUid { get; set; }
        public string Place { get; set; }
        public string AddressStreet { get; set; }
        public string AddressPostCode { get; set; }
        public DateTime BeginTime { get; set; }
        public DateTime EndTime { get; set; }
        public Boolean Rsvp { get; set; }
        public string AttendeesUids { get; set; }
        public int InviteesNumber { get; set; }

        public byte[] RowVersion { get; set; }
    }
}
