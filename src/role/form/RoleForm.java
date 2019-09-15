package role.form;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorActionForm;

public class RoleForm extends ValidatorActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleName;
	private String roleDescription;
	private long roleID;
	private long m_deleteIDs[];
	private String m_pageno;
	private boolean restrictedToOwn;
	private int maxClientPerDay;
	private Long parentRoleID;

	public int getMaxClientPerDay() {
		return maxClientPerDay;
	}

	public void setMaxClientPerDay(int m_maxClientPerDay) {
		this.maxClientPerDay = m_maxClientPerDay;
	}

	public boolean getRestrictedtoOwn() {
		return restrictedToOwn;
	}

	public void setRestrictedtoOwn(boolean restrictedtoOwn) {
		this.restrictedToOwn = restrictedtoOwn;
	}

	public String getRoleDesc() {
		return roleDescription;
	}

	public void setRoleDesc(String desc) {
		roleDescription = desc;
	}

	public String getRolename() {
		return roleName;
	}

	public void setRolename(String p_roleName) {
		roleName = p_roleName;
	}

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long p_roleID) {
		roleID = p_roleID;
	}

	public long[] getDeleteIDs() {
		return m_deleteIDs;
	}

	public void setDeleteIDs(long p_deleteIDs[]) {
		m_deleteIDs = p_deleteIDs;
	}

	public String getPageno() {
		return m_pageno;
	}

	public void setPageno(String p_pageno) {
		m_pageno = p_pageno;
	}

	public void reset(ActionMapping actionmapping, HttpServletRequest httpservletrequest) {
	}

	public Long getParentRoleID() {
		return parentRoleID;
	}

	public void setParentRoleID(Long parentRoleID) {
		this.parentRoleID = parentRoleID;
	}

	@Override
	public String toString() {
		return "RoleForm [roleName=" + roleName + ", roleDescription=" + roleDescription + ", roleID=" + roleID
				+ ", m_deleteIDs=" + Arrays.toString(m_deleteIDs) + ", m_pageno=" + m_pageno + ", restrictedToOwn="
				+ restrictedToOwn + ", maxClientPerDay=" + maxClientPerDay + ", parentRoleID=" + parentRoleID + "]";
	}

}