using file_service.Model;

namespace file_service.Service
{
    public interface IFileService
    {
        Task<string> UploadImage(long accId, byte[] content, ImageType type);
    }
}
