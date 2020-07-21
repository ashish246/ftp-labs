package com.ftpserver.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.ftpserver.ConnectionConfigFactory;
import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.UserManager;
import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.PropertiesUserManagerFactory;
import org.apache.ftpserver.usermanager.SaltedPasswordEncryptor;
import org.apache.ftpserver.usermanager.impl.BaseUser;

public class FtpService {
	public static final String[] IMPORT_SUB_FOLDERS = new String[] {
			"catalogs", "products", "pricelists", "customers" };

	private static final Logger logger = Logger.getLogger("FTPService");

	private FtpServerThread mFtpServerThread;

	private Map<String, Ftplet> mFtplets = new HashMap<>();

	public FtpService() {

	}

	public void start(Properties configProp) throws IOException, FtpException {

		PropertiesUserManagerFactory tUMFactory = new PropertiesUserManagerFactory();
		tUMFactory.setPasswordEncryptor(new SaltedPasswordEncryptor());
		UserManager tUserManager = tUMFactory.createUserManager();

		String tDomainName = "AusPostCOM-PCC";

		FtpServerConfiguration aChannelConfiguration = new FtpServerConfiguration(
				configProp, tDomainName);

		// Make sure the ftp folder is created
		aChannelConfiguration.verifyUploadFolder();

		// Create read-write user for domain if exists
		BaseUser tFtpUser = aChannelConfiguration.getWriteUser();
		if (null != tFtpUser)
			tUserManager.save(tFtpUser);

		// Create read-only user for domain if exists
		tFtpUser = aChannelConfiguration.getReadUser();
		if (null != tFtpUser)
			tUserManager.save(tFtpUser);

		if (aChannelConfiguration.isEnableImport()) {

			mFtplets.put(tDomainName, new ImportFtplet(aChannelConfiguration));
		}

		try {
			FtpServer tServer = createFtpServer(
					aChannelConfiguration.getPort(), tUserManager,
					aChannelConfiguration.getMaxLogins(),
					aChannelConfiguration.getIdleTimeout());

			mFtpServerThread = new FtpServerThread(tServer);

			new Thread(mFtpServerThread).start();
		} catch (FtpException | IOException ioex) {
			stop();
			logger.log(Level.SEVERE, ioex.getMessage(), this);
		}
	}

	public void stop() {
		for (Ftplet tFtptlet : mFtplets.values()) {
			try {

				((ImportFtplet) tFtptlet).stop();

			} catch (InterruptedException iEx) {
				logger.log(Level.SEVERE, iEx.getMessage(), this);
			}
		}

		if (mFtpServerThread != null) {
			mFtpServerThread.stopServer();
		}
	}

	private FtpServer createFtpServer(int pFtpPort, UserManager pUserManager,
			int pMaxLogins, int pIdleTimeout) throws FtpException, IOException {

		ListenerFactory tListenerFactory = new ListenerFactory();
		tListenerFactory.setPort(pFtpPort);
		tListenerFactory.setIdleTimeout(pIdleTimeout);

		FtpServerFactory tServerFactory = new FtpServerFactory();

		tServerFactory
				.addListener("default", tListenerFactory.createListener());
		tServerFactory.setUserManager(pUserManager);
		tServerFactory.setFtplets(mFtplets);

		if (pMaxLogins > 0) {
			ConnectionConfigFactory ccf = new ConnectionConfigFactory();
			ccf.setMaxLogins(pMaxLogins);
			tServerFactory.setConnectionConfig(ccf.createConnectionConfig());
		}

		return tServerFactory.createServer();
	}
}
