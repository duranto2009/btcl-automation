package login;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public class LoginForm extends ActionForm
{
	private String m_username;
    private String m_password;
    
    public LoginForm()
    {
    }

    public String getUsername()
    {
        return m_username;
    }

    public void setUsername(String p_username)
    {
        m_username = p_username;
    }

    public String getPassword()
    {
        return m_password;
    }

    public void setPassword(String p_password)
    {
        m_password = p_password;
    }

    public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest)
    {
    }

    public ActionErrors validate(ActionMapping p_mapping, HttpServletRequest p_request)
    {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

    
}