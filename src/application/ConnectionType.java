package application;

public enum ConnectionType {
    REGULAR ("Regular"),
    SPECIAL( "Special Connection");


    private String connectionTypeName;
    ConnectionType(String name) {
        connectionTypeName = name;
    }

    public String getConnectionTypeName() { return this.connectionTypeName; }
}
