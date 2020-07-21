package com.ftpclient.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * A program that demonstrates how to upload files from local computer to a
 * remote FTP server using Apache Commons Net API.
 * 
 * 
 * To properly write code to upload files to a FTP server using Apache Commons
 * Net API, the following steps should be followed: (1) Connect and login to the
 * server. (2) Enter local passive mode for data connection. (3) Set file type
 * to be transferred to binary. (4) Create an InputStream for the local file.
 * (5) Construct path of the remote file on the server. The path can be absolute
 * or relative to the current working directory. (6) Call one of the
 * storeXXX()methods to begin file transfer. There are two scenarios: (7) Using
 * an InputStream-based approach: this is the simplest way, since we let the
 * system does the ins and outs. There is no additional code, just passing the
 * InputStream object into the appropriate method, such as storeFile(String
 * remote, InputStream local) method. (8) Using an OutputStream-based approach:
 * this is more complex way, but more control. Typically we have to write some
 * code that reads bytes from the InputStream of the local file and writes those
 * bytes into the OutputStream which is returned by the storeXXX() method, such
 * as storeFileStream(String remote) method. (9) Close the opened InputStream
 * and OutputStream. (10) Call completePendingCommand() method to complete
 * transaction. (11) Logout and disconnect from the server.
 * 
 * @author www.codejava.net
 */
public class FTPUploadFileDemo {

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

			// APPROACH #1: uploads first file using an InputStream
			File firstLocalFile = new File(
					"src/main/java/com/ftpclient/data/TestFTPFile");

			String firstRemoteFile = "TestFTPFile";
			InputStream inputStream = new FileInputStream(firstLocalFile);

			System.out.println("Start uploading first file");
			boolean done = ftpClient.storeFile(firstRemoteFile, inputStream);
			inputStream.close();
			if (done) {
				System.out.println("The first file is uploaded successfully.");
			}

			// APPROACH #2: uploads second file using an OutputStream
			File secondLocalFile = new File(
					"src/main/java/com/ftpclient/data/TestFTPFile");
			String secondRemoteFile = "test/TestFTPFile";
			inputStream = new FileInputStream(secondLocalFile);

			System.out.println("Start uploading second file");
			OutputStream outputStream = ftpClient
					.storeFileStream(secondRemoteFile);
			byte[] bytesIn = new byte[4096];
			int read = 0;

			while ((read = inputStream.read(bytesIn)) != -1) {
				outputStream.write(bytesIn, 0, read);
			}
			inputStream.close();
			outputStream.close();

			boolean completed = ftpClient.completePendingCommand();
			if (completed) {
				System.out.println("The second file is uploaded successfully.");
			}

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