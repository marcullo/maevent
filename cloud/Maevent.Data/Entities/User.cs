using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data.Entities
{
    public class User
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public int Id { get; set; }
        public int Uid { get; set; }
        public string Name { get; set; }
        public string Title { get; set; }
        public string Pose { get; set; }
        public string Headline { get; set; }
        public string Phone { get; set; }
        public string Email { get; set; }
        public string Linkedin { get; set; }
        public string Location { get; set; }
        public string Tags { get; set; }
        
        public byte[] RowVersion { get; set; }
    }
}
