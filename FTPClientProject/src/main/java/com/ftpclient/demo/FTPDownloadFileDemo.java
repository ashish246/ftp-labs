package com.ftpclient.demo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * A program demonstrates how to upload files from local computer to a remote
 * FTP server using Apache Commons Net API.
 * 
 * Here are the steps to properly implement code for downloading a remote file
 * from a FTP server using Apache Commons Net API which is discussed so far: (1)
 * Connect and login to the server. (2) Enter local passive mode for data
 * connection. (3) Set file type to be transferred to binary. (4) Construct path
 * of the remote file to be downloaded. (5) Create a new OutputStream for
 * writing the file to disk. (6) If using the first method (retrieveFile): (7)
 * Pass the remote file path and the OutputStream as arguments of the method
 * retrieveFile(). (8) Close the OutputStream. (9) Check return value of
 * retrieveFile() to verify success. (10) If using the second method
 * (retrieveFileStream): (11) Retrieve an InputStream returned by the method
 * retrieveFileStream(). (12) Repeatedly a byte array from the InputStream and
 * write these bytes into the OutputStream, until the InputStream is empty. (13)
 * Call completePendingCommand() method to complete transaction. (14) Close the
 * opened OutputStream the InputStream. (15) Check return value of
 * completePendingCommand() to verify success. (16) Logout and disconnect from
 * the server.
 * 
 * @author www.codejava.net
 */
public class FTPDownloadFileDemo {

	public static void main(String[] args) {
		String server = "localhost";
		int port = 42042;
		String user = "auspost";
		String pass = "auspost42";

		FTPClient ftpClient = new FTPClient();
		try {

			ftpClient.connect(server, port);
			ftpClient.login(user, pass);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

			// APPROACH #1: using retrieveFile(String, OutputStream)
			String remoteFile1 = "charge_codes.sql";
			File downloadFile1 = new File(
					"src/main/java/com/ftpclient/data/charge_codes.sql");
			OutputStream outputStream1 = new BufferedOutputStream(
					new FileOutputStream(downloadFile1));
			boolean success = ftpClient
					.retrieveFile(remoteFile1, outputStream1);
			outputStream1.close();

			if (success) {
				System.out.println("File #1 has been downloaded successfully.");
			}

			// APPROACH #2: using InputStream retrieveFileStream(String)
			String remoteFile2 = "TestFTPFile";
			File downloadFile2 = new File("TestFTPFile");
			OutputStream outputStream2 = new BufferedOutputStream(
					new FileOutputStream(downloadFile2));
			
			InputStream inputStream = ftpClient.retrieveFileStream(remoteFile2);
			byte[] bytesArray = new byte[4096];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(bytesArray)) != -1) {
				outputStream2.write(bytesArray, 0, bytesRead);
			}

			success = ftpClient.completePendingCommand();
			if (success) {
				System.out.println("File #2 has been downloaded successfully.");
			}
			outputStream2.close();
			inputStream.close();

		} catch (IOException ex) {
			System.out.println("Error: " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (ftpClient.isConnected()) {
					ftpClient.logout();
					ftpClient.disconnect();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
