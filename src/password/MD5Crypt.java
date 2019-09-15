package password;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5Crypt
{
    private static final String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final String itoa64 = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static final String USAGE = "MD5Crypt <password> <salt>";
    
    public MD5Crypt()
    {
    }

    private static final String to64(long v, int size)
    {
        StringBuffer result = new StringBuffer();
        while(--size >= 0) 
        {
            result.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt((int)(v & 63L)));
            v >>>= 6;
        }
        return result.toString();
    }

    private static final void clearbits(byte bits[])
    {
        for(int i = 0; i < bits.length; i++)
            bits[i] = 0;

    }

    private static final int bytes2u(byte inp)
    {
        return inp & 0xff;
    }

    public static final String crypt(String password)
    {
        StringBuffer salt = new StringBuffer();
        Random rnd = new Random();
        int index;
        for(; salt.length() < 8; salt.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".substring(index, index + 1)))
            index = (int)(rnd.nextFloat() * (float)"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".length());

        return crypt(password, salt.toString(), "$1$");
    }

    public static final String crypt(String password, String salt)
    {
        return crypt(password, salt, "$1$");
    }

    public static final String crypt(String password, String salt, String magic)
    {
        MessageDigest ctx;
        MessageDigest ctx1;
        try
        {
            ctx = MessageDigest.getInstance("md5");
            ctx1 = MessageDigest.getInstance("md5");
        }
        catch(NoSuchAlgorithmException ex)
        {
            System.err.println(ex);
            return null;
        }
        if(salt.startsWith(magic))
            salt = salt.substring(magic.length());
        if(salt.indexOf('$') != -1)
            salt = salt.substring(0, salt.indexOf('$'));
        if(salt.length() > 8)
            salt = salt.substring(0, 8);
        ctx.update(password.getBytes());
        ctx.update(magic.getBytes());
        ctx.update(salt.getBytes());
        ctx1.update(password.getBytes());
        ctx1.update(salt.getBytes());
        ctx1.update(password.getBytes());
        byte finalState[] = ctx1.digest();
        for(int pl = password.length(); pl > 0; pl -= 16)
            ctx.update(finalState, 0, pl <= 16 ? pl : 16);

        clearbits(finalState);
        for(int i = password.length(); i != 0; i >>>= 1)
            if((i & 1) != 0)
                ctx.update(finalState, 0, 1);
            else
                ctx.update(password.getBytes(), 0, 1);

        finalState = ctx.digest();
        for(int i = 0; i < 1000; i++)
        {
            try
            {
                ctx1 = MessageDigest.getInstance("md5");
            }
            catch(NoSuchAlgorithmException e0)
            {
                return null;
            }
            if((i & 1) != 0)
                ctx1.update(password.getBytes());
            else
                ctx1.update(finalState, 0, 16);
            if(i % 3 != 0)
                ctx1.update(salt.getBytes());
            if(i % 7 != 0)
                ctx1.update(password.getBytes());
            if((i & 1) != 0)
                ctx1.update(finalState, 0, 16);
            else
                ctx1.update(password.getBytes());
            finalState = ctx1.digest();
        }

        StringBuffer result = new StringBuffer();
        result.append(magic);
        result.append(salt);
        result.append("$");
        long l = bytes2u(finalState[0]) << 16 | bytes2u(finalState[6]) << 8 | bytes2u(finalState[12]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[1]) << 16 | bytes2u(finalState[7]) << 8 | bytes2u(finalState[13]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[2]) << 16 | bytes2u(finalState[8]) << 8 | bytes2u(finalState[14]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[3]) << 16 | bytes2u(finalState[9]) << 8 | bytes2u(finalState[15]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[4]) << 16 | bytes2u(finalState[10]) << 8 | bytes2u(finalState[5]);
        result.append(to64(l, 4));
        l = bytes2u(finalState[11]);
        result.append(to64(l, 2));
        clearbits(finalState);
        return result.toString();
    }

    public static void main(String args[])
    {
        //System.out.println(crypt("pwxq*%72"));
    }
}