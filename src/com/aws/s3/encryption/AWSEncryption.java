package com.aws.s3.encryption;


public class AWSEncryption {

	private static final String FILE_KEY = "gi-joe";
	private static final String BUCKET_NAME = "20131103";
	private static final String FILE_TO_UPLOAD = "./gi-joe.jpg";

	public static void main(String[] args) {
		AwsS3ServerSideEncryption s3Client = new AwsS3ServerSideEncryption("access_key", "secret_key");
		s3Client.uploadFile(FILE_TO_UPLOAD).toFolder(BUCKET_NAME, FILE_KEY).usingServerSideEncryption().go();
	}

}