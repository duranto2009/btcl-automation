package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.mindrot.jbcrypt.BCrypt;

import org.apache.commons.codec.binary.Base64;

public final class PasswordService
{
  private static PasswordService instance;

  private PasswordService()
  {
  }

  public synchronized String encrypt(String plaintext) throws Exception
  {
    MessageDigest md = null;
    try
    {
      md = MessageDigest.getInstance("SHA"); //step 2
    }
    catch(NoSuchAlgorithmException e)
    {
      throw new Exception(e.getMessage());
    }
    try
    {
      md.update(plaintext.getBytes("UTF-8")); //step 3
    }
    catch(UnsupportedEncodingException e)
    {
      throw new Exception(e.getMessage());
    }

    byte raw[] = md.digest(); //step 4
    String hash = (new Base64()).encodeToString(raw); //step 5 
    return hash; //step 6
  }
  
  public static String hashPassword(String password_plaintext) {
		String salt = BCrypt.gensalt();
		String hashed_password = BCrypt.hashpw(password_plaintext, salt);
		StringBuilder hashed_password_builder = new StringBuilder(hashed_password);
		if(hashed_password.startsWith("$2a$")){
			hashed_password_builder.setCharAt(2, 'y');
		}
		return(hashed_password_builder.toString());
	}
  
  public static boolean checkPassword(String password_plaintext, String stored_hash) {
		boolean password_verified = false;

		if(null == stored_hash||!stored_hash.startsWith("$2y$"))
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");
		StringBuilder hashed_password_builder = new StringBuilder(stored_hash);
		if(stored_hash.startsWith("$2y$")){
			hashed_password_builder.setCharAt(2, 'a');
		}
		password_verified = BCrypt.checkpw(password_plaintext, hashed_password_builder.toString());

		return(password_verified);
	}

  public static synchronized PasswordService getInstance() //step 1
  {
    if(instance == null)
    {
       instance = new PasswordService(); 
    } 
    return instance;
  }
  
  public static void main(String args[]) throws Exception
  {
	  String plainPassword="hSLCZAQadV";
	  String encrypted = PasswordService.getInstance().encrypt(plainPassword);
	  System.out.println("encrypted " + encrypted);
	  
	  String encrypted2 = PasswordService.getInstance().hashPassword(plainPassword);
	  System.out.println("encrypted " + encrypted2);
	  
	  System.out.println("checked " +  PasswordService.getInstance().checkPassword(plainPassword, "$2y$10$utFPTsVmMTfVFBfzb.7TqOPq5ZPTsIY0A.WXCE7n.1NnxHFZtzDw2"));
  }
  
  
}
