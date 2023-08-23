namespace message_service.Service
{
    public interface IAccountService
    {
        Task<long> GetAccountId(string apiKey);

        /*
         * Check if account is existing and not blocked
         */
        Task<Boolean> IsChattable(string apiKey, long accId);
    }
}
