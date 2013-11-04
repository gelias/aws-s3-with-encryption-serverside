package com.aws.s3.encryption;

import java.io.File;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

public class AwsS3ServerSideEncryption {

	private File imageToUpload;
	private PutObjectRequest putRequest;
	private final BasicAWSCredentials awsCredentials;

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
		AmazonS3Client s3 = new AmazonS3Client(awsCredentials);
		PutObjectResult response = s3.putObject(putRequest);
		return response;
	}

}