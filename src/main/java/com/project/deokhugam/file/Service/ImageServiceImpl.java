package com.project.deokhugam.file.Service;

import com.project.deokhugam.file.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

  private final S3Uploader s3Uploader;

  @Override
  public String uploadImage(MultipartFile file) {
    return s3Uploader.upload(file, "images");
  }
}
