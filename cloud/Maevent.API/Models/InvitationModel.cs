using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.API.Models
{
    public class InvitationModel
    {
        [Required]
        public int Id { get; set; }
        [Required]
        public UserModel Inviter { get; set; }
        [Required]
        public UserModel Invitee { get; set; }
        [Required]
        public EventModel Event { get; set; }
        public string Message { get; set; }
    }
}
