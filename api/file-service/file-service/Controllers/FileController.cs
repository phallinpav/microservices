using file_service.Service;
using Microsoft.AspNetCore.Mvc;
using file_service.Model;
using System;

namespace file_service.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class FileController : ControllerBase
    {
        private readonly IFileService _fileService;

        public FileController(IFileService fileService)
        {
            _fileService = fileService;
        }

        [HttpPost("/image/upload")]
        public async Task<IActionResult> UploadFile(IFormFile image, [FromForm] long accountId, [FromForm] string type)
        {
            // Check if a file was provided
            if (image == null || image.Length == 0)
            {
                return BadRequest("No file was provided.");
            } else if (!IsImage(image))
            {
                return BadRequest("file provided is not image type.");
            }
                
            // Read the file content into a byte array
            byte[] fileBytes;
            using (var memoryStream = new MemoryStream())
            {
                await image.CopyToAsync(memoryStream);
                fileBytes = memoryStream.ToArray();
            }

            if (ImageType.TryParse(type, true, out ImageType _type)) {
                string imageUrl = await _fileService.UploadImage(accountId, fileBytes, _type);
                return Ok(imageUrl);
            } else
            {
                return BadRequest("invalid fileType");
            }
        }

        private static bool IsImage(IFormFile file)
        {
            // Get the content type of the file
            string contentType = file.ContentType;

            // Check if the content type indicates an image
            return contentType.StartsWith("image/");
        }
    }
}
