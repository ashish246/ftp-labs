package com.service.ssh.demo;

import java.io.FileWriter;
import java.io.IOException;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UIKeyboardInteractive;
import com.jcraft.jsch.UserInfo;

/**
 * Creates a local_known_hosts file with HostKey
 * 
 * @author Administrator
 * 
 */
public class SFTPTest {

	public static void main(String[] args) {

		JSch jsch = new JSch();
		Session session = null;
		try {
			// new File("local_known_hosts");
			// StreamReader kr = new StreamReader(File.Open("local_known_hosts",
			// FileMode.OpenOrCreate));
			jsch.setKnownHosts("local_known_hosts");

			session = jsch.getSession("administrator", "192.168.1.2", 22); // default
																			// port
																			// is
																			// 22
			//session.setConfig("StrictHostKeyChecking", "no");
			UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.setPassword("ebsi@123".getBytes());
			session.connect();

			System.out.println(session.getHostKey().getKey());

			Channel channel = session.openChannel("sftp");
			channel.connect();

			System.out.println("Connected");

			ChannelSftp sftp = (ChannelSftp) channel;
			System.out.println(sftp.getHome());
			for (Object o : sftp.ls(sftp.getHome())) {
				System.out.println(((ChannelSftp.LsEntry) o).getFilename());
			}

		} catch (JSchException e) {
			e.printStackTrace(System.out);
			addHost(session.getHostKey().getKey());
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {

			session.disconnect();
			System.out.println("Disconnected");
		}
	}

	private static void addHost(String key) {
		try {
			FileWriter tmpwriter = new FileWriter("local_known_hosts", true);

			tmpwriter.append("192.168.1.2" + " ssh-rsa " + key + "\n");
			System.out.println("192.168.1.2" + " ssh-rsa " + key);

			tmpwriter.flush();
			tmpwriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class MyUserInfo implements UserInfo, UIKeyboardInteractive {

		public String[] promptKeyboardInteractive(String paramString1,
				String paramString2, String paramString3,
				String[] paramArrayOfString, boolean[] paramArrayOfBoolean) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getPassphrase() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getPassword() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean promptPassword(String paramString) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean promptPassphrase(String paramString) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean promptYesNo(String paramString) {
			// TODO Auto-generated method stub
			return false;
		}

		public void showMessage(String paramString) {
			// TODO Auto-generated method stub

		}

	}

}
