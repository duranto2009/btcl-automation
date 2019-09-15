package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5
{

    public MD5()
    {
    }

    public synchronized byte[] hash(byte input[])
    {
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch(NoSuchAlgorithmException nosuchalgorithmexception) { }
        md5.reset();
        md5.update(input, 0, input.length);
        byte digest[] = md5.digest();
        return toHex(digest);
    }

    public byte[] toHex(byte a[])
    {
        int index = 0;
        byte b[] = new byte[a.length << 1];
        for(int i = 0; i < a.length; i++)
        {
            b[index + (i << 1)] = hex[a[i] >>> 4 & 15];
            b[index + (i << 1) + 1] = hex[a[i] & 15];
        }

        return b;
    }

    public static String getNonce()
    {
        if(nonce == null || System.currentTimeMillis() - nonceCreationTime > 300000L)
        {
            oldnonce = nonce;
            byte bytes[] = new byte[4];
            (new Random()).nextBytes(bytes);
            char hexArray[] = "0123456789ABCDEF".toCharArray();
            char hexChars[] = new char[bytes.length * 2];
            for(int j = 0; j < bytes.length; j++)
            {
                int v = bytes[j] & 255;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 15];
            }

            nonce = new String(hexChars);
            nonceCreationTime = System.currentTimeMillis();
        }
        return nonce;
    }

    public static String getOldNonce()
    {
        if(oldnonce != null)
            return oldnonce;
        else
            return nonce;
    }

    private MessageDigest md5;
    private static String nonce = null;
    private static String oldnonce = null;
    public static long nonceCreationTime = 0L;
    public static int SUCCESS = 0;
    public static int FAILED = 1;
    public static int ERROR_INVALID_TYPE = 100;
    public static int INTERNAL_SERVER_ERROR = 101;
    public static int ERROR_INVALID_PARAMETER = 102;
    public static int ERROR_INVALID_PIN = 103;
    public static int ERROR_INVALID_PIN_OR_PASS = 104;
    public static int ERROR_INSUFFICIENT_PARAMETER = 105;
    public static int INVALID_NONCE = 106;
    public static int INVALID_PASSWORD = 108;
    public static int INVALID_USER = 109;
    public static int NEW_NONCE_CREATED = 110;
    public static int HASH_MATCH_FOUND = 111;
    public static int HASH_MATCH_NOT_FOUND = 112;
    public static int INSUFFICIENT_PARAMETER = 113;
    public static int INVALID_PARAMETER = 114;
    public static int PP_NOT_SET = 115;
    public static int SUCCESSFULLY_DOWNLOADED = 116;
    public static int FILE_DATA_CORRUPTED = 117;
    public static int PAYMENT_SUCCESSFUL = 118;
    public static int INVALID_SERIAL_CARD_NO = 119;
    public static int PAYMENT_FAILURE = 120;
    public static int COUNT_NOT_WITHIN_VALID_RANGE = 121;
    public static int RECHARGE_HISTORY_FOUND = 122;
    public static int PAYMENT_AMOUNT_UNMATCHED = 123;
    public static int PAYMENT_CURRENCY_UNMATCHED = 124;
    public static int DUPLICATE_PAYMENT_REQUEST = 125;
    public static int INCOMPLETE_PAYMENT = 130;
    public static int TRANSACTION_CANCELLED_BY_CUSTOMER = 131;
    public static int REFUSED_PAYMENT_BY_ACQUIRER = 132;
    public static int UNCERTAIN_PAYMENT = 133;
    public static int REFUSED_PAYMENT_FOR_TECHNICAL_ERROR = 134;
    public static int UNAUTHORIZED_PAYMENT = 135;
    public static int NOT_AVAILABLE = 136;
    public static int INVALID_SERVICE = 401;
    public static int INVALID_FROM_TIME = 405;
    public static int INVALID_TO_TIME = 406;
    public static int INVALID_ACCOUNT_ID = 407;
    public static int SERVICE_TYPE_CALLHISTORY = 1;
    public static int IP_RESTRICTED = 5400;
    public static int ERROR_NO_COUNTRY_AVAILABLE = 5009;
    public static int ERROR_NO_OPERATOR_AVAILABLE = 5010;
    public static int ERROR_INVALID_COUNTRY = 103;
    public static int ERROR_UNKNOWN = 5011;
    public static final String ACCCESS_TOKEN = "\"access_token\"";
    public static final String EXPIRES = "\"expires_in\"";
    public static long accessTokenExpireTime = 0L;
    public static String accessToken = null;
    private byte hex[] = {
        48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 
        97, 98, 99, 100, 101, 102
    };

}