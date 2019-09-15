package application;

public enum ApplicationType {
    VPN_NEW_CONNECTION ("New Link"),
    VPN_UPGRADE_CONNECTION ( "Upgrade Connection"),
    VPN_DOWNGRADE_CONNECTION ( "Downgrade Connection"),
    VPN_SHIFT_LINK ( "Shift Link"),
    VPN_TD("Temporary Disconnect"),
    VPN_RECONNECT("Reconnect"),
    VPN_CLOSE("Close"),
    VPN_OWNER_CHANGE("Owner Change"),
    INVALID_TYPE("INVALID TYPE"),
    ASN_APPLICATION("ASN Application"),

    UPSTREAM_NEW_REQUEST ("Upstream New request"),
    UPSTREAM_CONTRACT_EXTENSION_REQUEST("Upstream Contract extension request"),
    UPSTREAM_CONTRACT_CLOSE_REQUEST("Upstream Contract close request"),
    UPSTREAM_DOWNGRADE ("Upstream downgrade request"),
    UPSTREAM_UPGRADE ("Upstream upgrade request"),
    ;

    private String applicationTypeName;
    ApplicationType(String name) {
        applicationTypeName = name;
    }

    public String getApplicationTypeName() { return this.applicationTypeName; }
}
