package com.ftpserver.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

/**
 * helper class to handle ftp configuration
 * 
 * @author Administrator
 * 
 */
public class FtpServerConfiguration {
	private int tPort = 21;
	private int tMaxLogins = 0;
	private int tIdleTimeout = 300;

	private String tWriteUser = null;
	private String tWritePassword = null;
	private String tReadUser = null;
	private String tReadPassword = null;

	private int tUserMaxIdleItem = tIdleTimeout;

	private boolean tArchiveFiles = false;
	private boolean tCompressFiles = false;
	private boolean tEnableImport = false;
	private boolean tEnableEventBasedImport = false;
	private boolean tAsyncImport = false;

	private String tUploadFolder = null;

	public FtpServerConfiguration() {
	}

	public FtpServerConfiguration(Properties configProp, String pDomainName) {

		tPort = Integer.parseInt(configProp
				.getProperty("ftpservice.port", "21"));
		tMaxLogins = Integer.parseInt(configProp.getProperty(
				"ftpservice.maxlogins", "0"));
		tIdleTimeout = Integer.parseInt(configProp.getProperty(
				"ftpservice.idletimeout", "300"));

		tWriteUser = configProp.getProperty("ftpservice." + pDomainName
				+ ".write.user", null);
		tWritePassword = configProp.getProperty("ftpservice." + pDomainName
				+ ".write.password", null);
		tReadUser = configProp.getProperty("ftpservice." + pDomainName
				+ ".read.user", null);
		tReadPassword = configProp.getProperty("ftpservice." + pDomainName
				+ ".read.password", null);
		tUserMaxIdleItem = Integer.parseInt(configProp.getProperty(
				"ftpservice." + pDomainName + ".user.maxidletime",
				String.valueOf(tIdleTimeout)));
		tArchiveFiles = Boolean.parseBoolean(configProp.getProperty(
				"ftpservice." + pDomainName + ".archive", "false"));
		tCompressFiles = Boolean.parseBoolean(configProp.getProperty(
				"ftpservice." + pDomainName + ".compress", "false"));
		tEnableImport = Boolean.parseBoolean(configProp.getProperty(
				"ftpservice." + pDomainName + ".enable.import", "false"));
		tEnableEventBasedImport = Boolean.parseBoolean(configProp.getProperty(
				"ftpservice." + pDomainName + ".enable.event.import", "false"));
		tAsyncImport = Boolean.parseBoolean(configProp.getProperty(
				"ftpservice." + pDomainName + ".enable.async.import", "false"));

		tUploadFolder = "/ftp/";
	}

	public int getPort() {
		return tPort;
	}

	public int getMaxLogins() {
		return tMaxLogins;
	}

	public int getIdleTimeout() {
		return tIdleTimeout;
	}

	public boolean isArchiveFiles() {
		return tArchiveFiles;
	}

	public boolean isCompressFiles() {
		return tCompressFiles;
	}

	public boolean isEnableImport() {
		return tEnableImport;
	}

	public boolean isEnableEventBasedImport() {
		return tEnableEventBasedImport;
	}

	public boolean isAsyncImport() {
		return tAsyncImport;
	}

	public String getUploadFolder() {
		return tUploadFolder;
	}

	public boolean verifyUploadFolder() throws IOException {

		Path uploadFolderPath = Paths.get(tUploadFolder);
		if (Files.exists(uploadFolderPath))
			return true;

		return (null != Files.createDirectories(uploadFolderPath));
	}

	public BaseUser getWriteUser() {
		if (tWriteUser == null || tWritePassword == null)
			return null;

		BaseUser tFtpUser = new BaseUser();

		tFtpUser.setName(tWriteUser);
		tFtpUser.setPassword(tWritePassword);
		tFtpUser.setHomeDirectory(tUploadFolder);
		tFtpUser.setMaxIdleTime(tUserMaxIdleItem);

		List<Authority> tAuthorities = new ArrayList<>();
		tAuthorities.add(new WritePermission());
		tFtpUser.setAuthorities(tAuthorities);
		return tFtpUser;
	}

	public BaseUser getReadUser() {
		if (tReadUser == null || tReadPassword == null)
			return null;

		BaseUser tFtpUser = new BaseUser();

		tFtpUser.setName(tReadUser);
		tFtpUser.setPassword(tReadPassword);
		tFtpUser.setHomeDirectory(tUploadFolder);
		tFtpUser.setMaxIdleTime(tUserMaxIdleItem);
		return tFtpUser;
	}

}
