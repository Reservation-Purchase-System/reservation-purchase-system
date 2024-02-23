package com.nayoon.user_service.common.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nayoon.user_service.common.exception.CustomException;
import com.nayoon.user_service.common.exception.ErrorCode;
import com.nayoon.user_service.user.entity.User;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

  private final AmazonS3 amazonS3;

  @Value("${aws.s3.bucket}")
  private String bucket;

  // TODO: 연결 유실이 발생할 경우 다시 업로드 하도록 로직 수정 필요
  /**
   * S3 파일 업로드 메서드
   */
  public String saveFile(MultipartFile file, User user, String path) throws IOException {
    if (file == null) {
      throw new CustomException(ErrorCode.NO_FILE);
    }

    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentLength(file.getSize());
    metadata.setContentType(file.getContentType());

    String fileName = path + user.getId() + "/" + UUID.randomUUID();

    amazonS3.putObject(bucket, fileName, file.getInputStream(), metadata);
    return amazonS3.getUrl(bucket, fileName).toString();
  }

  /**
   * S3 파일 삭제 메서드
   */
  public void deleteFile(String file) {
    amazonS3.deleteObject(bucket, file);
  }

}
