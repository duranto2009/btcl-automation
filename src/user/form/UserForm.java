package user.form;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorActionForm;

public class UserForm extends ValidatorActionForm
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long m_userID;
    private String m_username;
    private String m_password;
    private String m_repassword;
    private long m_roleID;
    private long m_role;
    private long m_deleteIDs[];
    private String m_pageno;
    private String m_mailAddress;
    private String m_secToken;
    private String m_loginIPs;
    private String m_fullName;
    private String m_phoneNo;
    private String m_designation;
    private String m_additional;
    private int m_status;
    private long m_addedBy;
    private long m_addTime;
    private long m_lastModifiedBy;
    private long m_lastModifyTime;
    private String m_addedByName;
    private String m_addDateTime;
    private String m_lastModifiedByName;
    private String m_lastModifyDateTime;
    private String roleName;
    
    private Long districtID;
    private Long upazilaID;
    private Long unionID;
    private String departmentName;
    
    private double balance;
    private boolean isBTCLPersonnel;
    
    
    public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}
    
    
    public void setAddedByName(String m_addedByName) {
		this.m_addedByName = m_addedByName;
	}

	public void setAddDateTime(String m_addDateTime) {
		this.m_addDateTime = m_addDateTime;
	}

	public void setLastModifiedByName(String m_lastModifiedByName) {
		this.m_lastModifiedByName = m_lastModifiedByName;
	}

	public void setLastModifyDateTime(String m_lastModifyDateTime) {
		this.m_lastModifyDateTime = m_lastModifyDateTime;
	}

	public long getAddedBy() {
		return m_addedBy;
	}

	public void setAddedBy(long m_addedBy) {
		this.m_addedBy = m_addedBy;
	}

	public long getAddTime() {
		return m_addTime;
	}

	public void setAddTime(long m_addTime) {
		this.m_addTime = m_addTime;
	}

	public long getLastModifiedBy() {
		return m_lastModifiedBy;
	}

	public void setLastModifiedBy(long m_lastModifiedBy) {
		this.m_lastModifiedBy = m_lastModifiedBy;
	}

	public long getLastModifyTime() {
		return m_lastModifyTime;
	}

	public void setLastModifyTime(long m_lastModifyTime) {
		this.m_lastModifyTime = m_lastModifyTime;
	}

    
    public int getStatus()
    {
        return m_status;
    }

    public void setStatus(int p_status)
    {
        m_status = p_status;
    }


    public String getAdditionalInfo()
    {
        return m_additional;
    }

    public void setAdditionalInfo(String p_additional)
    {
        m_additional = p_additional;
    }

    public String getDesignation()
    {
        return m_designation;
    }

    public void setDesignation(String p_designation)
    {
        m_designation = p_designation;
    }

    public String getPhoneNo()
    {
        return m_phoneNo;
    }

    public void setPhoneNo(String p_phoneNo)
    {
        m_phoneNo = p_phoneNo;
    }

    public String getFullName()
    {
        return m_fullName;
    }

    public void setFullName(String p_fullName)
    {
        m_fullName = p_fullName;
    }

    public String getLoginIPs()
    {
        return m_loginIPs;
    }

    public void setLoginIPs(String p_loginIPs[])
    {
        String commaSeparatedIPs = "";
        for(int i=0;i<p_loginIPs.length ; i++) {
        	if(i==0) {
        		commaSeparatedIPs += p_loginIPs[i];
        	}else {
        		commaSeparatedIPs += "," +p_loginIPs[i];
        	}
        }
    	m_loginIPs = commaSeparatedIPs;
    }

    public String getMailAddress()
    {
        return m_mailAddress;
    }

    public void setMailAddress(String p_mailAddress)
    {
        m_mailAddress = p_mailAddress;
    }

    public String getSecToken()
    {
        return m_secToken;
    }

    public void setSecToken(String p_secToken)
    {
        m_secToken = p_secToken;
    }

    public long getUserID()
    {
        return m_userID;
    }

    public void setUserID(long p_userID)
    {
        m_userID = p_userID;
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

    public String getRepassword()
    {
        return m_repassword;
    }

    public void setRepassword(String p_repassword)
    {
        m_repassword = p_repassword;
    }

    public long getRoleID()
    {
        return m_roleID;
    }

    public void setRoleID(long p_roleID)
    {
        m_roleID = p_roleID;
    }

    public long getRole()
    {
        return m_role;
    }

    public void setRole(long p_role)
    {
        m_role = p_role;
    }

    public long[] getDeleteIDs()
    {
        return m_deleteIDs;
    }

    public void setDeleteIDs(long p_deleteIDs[])
    {
        m_deleteIDs = p_deleteIDs;
    }

    public String getPageno()
    {
        return m_pageno;
    }

    public void setPageno(String p_pageno)
    {
        m_pageno = p_pageno;
    }

    public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getDistrictID() {
		return districtID;
	}

	public void setDistrictID(Long districtID) {
		this.districtID = districtID;
	}

	public Long getUpazilaID() {
		return upazilaID;
	}

	public void setUpazilaID(Long upazilaID) {
		this.upazilaID = upazilaID;
	}

	public Long getUnionID() {
		return unionID;
	}

	public void setUnionID(Long unionID) {
		this.unionID = unionID;
	}
	

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

    public boolean getIsBTCLPersonnel() {
		return isBTCLPersonnel;
	}

	public void setIsBTCLPersonnel(boolean isBTCLPersonnel) {
		this.isBTCLPersonnel = isBTCLPersonnel;
	}

	public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest)
    {
    }

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
        ActionErrors errors = new ActionErrors();
       
        if(m_username!=null)
        {
	        if(m_username.equals(""))
			{
				errors.add("username", new ActionMessage("error.user.NoUsername"));
			}
			else if(m_username.contains("\"")||m_username.contains("'"))
			{
				errors.add("username", new ActionMessage("error.user.InvalidUsername"));
			}
			else if(m_username.length()>50)
			{
				errors.add("username", new ActionMessage("error.user.usernameLength"));
			}
        }
        if(m_password!=null)
        {
	     
	        
	        if(m_password.equals(""))
			{
				errors.add("password", new ActionMessage("error.user.NoPassword"));
			}
			else if(m_password.contains("\"")||m_password.contains("'"))
			{
				errors.add("password", new ActionMessage("error.user.InvalidPassword"));
			}
			else if(m_password.length()>50)
			{
				errors.add("password", new ActionMessage("error.user.passwordLength"));
			}
        }
        if(m_mailAddress != null)
        {
        
	        if(m_mailAddress.contains("\"")||m_mailAddress.contains("'"))
			{
				errors.add("mailAddress", new ActionMessage("error.user.InvalidMailAddress"));
			}
        }
        
		
        if(m_secToken != null)
        {
	        
			if(m_secToken.contains("\"")||m_secToken.contains("'"))
			{
				errors.add("secToken", new ActionMessage("error.user.InvalidSecToken"));
			}
			else if(m_secToken.length()>6)
			{
				errors.add("secToken", new ActionMessage("error.user.SecTokenLength"));
			}
        }
        if(m_fullName != null)
        {
	        if(m_fullName.contains("\"")||m_fullName.contains("'"))
			{
				errors.add("fullName", new ActionMessage("error.user.InvalidFullName"));
			}
	        else if(m_fullName.length()>50)
			{
				errors.add("fullName", new ActionMessage("error.user.FullNameLength"));
			}
        }
        
        if(m_designation != null)
        {
	        if(m_designation.contains("\"")||m_designation.contains("'"))
			{
				errors.add("designation", new ActionMessage("error.user.InvalidDesignation"));
			}
	        else if(m_designation.length()>50)
			{
				errors.add("designation", new ActionMessage("error.user.DesignationLength"));
			}
        }
        
        if(m_phoneNo != null)
        {
	        if(m_phoneNo.contains("\"")||m_phoneNo.contains("'"))
			{
				errors.add("phoneNo", new ActionMessage("error.user.InvalidPhoneNo"));
			}
	        else if(m_phoneNo.length()>20)
			{
				errors.add("phoneNo", new ActionMessage("error.user.PhoneNoLength"));
			}
        }
        if(m_additional != null)
        {
	        if(m_additional.contains("\"")||m_additional.contains("'"))
			{
				errors.add("additionalInfo", new ActionMessage("error.user.InvalidAdditionalInfo"));
			}
	        else if(m_additional.length()>1000)
			{
				errors.add("additionalInfo", new ActionMessage("error.user.AdditionalInfoLength"));
			}
        }
       
        return errors;
    }

	@Override
	public String toString() {
		return "UserForm [m_userID=" + m_userID + ", m_username=" + m_username + ", m_password=" + m_password
				+ ", m_repassword=" + m_repassword + ", m_roleID=" + m_roleID + ", m_role=" + m_role + ", m_deleteIDs="
				+ Arrays.toString(m_deleteIDs) + ", m_pageno=" + m_pageno + ", m_mailAddress=" + m_mailAddress
				+ ", m_secToken=" + m_secToken + ", m_loginIPs=" + m_loginIPs + ", m_fullName=" + m_fullName
				+ ", m_phoneNo=" + m_phoneNo + ", m_designation=" + m_designation + ", m_additional=" + m_additional
				+ ", m_status=" + m_status + ", m_addedBy=" + m_addedBy + ", m_addTime=" + m_addTime
				+ ", m_lastModifiedBy=" + m_lastModifiedBy + ", m_lastModifyTime=" + m_lastModifyTime
				+ ", m_addedByName=" + m_addedByName + ", m_addDateTime=" + m_addDateTime + ", m_lastModifiedByName="
				+ m_lastModifiedByName + ", m_lastModifyDateTime=" + m_lastModifyDateTime + ", roleName=" + roleName
				+ ", districtID=" + districtID + ", upazilaID=" + upazilaID + ", unionID=" + unionID
				+ ", departmentName=" + departmentName + ", balance=" + balance + ", isBTCLPersonnel=" + isBTCLPersonnel
				+ "]";
	}

	
}