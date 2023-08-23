using Newtonsoft.Json.Linq;

namespace message_service.Service
{
    public class AccountService : IAccountService
    {
        private static readonly string _server = "http://localhost:8080/";

        public async Task<long> GetAccountId(string apiKey)
        {
            string responseBody = await GetRequest("account", apiKey);

            JObject jsonObject = JObject.Parse(responseBody);
            if (jsonObject["id"] is not null)
            {
                if (long.TryParse(jsonObject["id"]!.ToString(), out long id))
                {
                    return id;
                }
            }
            throw new Exception("parse account response error!");
        }

        public async Task<bool> IsChattable(string apiKey, long accId)
        {
            string responseBody = await GetRequest($"friends", apiKey);

            JArray jsonArray = JArray.Parse(responseBody);
            return jsonArray.Any(jsonObject => { return (long) jsonObject["id"]! == accId; });
        }

        private static async Task<string> GetRequest(string path, string apiKey)
        {
            try
            {
                using HttpClient client = new();
                string apiUrl = $"{_server}{path}";
                client.DefaultRequestHeaders.Add("x-api-key", apiKey);

                HttpResponseMessage response = await client.GetAsync(apiUrl);

                if (response.IsSuccessStatusCode)
                {
                    return await response.Content.ReadAsStringAsync();
                }
                else
                {
                    throw new Exception("request proxy to account service fail!");
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("An error occurred: " + ex.Message);
                throw;
            }
        }

    }
}
