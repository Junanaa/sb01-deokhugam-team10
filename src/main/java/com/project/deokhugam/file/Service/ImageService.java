package com.project.deokhugam.file.Service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
  String uploadImage(MultipartFile file);
}