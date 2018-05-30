using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Maevent.API.Models.MappingProfiles;
using Maevent.Data;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Logging;
using Microsoft.Extensions.Options;
using Newtonsoft.Json;

namespace Maevent.API
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {            
            services.AddDbContext<MaeventContext>(o => o.UseSqlServer(Configuration["Data:ConnectionString"]));
            services.AddScoped<IMaeventRepository, MaeventRepository>();
            services.AddTransient<MaeventDbInitializer>();

            AutoMapper.Mapper.Initialize(cfg =>
                {
                    cfg.AddProfile(new EventProfileMapping());
                    cfg.AddProfile(new UserProfileMapping());
                });

            services.AddMvc()
                .AddJsonOptions(opt =>
                {
                    opt.SerializerSettings.ReferenceLoopHandling = ReferenceLoopHandling.Ignore;
                });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env, MaeventDbInitializer seeder)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseMvc();

            seeder.Seed().Wait();
        }
    }
}
