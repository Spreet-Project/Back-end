package com.team1.spreet.global.infra.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import com.team1.spreet.global.error.exception.RestApiException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final AmazonS3 amazonS3;

	public String uploadFile(MultipartFile file) {
		String fileName = createFileName(file.getOriginalFilename());
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(file.getSize());
		objectMetadata.setContentType(file.getContentType());

		try(InputStream inputStream = file.getInputStream()) {
			amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

			return amazonS3.getUrl(bucket, fileName).toString();
		} catch(IOException e) {
			throw new RestApiException(ErrorStatusCode.FAIL_FILE_UPLOAD);
		}

	}

	public void deleteFile(String fileName) {
		amazonS3.deleteObject(new DeleteObjectRequest(bucket, fileName));
	}


	// 파일 업로드 시, 파일명을 난수화하기 위해 random 변환
	private String createFileName(String fileName) {
		return UUID.randomUUID().toString().concat(getFileExtension(fileName));
	}


	/**
	 * file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직
	 * 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
	 * */
	private String getFileExtension(String fileName) {
		try {
			return fileName.substring(fileName.lastIndexOf("."));
		} catch (StringIndexOutOfBoundsException e) {
			throw new RestApiException(ErrorStatusCode.INVALID_FILE);
		}
	}
}