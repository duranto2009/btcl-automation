package password;

public class HashPassword
{

    public HashPassword()
    {
    }

    public static boolean check(String PlainTextPassword, String CrypPassword)
    {
        String NewCrypPassword = new String();
        String Salt = new String();
        if(CrypPassword.length() > 13)
        {
            Salt = CrypPassword.substring(0, 11);
            NewCrypPassword = MD5Crypt.crypt(PlainTextPassword, Salt);
        } 
        else
        {
            Salt = CrypPassword.substring(0, 2);
            NewCrypPassword = jcrypt.crypt(Salt, PlainTextPassword);
        }
        
      return NewCrypPassword.equals(CrypPassword);
      
    }
}