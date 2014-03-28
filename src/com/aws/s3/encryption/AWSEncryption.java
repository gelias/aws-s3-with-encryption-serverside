package com.aws.s3.encryption;


public class AWSEncryption {

	private static final String LOCAL_FILE = "/tmp/photo";
	private static final String FILE_KEY = "gi-joe";
	private static final String BUCKET_NAME = "20131103";
	private static final String FILE_TO_UPLOAD = "./gi-joe.jpg";

	public static void main(String[] args) {
		AwsS3ServerSideEncryption s3Client = new AwsS3ServerSideEncryption("", "");
		s3Client.uploadFile(FILE_TO_UPLOAD).toFolder(BUCKET_NAME, FILE_KEY).usingServerSideEncryption().go();
		s3Client.getPhoto(BUCKET_NAME, FILE_KEY, LOCAL_FILE);
	}

}
