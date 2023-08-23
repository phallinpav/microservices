using System.Text;
using Dropbox.Api;
using Dropbox.Api.Files;
using Dropbox.Api.Sharing;
using file_service.Model;
using file_service.Utils;
using static Dropbox.Api.Files.SearchMatchType;

namespace file_service.Service
{
    public class FileService : IFileService
    {
        private readonly string DROPBOX_TOKEN = "sl.Bf_F6CaCPquZH6Au2P23K5IjMGijDeX0Q9bw4O4gKSPv-k1TkFG5W4QConEJInSlAV_axe1xr1R1y5serMqThJ9uhCi_5yS-0_6P9QKDO7lxv2PD6MvJbUV8IfFp6iUMMvMwNXkk";
        private readonly DropboxClient dropboxClient;

        public FileService()
        {
            this.dropboxClient = new DropboxClient(DROPBOX_TOKEN);
        }

        public async Task<string> UploadImage(long accId, byte[] content, ImageType type)
        {
            content = ImgConverter.ConvertToJpegLimitedSize(content);
            using (var mem = new MemoryStream(content))
            {
                string folder = $"/accId_{accId}";
                try
                {
                    await dropboxClient.Files.CreateFolderV2Async(folder);
                }
                catch (ApiException<CreateFolderError>)
                {
                    // The folder already exists or an error occurred while creating it
                    // Handle the error or consider it already existing
                }

                string file = "emptyFileName";
                switch (type)
                {
                    case ImageType.PROFILE:
                        file = "profile.jpg";
                        break;
                    case ImageType.COVER:
                        file = "cover.jpg";
                        break;
                    case ImageType.POST:
                        file = "post.jpg";
                        break;
                }
                var uploadedFile = await dropboxClient.Files.UploadAsync(
                    folder + "/" + file,
                    WriteMode.Overwrite.Instance,
                    body: mem);

                var sharedLink = await dropboxClient.Sharing.CreateSharedLinkWithSettingsAsync(
                    uploadedFile.PathDisplay);

                // Extract the shared link URL from the result
                var sharedLinkUrl = sharedLink.Url;

                sharedLinkUrl = sharedLinkUrl.Replace("www.dropbox.com", "dl.dropboxusercontent.com");

                return sharedLinkUrl;

            }

        }
    }
}
