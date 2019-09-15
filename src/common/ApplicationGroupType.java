package common;

import java.util.Arrays;

public enum ApplicationGroupType {
    LLI_CONNECTION_APPLICATION (0),
    LLI_CLIENT_REVISION_APPLICATION (1),
    COLOCATION_CONNECTION_APPLICATION (2),
    COLOCATION_CLIENT_REVISION_APPLICATION (3),
    NIX_CONNECTION_APPLICATION (4),
    NIX_CLIENT_APPLICATION (5),
    VPN_LINK_APPLICATION(6),
    VPN_CLIENT_APPLICATION(7),
    VPN_LINK_CLIENT_APPLICATION(8)
    ;

    private int type;

    ApplicationGroupType(int type) {
        this.type = type;
    }

    public static ApplicationGroupType getAppGroupTypeByOrdinal(int type) {
        return Arrays.stream(ApplicationGroupType.values())
                .filter(t->t.ordinal() == type)
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Application Group Type Found with type " + type));
    }

    public static int getAppGroupTypeByName(String appGroupType) {
        ApplicationGroupType applicationGroupType = ApplicationGroupType.valueOf(appGroupType);
        return applicationGroupType.ordinal();
    }

}
