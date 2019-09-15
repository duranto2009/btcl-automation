package lli;

public class LLIActionButton {
	String title;
	String url;
	boolean isForward;
	
	public LLIActionButton(String title, String url, boolean isForward) {
		this.title = title;
		this.url = url;
		this.isForward = isForward;
	}
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}


	public boolean isForward() {
		return isForward;
	}


	public void setForward(boolean isForward) {
		this.isForward = isForward;
	}
	
	
}
