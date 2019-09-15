package lli;

public class LLIDropdownPair {
	long ID;
	String label;
	Object object;

	/**/
	public LLIDropdownPair(long l, String label) {
		this.ID = l;
		this.label = label;
	}
	public LLIDropdownPair(long l, String label,Object object) {
		this.ID = l;
		this.label = label;
		this.object = object;
	}
	
	/**/
	public long getID() {
		return ID;
	}
	public void setID(long iD) {
		ID = iD;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}
