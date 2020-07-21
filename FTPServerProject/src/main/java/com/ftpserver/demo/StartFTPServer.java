package com.ftpserver.demo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.ftpserver.ftplet.FtpException;

import com.ftpserver.config.FtpService;

public class StartFTPServer {

	public static void main(String[] args) {

		Properties configProp = new Properties();

		InputStream in = null;
		try {
			in = new FileInputStream("ftp_server_config.properties");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//InputStream in = StartFTPServer.class.getClassLoader().getResourceAsStream("ftp_server_config.properties");

		try {
			configProp.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}

		FtpService tFtpServer = new FtpService();

		try {
			tFtpServer.start(configProp);
		} catch (IOException | FtpException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// stop the service
		tFtpServer.stop();
	}

}
