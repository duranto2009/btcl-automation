/*
 * Created on Mar 7, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package config;


/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Configuration {

	public String server;
	public String username;
	public String password;

	public String toString() {
		String s = "";
		s += "\n server = " + server;
		s += "\n username = " + username;
		s += "\n password = " + password;
		return s;
	}
	
}
