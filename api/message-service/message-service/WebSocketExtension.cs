using System.Net.WebSockets;
using System.Net;
using System.Collections.Concurrent;
using System.Collections.Generic;

namespace WebApplication2
{
    public static class WebSocketExtension
    {
        public static readonly ConcurrentDictionary<string, ConcurrentBag<WebSocket>> activeConnections = new();

        public static IApplicationBuilder UseWebSocketNotification(this WebApplication app)
        {
            app.UseWebSockets();

            app.Map("/chat-notify/{accId}", async context =>
            {
                string? accId = context.Request.RouteValues["accId"] as string;
                
                if (context.WebSockets.IsWebSocketRequest && accId is not null)
                {
                    using WebSocket webSocket = await context.WebSockets.AcceptWebSocketAsync();
                    if (activeConnections.TryGetValue(accId, out var existingSockets))
                    {
                        existingSockets.Add(webSocket);
                    } else
                    {
                        ConcurrentBag<WebSocket> webSockets = new ConcurrentBag<WebSocket>
                        {
                            webSocket
                        };
                        activeConnections.TryAdd(accId, webSockets);
                    }
                    await HandleWebSocket(webSocket);
                    if (activeConnections.TryGetValue(accId, out existingSockets))
                    {
                        if (!existingSockets.TryTake(out _))
                        {
                            Console.WriteLine($"fail to remove socket from {accId}");
                        }
                        if (existingSockets.IsEmpty)
                        {
                            activeConnections.TryRemove(accId, out _);
                        }
                    }
                }
                else
                {
                    context.Response.StatusCode = (int)HttpStatusCode.BadRequest;
                }
            });

            return app;
        }

        private static async Task HandleWebSocket(WebSocket webSocket)
        {
            var buffer = new byte[1024];
            while (webSocket.State == WebSocketState.Open)
            {
                var result = await webSocket.ReceiveAsync(buffer, CancellationToken.None);
                if (result.MessageType == WebSocketMessageType.Close)
                {
                    await webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Connection closed by the client", CancellationToken.None);
                    return;
                }
            }
        }
    }
}
