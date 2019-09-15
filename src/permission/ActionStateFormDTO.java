package permission;

import org.apache.struts.action.ActionForm;

import annotation.ColumnName;
import annotation.PrimaryKey;
import annotation.TableName;
@TableName("at_action_form")
public class ActionStateFormDTO extends ActionForm{
	@PrimaryKey
	@ColumnName("afID")
	int ID;
	@ColumnName("afName")
	String name;
	@ColumnName("afHtml")
	String formHTML;
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFormHTML() {
		return formHTML;
	}
	public void setFormHTML(String formHTML) {
		this.formHTML = formHTML;
	}
	@Override
	public String toString() {
		return "ActionStateFormDTO [ID=" + ID + ", name=" + name
				+ ", formHTML=" + formHTML + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
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
		ActionStateFormDTO other = (ActionStateFormDTO) obj;
		if (ID != other.ID)
			return false;
		return true;
	}
}
