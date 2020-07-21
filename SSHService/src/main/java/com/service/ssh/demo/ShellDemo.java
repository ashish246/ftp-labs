package com.service.ssh.demo;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

public class ShellDemo {
	
	public static void main(String[] args) {

		try {
			JSch jsch = new JSch();
			//jsch.setKnownHosts("known_hosts.txt");
			Session session = jsch.getSession("administrator", "192.168.1.2", 22);
			session.setPassword("ebsi@123".getBytes());
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel channel = session.openChannel("shell");
			channel.setInputStream(System.in);
			channel.setOutputStream(System.out);
			channel.connect();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
