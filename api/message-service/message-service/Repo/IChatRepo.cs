using message_service.Model;

namespace message_service.Repo
{
    public interface IChatRepo
    {
        void ChatTo(long accFrom, long accTo, string message);

        List<Chat> GetChats(long accFrom, long accTo, int limit);
    }
}
