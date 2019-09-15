package login;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public class PasswordMailForm extends ActionForm
{
	private String m_username;
    private String m_mailAddress;
    private String m_secretQuestion;
    private String m_secretAnswer;
    private int reqType;
    
    public String getSecretQuestion()
    {
        return m_secretQuestion;
    }

    public void setSecretQuestion(String p_secretQuestion)
    {
        m_secretQuestion = p_secretQuestion;
    }

    public String getSecretAnswer()
    {
        return m_secretAnswer;
    }

    public void setSecretAnswer(String p_secretAnswer)
    {
        m_secretAnswer = p_secretAnswer;
    }

    public PasswordMailForm()
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

    public String getMailAddress()
    {
        return m_mailAddress;
    }

    public void setMailAddress(String p_mailAddress)
    {
        m_mailAddress = p_mailAddress;
    }

    public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest)
    {
    }

    public ActionErrors validate(ActionMapping p_mapping, HttpServletRequest p_request)
    {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

	public int getReqType() {
		return reqType;
	}

	public void setReqType(int reqType) {
		this.reqType = reqType;
	}

    
}