package lli;

public class LLIDropdownClient {
	long ID;
	String label;
	int registrantType;

	public int getRegistrantType() {
		return registrantType;
	}

	public void setRegistrantType(int registrantType) {
		this.registrantType = registrantType;
	}

	/**/
	public LLIDropdownClient(long l, String label,int registrantType) {
		this.ID = l;
		this.label = label;
		this.registrantType = registrantType;
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
