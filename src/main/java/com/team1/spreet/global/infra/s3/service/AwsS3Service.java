package com.team1.spreet.global.infra.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.team1.spreet.global.error.exception.RestApiException;
import com.team1.spreet.global.error.model.ErrorStatusCode;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AwsS3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;
	private final AmazonS3 amazonS3;
	private static final int TARGET_HEIGHT = 650;

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

	public String uploadImage(MultipartFile file) throws IOException {
		String fileFormat = Objects.requireNonNull(file.getContentType())
			.substring(file.getContentType().lastIndexOf("/") + 1).toLowerCase();

		String fileName = createFileName(file.getOriginalFilename());

		BufferedImage croppedImage = resizeImage(file);

		ObjectMetadata objectMetadata = new ObjectMetadata();
		ByteArrayInputStream byteArrayInputStream = convertImage(croppedImage, file.getContentType(),
			fileFormat, objectMetadata);

		amazonS3.putObject(new PutObjectRequest(bucket, fileName, byteArrayInputStream, objectMetadata)
			.withCannedAcl(CannedAccessControlList.PublicRead));

		return amazonS3.getUrl(bucket, fileName).toString();
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

	// imgscalr 라이브러리로 resizing
	private BufferedImage resizeImage(MultipartFile multipartFile) throws IOException {

		BufferedImage sourceImage = ImageIO.read(multipartFile.getInputStream());

		if (sourceImage.getHeight() <= TARGET_HEIGHT) {
			return sourceImage;
		}

		double sourceImageRatio = (double) sourceImage.getWidth() / sourceImage.getHeight();

		int newWidth = (int) (TARGET_HEIGHT * sourceImageRatio);

		return Scalr.resize(sourceImage, newWidth, TARGET_HEIGHT);
	}

	private ByteArrayInputStream convertImage(BufferedImage croppedImage, String contentType,
		String fileFormat, ObjectMetadata objectMetadata) throws IOException {

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(croppedImage, fileFormat, byteArrayOutputStream);

		objectMetadata.setContentType(contentType);
		objectMetadata.setContentLength(byteArrayOutputStream.size());

		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}
}