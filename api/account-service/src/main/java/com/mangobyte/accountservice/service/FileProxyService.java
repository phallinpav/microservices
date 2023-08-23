package com.mangobyte.accountservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileProxyService {
    String uploadProfileImg(long accountId, MultipartFile image);
}
