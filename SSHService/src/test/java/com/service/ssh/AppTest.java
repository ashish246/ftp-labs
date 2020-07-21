package com.service.ssh;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.service.ssh.demo.SSHManager;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	/**
	 * Create the test case
	 * 
	 * @param testName
	 *            name of the test case
	 */
	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * Rigourous Test :-)
	 */
	public void testApp() {
		assertTrue(true);
	}

	/**
	 * Test of sendCommand method, of class SSHManager.
	 */
	public void testSendCommand() {
		System.out.println("sendCommand");

		/**
		 * YOU MUST CHANGE THE FOLLOWING FILE_NAME: A FILE IN THE DIRECTORY
		 * USER: LOGIN USER NAME PASSWORD: PASSWORD FOR THAT USER HOST: IP
		 * ADDRESS OF THE SSH SERVER
		 **/
		String command = "ls soapui-settings.xml";
		String userName = "administrator";
		String password = "ebsi@123";
		String connectionIP = "192.168.1.44";
		SSHManager instance = new SSHManager(userName, password, connectionIP,
				"");
		String errorMessage = instance.connect();

		if (errorMessage != null) {
			System.out.println(errorMessage);
			fail();
		}

		String expResult = "soapui-settings.xml\n";
		// call sendCommand for each command and the output
		// (without prompts) is returned
		String result = instance.sendCommand(command);
		// close only after all commands are sent
		instance.close();
		assertEquals(expResult, result);
	}
}
