using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data.Entities
{
    public class Invitation
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public int InviterId { get; set; }
        public int InviteeId { get; set; }
        public int EventId { get; set; }
        public string Message { get; set; }

        public byte[] RowVersion { get; set; }
    }
}
