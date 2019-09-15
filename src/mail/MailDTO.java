package mail;

public class MailDTO
{
	 public String toList;
	 public String msgText; 
	 public String mailSubject;
	 public boolean isHtmlMail;
	 public String ccList;
	 public String attachmentPath;
	 public MailDTO()
	 {
		 
	 }
	public MailDTO(String p_list, String p_Text)
	{
		toList=p_list;
		msgText=p_Text;
	}
	@Override
	public String toString() {
		return "MailDTO [toList=" + toList + ", msgText=" + msgText
				+ ", mailSubject=" + mailSubject + ", isHtmlMail=" + isHtmlMail
				+ ", ccList=" + ccList + ", attachmentPath=" + attachmentPath
				+ "]";
	}
	

}
