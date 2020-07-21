package com.ftpserver.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.ftpserver.ftplet.DefaultFtplet;
import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpRequest;
import org.apache.ftpserver.ftplet.FtpSession;
import org.apache.ftpserver.ftplet.Ftplet;
import org.apache.ftpserver.ftplet.FtpletResult;

public class ImportFtplet extends DefaultFtplet implements Ftplet {
	private String mUploadFolder;

	private boolean mEventBasedImport;

	private boolean mAsyncImport;

	public ImportFtplet(FtpServerConfiguration ftpServerConfiguration) {
		mUploadFolder = ftpServerConfiguration.getUploadFolder();

		mEventBasedImport = ftpServerConfiguration.isEnableEventBasedImport();

		mAsyncImport = ftpServerConfiguration.isAsyncImport();
	}

	@Override
	public FtpletResult onUploadEnd(FtpSession pSession, FtpRequest pRequest)
			throws FtpException, IOException {
		Path tImportFile = Paths.get(mUploadFolder, pRequest.getArgument());

		if (Files.exists(tImportFile) && mEventBasedImport) {
			// mImportQueue.add(tImportFile);
		}

		return super.onUploadEnd(pSession, pRequest);
	}

	public void stop() throws InterruptedException {
		// mImportQueue.stop();
	}

	/**
	 * Invoked when AP_PCC_FTP_IMPORT job runs to process all the import files
	 * in batches for each type of file/data.
	 * 
	 * @return FtpletResult.DEFAULT
	 * @throws FtpException
	 * @throws IOException
	 */
	public FtpletResult onAsyncJobRun(File[] pFiles) throws FtpException,
			IOException {
		if (mAsyncImport) {
			for (File file : pFiles) {
				Path tImportFile = Paths.get(file.toURI());

				if (Files.exists(tImportFile)) {
					// mImportQueue.importFile(tImportFile);
				}
			}
		}

		return FtpletResult.DEFAULT;
	}
}
