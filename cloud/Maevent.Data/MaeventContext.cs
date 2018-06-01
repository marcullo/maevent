using Maevent.Data.Entities;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Maevent.Data
{
    public class MaeventContext : DbContext
    {
        public MaeventContext(DbContextOptions options)
            : base(options)
        {
            //Database.Migrate();
            Database.EnsureCreated();
        }

        public DbSet<Event> Events { get; set; }

        public DbSet<User> Users { get; set; }

        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<Event>()
                .Property(c => c.Name)
                .IsRequired();
            builder.Entity<Event>()
                .Property(c => c.RowVersion)
                .ValueGeneratedOnAddOrUpdate()
                .IsConcurrencyToken();
            builder.Entity<User>()
                .Property(c => c.FirstName)
                .IsRequired();
            builder.Entity<User>()
                .Property(c => c.LastName)
                .IsRequired();
            builder.Entity<User>()
                .Property(c => c.RowVersion)
                .ValueGeneratedOnAddOrUpdate()
                .IsConcurrencyToken();
        }
    }
}
