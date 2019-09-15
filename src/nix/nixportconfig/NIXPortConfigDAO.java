package nix.nixportconfig;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NIXPortConfigDAO {
    public List<NIXPortConfig> getPortConfigByPortType(int portType)throws Exception {

        List<NIXPortConfig> nixPortConfigList = ModifiedSqlGenerator.
                getAllObjectList(NIXPortConfig.class,new NIXPortConfigConditionBuilder()
                            .Where()
                            .portTypeEquals(portType)
                            .getCondition());
        return nixPortConfigList;
    }
}
