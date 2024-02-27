package com.sparta.baemineats.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class UploadService {
    @Value("${image.upload-dir}")
    private String uploadDir;

    public String uploadImageAndGetUrl(MultipartFile image){
        try {
            // 파일 이름 생성
            String fileName = StringUtils.cleanPath(image.getOriginalFilename());
            // 저장할 경로
            Path targetLocation = Paths.get(uploadDir).resolve(fileName);
            // 파일 저장
            Files.copy(image.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/uploads/")
                    .path(fileName)
                    .toUriString();

//            String fileDownloadUri = "http://localhost:8080/uploads/" + fileName; //테스트 환경에서는 실제 HTTP 요청이 없기 때문에 테스트할 때 적용

            return fileDownloadUri;
        } catch (IOException ex) {
            throw new RuntimeException("파일 저장 중 오류 발생: " + ex.getMessage());
        }
    }
}
