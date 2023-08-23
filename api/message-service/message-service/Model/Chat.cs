using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace message_service.Model
{
    public class Chat
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        [BsonElement("message")]
        public string? Message { get; set; }

        [BsonElement("accFrom")]
        public long AccFrom { get; set; }

        [BsonElement("accTo")]
        public long AccTo { get; set; }


        [BsonElement("sendAt")]
        public DateTime SendAt { get; set; }

        [BsonElement("seenAt")]
        public DateTime? SeenAt { get; set; }
        
        [BsonElement("removeAt")]
        public DateTime? RemoveAt { get; set; }

    }
}
