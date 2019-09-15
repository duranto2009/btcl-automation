package nix.common;

public class PortDetails {

    String portType;
    String portCount;
    String portCost;

    public PortDetails(String portType, String portCount, String portCost) {
        this.portType = portType;
        this.portCount = portCount;
        this.portCost = portCost;
    }

    public PortDetails(){

    }
}
