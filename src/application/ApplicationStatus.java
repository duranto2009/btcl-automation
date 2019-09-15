package application;

public enum ApplicationStatus {
    INACTIVE(0,"Inactive"),
    ACTIVE(1,"Active"),
    TD(2,"Temporary Disconnect"),
    CLOSED(3,"Closed");

    private int status;
    private String name;
    ApplicationStatus(int status, String name) {
        this.status = status;
        this.name = name;
    }

    public int getStatus() { return  this.status; }
    public String getName(){
        return name;
    }
}
