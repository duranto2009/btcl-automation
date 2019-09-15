package util;


public class DAOResult
{
	public static final int DONE = 0;
    public static final int VALIDATION_ERROR = 1;
    public static final int DB_EXCEPTION = 2;
    private String message;
    private boolean successful;
    private int type;
    
    public DAOResult()
    {
        successful = false;
        message = "";
    }

    public DAOResult(String p_message, boolean p_successful)
    {
        successful = p_successful;
        message = p_message;
    }

    public DAOResult(String p_message, boolean p_successful, int p_type)
    {
        message = p_message;
        successful = p_successful;
        type = p_type;
    }

    public void setResult(String p_message, boolean p_successful, int p_type)
    {
        message = p_message;
        successful = p_successful;
        type = p_type;
    }

    public void setMessage(String p_message)
    {
        message = p_message;
    }

    public String getMessage()
    {
        return message;
    }

    public boolean isSuccessful()
    {
        return successful;
    }

    public void setSuccessful(boolean p_successful)
    {
        successful = p_successful;
    }

    public void setType(int p_type)
    {
        type = p_type;
    }

    public int getType()
    {
        return type;
    }

    public String toString()
    {
        String s = "";
        String errorDesc = null;
        switch(type)
        {
        case 0: // '\0'
            errorDesc = "DONE";
            break;

        case 1: // '\001'
            errorDesc = "VALIDATION_ERROR";
            break;

        case 2: // '\002'
            errorDesc = "DB_EXCEPTION";
            break;
        }
        s = s + "";
        s = s + "\nmessage = " + message;
        s = s + "\nsuccessful = " + successful;
        s = s + "\ntype = " + errorDesc;
        return s;
    }
}