using message_service.Repo;
using message_service.Service;
using WebApplication2;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

// Add services to the container.

builder.Services.AddControllers();
builder.Services.AddScoped<IChatRepo, ChatRepo>();
builder.Services.AddScoped<IChatService, ChatService>();
builder.Services.AddScoped<IAccountService, AccountService>();
builder.Services.AddCors(options =>
{
    options.AddPolicy("AllowAll", builder =>
    {
        builder.AllowAnyOrigin()
               .AllowAnyMethod()
               .AllowAnyHeader();
    });
});

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}
// Configure the HTTP request pipeline.s

app.UseAuthorization();

app.MapControllers();

app.UseWebSocketNotification();

app.UseCors("AllowAll");

app.Run();
