package com.ftpserver.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.ftplet.FtpException;

public class FtpServerThread implements Runnable {
	private FtpServer mServerInstance;

	private static final Logger logger = Logger.getLogger("FTPService");

	public FtpServerThread(FtpServer pServerInstance) {
		mServerInstance = pServerInstance;
	}

	@Override
	public void run() {
		try {
			logger.log(Level.INFO, "Starting FTP import service.", this);
			mServerInstance.start();
		} catch (FtpException ftpEx) {
			logger.log(Level.SEVERE, ftpEx.getMessage(), this);
		}
	}

	public void stopServer() {
		if (mServerInstance != null) {
			logger.log(Level.INFO, "Stopping FTP import service.", this);
			mServerInstance.stop();
		}
	}

}
