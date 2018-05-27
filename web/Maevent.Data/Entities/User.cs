using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data.Entities
{
    public class User
    {
        public int Id { get; set; }
        public string Name { get; set; }

        public byte[] RowVersion { get; set; }
    }
}
