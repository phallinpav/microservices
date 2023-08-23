using message_service.Model;

namespace message_service.Service
{
    public interface IChatService
    {
        void ChatTo(long accFrom, long accTo, string message);
        List<Chat> GetChats(long accFrom, long accTo, int limit); 
    }
}
