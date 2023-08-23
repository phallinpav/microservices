using SixLabors.ImageSharp.Formats.Jpeg;

namespace file_service.Utils
{
    public static class ImgConverter
    {

        public static byte[] ConvertToJpegLimitedSize(byte[] imageData)
        {
            bool isAlreadyJpg = imageData.Length >= 2 && imageData[0] == 0xFF && imageData[1] == 0xD8;
            using (MemoryStream sourceStream = new MemoryStream(imageData))
            using (Image image = Image.Load(sourceStream))
            {
                int quality = 100; // Starting quality level

                while (true)
                {
                    using (MemoryStream jpegStream = new MemoryStream())
                    {
                        // Set the quality level for JPEG compression
                        var encoder = new JpegEncoder { Quality = quality };

                        // Resize the image if needed to reduce the file size
                        if (jpegStream.Length > 2 * 1024 * 1024) // 2MB
                        {
                            int newWidth = (int)(image.Width * 0.9);
                            int newHeight = (int)(image.Height * 0.9);
                            image.Mutate(x => x.Resize(newWidth, newHeight));
                        } else if (isAlreadyJpg)
                        {
                            return imageData;
                        }

                        // Save the image as JPEG with the specified quality level
                        image.Save(jpegStream, encoder);

                        // Check the resulting file size
                        if (jpegStream.Length <= 2 * 1024 * 1024) // 2MB
                        {
                            // File size is under 2MB, return the JPEG image
                            return jpegStream.ToArray();
                        }

                        // Adjust the quality level for the next iteration
                        quality -= 10;

                        // Break the loop if quality level goes below 10
                        if (quality < 10)
                        {
                            break;
                        }
                    }
                }

                // If no JPEG image could be generated under 2MB, return the original image data
                return imageData;
            }
        }
    }
}
