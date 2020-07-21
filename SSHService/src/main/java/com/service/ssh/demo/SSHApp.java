package com.service.ssh.demo;

/**
 * Hello world!
 * 
 */
public class SSHApp {
	public static void main(String[] args) {

		String command = "ls";
		String userName = "administrator";
		String password = "ebsi@123";
		String connectionIP = "192.168.1.2";
		SSHManager instance = new SSHManager(userName, password, connectionIP,
				"");
		String errorMessage = instance.connect();

		if (errorMessage != null) {
			System.out.println(errorMessage);
		}

		String expResult = "FILE_NAME\n";
		// call sendCommand for each command and the output
		// (without prompts) is returned
		String result = instance.sendCommand(command);

		System.out.println("result : " + result);
		
		instance.close();
	}

}
