package permission;

import java.util.ArrayList;

import annotation.ColumnName;
import annotation.CurrentTime;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("adpermission")
public class PermissionDTO
{
	@ColumnName("prPermissionID")
	@PrimaryKey
	long ID;
	
	@ColumnName("prMenuID")
	int menuID;

    
    @ColumnName("prType")
    int permissionType;
    
    int moduleTypeID;
    
    @ColumnName("prIsDeleted")
    boolean isDeleted;
    
    @CurrentTime
    @ColumnName("prLastModificationTime")
    long lastModificationTime;
    
    @ColumnName(value = "prRoleID",editable = false)
    long roleID;
    
    
    public PermissionDTO()
    {
        menuID = -1;
        roleID = -1;
        permissionType = -1;
    }

    

    public PermissionDTO(int menuID, long roleID, int permissionType)
    {
        menuID = menuID;
        this.roleID = roleID;
        permissionType = permissionType;
    }

    public PermissionDTO(int menuID, int permissionType)
    {
        menuID = menuID;
        permissionType = permissionType;
    }

    public void setMenuID(int id)
    {
        menuID = id;
    }

    public int getMenuID()
    {
        return menuID;
    }

    public void setPermissionType(int type)
    {
        permissionType = type;
    }

    public int getPermissionType()
    {
        return permissionType;
    }

	public int getModuleTypeID() {
		return moduleTypeID;
	}

	public void setModuleTypeID(int moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
	}



	public long getID() {
		return ID;
	}



	public void setID(long iD) {
		ID = iD;
	}



	public boolean isDeleted() {
		return isDeleted;
	}



	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}



	public long getLastModificationTime() {
		return lastModificationTime;
	}



	public void setLastModificationTime(long lastModificationTime) {
		this.lastModificationTime = lastModificationTime;
	}



	public long getRoleID() {
		return roleID;
	}



	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}



	@Override
	public String toString() {
		return "PermissionDTO [ID=" + ID + ", menuID=" + menuID + ", permissionType=" + permissionType
				+ ", moduleTypeID=" + moduleTypeID + ", isDeleted=" + isDeleted + ", lastModificationTime="
				+ lastModificationTime + ", roleID=" + roleID + "]";
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (ID ^ (ID >>> 32));
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
		PermissionDTO other = (PermissionDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}




    
    
}