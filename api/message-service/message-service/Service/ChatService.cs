using System.Net.WebSockets;
using System.Text;
using message_service.Model;
using message_service.Repo;
using WebApplication2;

namespace message_service.Service
{
    public class ChatService : IChatService
    {
        private readonly IChatRepo _repo;

        public ChatService(IChatRepo repo)
        {
            _repo = repo;
        }

        public void ChatTo(long accFrom, long accTo, string message)
        {
            _repo.ChatTo(accFrom, accTo, message);
            WebSocketExtension.activeConnections.TryGetValue(accTo.ToString(), out var webSockets);
            if (webSockets is not null)
            {
                foreach (WebSocket webSocket in webSockets)
                {
                    webSocket.SendAsync(Encoding.UTF8.GetBytes($"new message from:{accFrom}"), WebSocketMessageType.Text, endOfMessage: true, CancellationToken.None);
                }
            }
        }

        public List<Chat> GetChats(long accFrom, long accTo, int limit)
        {
            return _repo.GetChats(accFrom, accTo, limit);
        }
    }
}
