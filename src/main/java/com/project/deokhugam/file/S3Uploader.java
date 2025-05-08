package com.project.deokhugam.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.net.HttpURLConnection;
import java.net.URL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component  // 또는 @Service
@RequiredArgsConstructor
public class S3Uploader {

  private final AmazonS3 amazonS3;

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  public String upload(MultipartFile file, String dirName) {
    String fileName = dirName + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    try (InputStream inputStream = file.getInputStream()) {
      amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
    } catch (IOException e) {
      throw new RuntimeException("S3 업로드 실패", e);
    }

    return amazonS3.getUrl(bucket, fileName).toString();
  }

  public String uploadImageFromUrl(String imageUrl, String dirName) {
    try {
      URL url = new URL(imageUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      // ✅ User-Agent 추가 (네이버 등 차단 방지)
      connection.setRequestProperty("User-Agent", "Mozilla/5.0");
      connection.connect();

      // ✅ 파일명 및 메타데이터 설정
      String fileName = dirName + "/" + UUID.randomUUID() + ".jpg";

      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(connection.getContentType());

      long contentLength = connection.getContentLengthLong();
      if (contentLength > 0) {
        metadata.setContentLength(contentLength);
      }

      try (InputStream inputStream = connection.getInputStream()) {
        amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, metadata));
      }

      return amazonS3.getUrl(bucket, fileName).toString();
    } catch (IOException e) {
      throw new RuntimeException("외부 이미지 업로드 실패: " + e.getMessage(), e);
    }
  }

}
