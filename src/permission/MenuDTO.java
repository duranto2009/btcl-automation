package permission;

import java.util.ArrayList;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;

@TableName("admenu")
public class MenuDTO {
	
	@ColumnName("mnMenuID")
	@PrimaryKey
	int menuID;
	
	@ColumnName("mnParentMenuID")
	int parentMenuID;
	
	@ColumnName("mnMenuName")	
	String menuName;
	
	@ColumnName("mnModuleTypeID")
	int moduleTypeID;

	int permissionType = -1;
	
    ArrayList<MenuDTO> childMenuList = new ArrayList<>();


	public int getMenuID() {
		return menuID;
	}


	public void setMenuID(int menuID) {
		this.menuID = menuID;
	}


	public int getParentMenuID() {
		return parentMenuID;
	}


	public void setParentMenuID(int parentMenuID) {
		this.parentMenuID = parentMenuID;
	}


	public String getMenuName() {
		return menuName;
	}


	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}


	public int getModuleTypeID() {
		return moduleTypeID;
	}


	public void setModuleTypeID(int moduleTypeID) {
		this.moduleTypeID = moduleTypeID;
	}

    public int getPermissionType() {
		return permissionType;
	}


	public void setPermissionType(int permissionType) {
		this.permissionType = permissionType;
	}


	


	public ArrayList<MenuDTO> getChildMenuList() {
		return childMenuList;
	}


	public void setChildMenuList(ArrayList<MenuDTO> childMenuList) {
		this.childMenuList = childMenuList;
	}



    public void addChildPermission(MenuDTO dto)
    {
        childMenuList.add(dto);
    }

    public void removeChildPermission(MenuDTO dto)
    {
        for(int i = 0; i < childMenuList.size(); i++)
        {
            MenuDTO x = childMenuList.get(i);
            if(x.getMenuID() == dto.getMenuID())
            {
                childMenuList.remove(i);
                return;
            }
        }

    }


	@Override
	public String toString() {
		return "MenuDTO [menuID=" + menuID + ", parentMenuID=" + parentMenuID + ", menuName=" + menuName
				+ ", moduleTypeID=" + moduleTypeID + ", permissionType=" + permissionType + ", childPermission="
				+ childMenuList + "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + menuID;
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
		MenuDTO other = (MenuDTO) obj;
		if (menuID != other.menuID)
			return false;
		return true;
	}


	
	

}
