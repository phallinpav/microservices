using message_service.Model;
using MongoDB.Driver;

namespace message_service.Repo
{
    public class ChatRepo : IChatRepo
    {
        static readonly string _connectionServer;
        static readonly string _databaseName;

        private readonly IMongoClient _client;
        private readonly IMongoDatabase _database;

        public ChatRepo()
        {
            _connectionServer = Environment.GetEnvironmentVariable("MONGODB_CONNECTION_STRING") ?? "mongodb://localhost:27017";
            _databaseName = Environment.GetEnvironmentVariable("MONGODB_DATABASE_NAME") ?? "chat";
            _username = Environment.GetEnvironmentVariable("MONGODB_USERNAME") ?? "";
            _password = Environment.GetEnvironmentVariable("MONGODB_PASSWORD") ?? "";

            var mongoUrlBuilder = new MongoUrlBuilder(_connectionServer);
            mongoUrlBuilder.Username = _username;
            mongoUrlBuilder.Password = _password;
            mongoUrlBuilder.DatabaseName = _databaseName;

            _client = new MongoClient(mongoUrlBuilder.ToMongoUrl());
            _database = _client.GetDatabase(_databaseName);
        }

        public void ChatTo(long accFrom, long accTo, string message)
        {
            var collection = GetCollection(accFrom, accTo);

            Chat chat = new()
            {
                AccTo = accTo,
                AccFrom = accFrom,
                Message = message,
                SendAt = DateTime.UtcNow
            };

            collection.InsertOne(chat);
        }

        public List<Chat> GetChats(long accFrom, long accTo, int limit)
        {
            var collection = GetCollection(accFrom, accTo);

            var filter = Builders<Chat>.Filter.Empty;
            var sort = Builders<Chat>.Sort.Descending(c => c.SendAt);

            return collection.Find(filter).Sort(sort).Limit(limit).ToList();

        }

        private IMongoCollection<Chat> GetCollection(long accFrom, long accTo)
        {
            var collectionName = accFrom < accTo ? $"chat-{accFrom}-{accTo}" : $"chat-{accTo}-{accFrom}";
            return _database.GetCollection<Chat>(collectionName);
        }
    }
}
