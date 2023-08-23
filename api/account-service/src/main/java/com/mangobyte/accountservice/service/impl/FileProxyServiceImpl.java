package com.mangobyte.accountservice.service.impl;

import com.mangobyte.accountservice.service.FileProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileProxyServiceImpl implements FileProxyService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${file.server}")
    private String SERVER;

    @Override
    public String uploadProfileImg(long accountId, MultipartFile image) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> requestBody = new LinkedMultiValueMap<>();
        try {
            // Create a temporary file
            String[] fileNames = image.getOriginalFilename().split("\\.");
            String extension = "." + fileNames[fileNames.length - 1];
            File tempFile = File.createTempFile("temp", extension);
            image.transferTo(tempFile);

            // Create a FileSystemResource from the temporary file
            FileSystemResource fileSystemResource = new FileSystemResource(tempFile);
            
            requestBody.add("image", fileSystemResource);
            requestBody.add("type", "profile");
            requestBody.add("accountId", accountId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        String url = SERVER + "/image/upload";
        return restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class).getBody();
    }
}
