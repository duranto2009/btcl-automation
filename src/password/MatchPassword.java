package password;

import java.util.HashMap;

public class MatchPassword
{
	public static final int password_length = 6;
    public static final int charlist_length = 26;
    public static final char char_list[] = {
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 
        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 
        'u', 'v', 'w', 'x', 'y', 'z'
    };
    HashMap enPassword;
    HashMap keyPassword;
    
    public MatchPassword()
    {
        enPassword = new HashMap();
        enPassword.put(new String("nM8.Zm4NHUsQ."), new Long(1));
        keyPassword = new HashMap();
        keyPassword.put(new String("nM"), new Long(1));
    }

    String getCandidate(int pos_variable[])
    {
        char password[] = {
            's', 's', 's', 's', 's', 's', '\0'
        };
        for(int i = 0; i < 6; i++)
        {
        	password[i] = char_list[pos_variable[i]];
        }

        String pass = new String(password);
        String enpass = jcrypt.crypt("nM", pass);
        Long value = (Long)enPassword.get(enpass);
        if(value != null)
        {
            long pIndex = value.longValue();
            //System.out.println("####################Password Found################");
            //System.out.println("password is" + pass);
        }
        return pass;
    }

    public static void main(String args[])
    {
        int pos_variable[] = {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };
        int cur_pos = 0;
        boolean permutation = true;
        MatchPassword np = new MatchPassword();
label0:
        do
        {
            if(permutation)
            {
                cur_pos = 0;
                pos_variable[cur_pos] = 0;
                for(int char_no = 0; char_no < 26; char_no++)
                {
                    np.getCandidate(pos_variable);
                    pos_variable[cur_pos]++;
                }

                cur_pos++;
                do
                {
                    if(pos_variable[cur_pos] < 25)
                    {
                        pos_variable[cur_pos]++;
                        continue label0;
                    }
                    pos_variable[cur_pos] = 0;
                    if(cur_pos == 6)
                    {
                        permutation = false;
                        continue label0;
                    }
                    cur_pos++;
                } while(true);
            }
            return;
        } while(true);
    }
}