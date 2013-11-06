package com.aws.s3.encryption;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public class AwsS3ServerSideEncryption {

	private File imageToUpload;
	private PutObjectRequest putRequest;
	private final BasicAWSCredentials awsCredentials;
	private AmazonS3Client s3;

	public AwsS3ServerSideEncryption(String accessKey, String secretKey) {
		this.awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
	}

	public AwsS3ServerSideEncryption uploadFile(String fileToUpload) {
		this.imageToUpload = new File(fileToUpload);
		return this;
	}

	public AwsS3ServerSideEncryption toFolder(String bucket, String fileKey) {
		this.putRequest = new PutObjectRequest(bucket, fileKey, imageToUpload);
		return this;
	}

	public AwsS3ServerSideEncryption usingServerSideEncryption() {
		encryptObject(putRequest);
		return this;
	}

	protected void encryptObject(PutObjectRequest putRequest) {
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setServerSideEncryption(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
		putRequest.setMetadata(objectMetadata);
	}

	public void go() {
		PutObjectResult response = dispatchObject(putRequest);
		System.out.println("Uploaded object encryption status is " + response.getServerSideEncryption());
	}

	private PutObjectResult dispatchObject(PutObjectRequest putRequest) {
		this.s3 = new AmazonS3Client(awsCredentials);
		PutObjectResult response = s3.putObject(putRequest);
		return response;
	}

	public void getPhoto(String bucket, String fileKey, String localFile) {
		S3Object object = s3.getObject(new GetObjectRequest(bucket, fileKey));
		InputStream objectData = object.getObjectContent();
		try {
			OutputStream outputStream = new FileOutputStream(new File(localFile));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = objectData.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			System.out.println("Done!");
			objectData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}