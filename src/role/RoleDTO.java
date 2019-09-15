package role;

import java.util.ArrayList;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;
import permission.ActionPermissionDTO;
import permission.ColumnPermissionDTO;
import permission.PermissionDTO;

@TableName("adrole")
public class RoleDTO{
	
    @ColumnName("rlRoleID")
    @PrimaryKey
    private long roleID;
	
	@ColumnName("rlRoleName")
	private String roleName;
	
	@ColumnName("rlRoleDesc")
    private String roleDescription;

	@ColumnName("rlParentRoleID")
	private Long parentRoleID;
    
    private ArrayList<PermissionDTO> permissionList;
    private ArrayList<ColumnPermissionDTO> columnPermissionList;
    private boolean restrictedtoOwn;   
    private int maxClientPerDay;
    private ArrayList<ActionPermissionDTO> actionPermissionList;
    
    @CurrentTime
    @ColumnName("rlLastModificationTime")
    long lastModificationTime;
    
    public boolean hasParent() {
		return parentRoleID != null;
	}
    
    @ColumnName("rlIsDeleted")
    boolean isDeleted;
//    private int moduleTypeID;

	public boolean getRestrictedtoOwn() {
		return restrictedtoOwn;
	}

	public void setRestrictedtoOwn(boolean restrictedtoOwn) {
		this.restrictedtoOwn = restrictedtoOwn;
	}

	public int getMaxClientPerDay() {
		return maxClientPerDay;
	}

	public void setMaxClientPerDay(int maxClientPerDay) {
		this.maxClientPerDay = maxClientPerDay;
	}

    public ArrayList<ColumnPermissionDTO> getColumnPermissionList() {
        return columnPermissionList;
    }
    public long getRoleID(){
        return roleID;
    }

    public void setRoleID(long p_roleID){
        roleID = p_roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String p_roleName) {
        roleName = p_roleName;
    }

    public String getRoleDescription()  {
        return roleDescription;
    }

    public void setRoleDescription(String desc) {
        roleDescription = desc;
    }

    public void setPermissionList(ArrayList<PermissionDTO> permissionList) {
        this.permissionList = permissionList;
    }

    public ArrayList<PermissionDTO> getPermissionList() {
        return permissionList;
    }

	public long getLastModificationTime() {
		return lastModificationTime;
	}

	public ArrayList<ActionPermissionDTO> getActionPermissionList() {
		return actionPermissionList;
	}

	public void setActionPermissionList(ArrayList<ActionPermissionDTO> actionPermissionList) {
		this.actionPermissionList = actionPermissionList;
	}

	public void setColumnPermissionList(ArrayList<ColumnPermissionDTO> columnPermissionList) {
		this.columnPermissionList = columnPermissionList;
	}

	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getParentRoleID() {
		return parentRoleID;
	}

	public void setParentRoleID(Long parentRoleID) {
		this.parentRoleID = parentRoleID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (roleID ^ (roleID >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleDTO other = (RoleDTO) obj;
		if (roleID != other.roleID)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RoleDTO [roleID=" + roleID + ", roleName=" + roleName + ", roleDescription=" + roleDescription
				+ ", parentRoleID=" + parentRoleID + ", permissionList=" + permissionList + ", columnPermissionList="
				+ columnPermissionList + ", restrictedtoOwn=" + restrictedtoOwn + ", maxClientPerDay=" + maxClientPerDay
				+ ", actionPermissionList=" + actionPermissionList + ", lastModificationTime=" + lastModificationTime
				+ ", isDeleted=" + isDeleted + "]";
	}


	
}