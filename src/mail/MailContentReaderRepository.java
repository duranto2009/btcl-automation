package mail;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import databasemanager.DatabaseManager;



public class MailContentReaderRepository
{

	Logger logger = Logger.getLogger(MailContentReaderRepository.class);
	static MailContentReaderRepository mailContentReader = null;
	private String t_Name = "vbAlertMailContent";
	HashMap<Long, MailContentDTO> mailContentByID;
	private MailContentReaderRepository()
	{
		mailContentByID=new HashMap<Long, MailContentDTO>();
		reload();
		
	}

	public static MailContentReaderRepository getInstance()
	{
		if(mailContentReader == null)
		{
			createClientBalanceRepository();
		}
		return mailContentReader;
	}

	private synchronized static void createClientBalanceRepository()
	{
		if(mailContentReader == null)
		{
			mailContentReader = new MailContentReaderRepository();
			
		}
	}
	public String getTableName()
	{
		
		return t_Name;
	}

	public synchronized void reload()
	{
		
		 String sql=" select amcID,amcMailSubject,amcMailContent   from vbAlertMailContent ";
		
		 Connection cn=null;
		 Statement stmt=null;
		 try
		 {
			 cn=DatabaseManager.getInstance().getConnection();
			 stmt=cn.createStatement();
			 ResultSet rs=stmt.executeQuery(sql);
			 while(rs.next())
			 {
				 long id=rs.getLong(1);
				 MailContentDTO dto=new MailContentDTO();
				 dto.mailSubject=rs.getString(2);
				dto.mailContent=rs.getString(3);
				
				mailContentByID.put(id,dto);
				 
			 }
			 rs.close();
			 
		 }
		 catch(Exception ee)
		 {
			 logger.fatal("Exception at loading Balance Alert  Repository",ee);
			 
		 }
		 finally
		 {
			 if(stmt!=null)
			 {
				 try{stmt.close();}catch(Exception ee){}
			 }
			 if(cn!=null)
			 {
				 try
				 {
					 DatabaseManager.getInstance().freeConnection(cn);
				 }catch(Exception ee)
				 {}
			 }
		 }
		
	}
	
	public synchronized MailContentDTO getMailContentByAlertID(long id)
	{
		return mailContentByID.get(id);
	}

}
