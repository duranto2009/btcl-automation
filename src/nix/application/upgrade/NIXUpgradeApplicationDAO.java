package nix.application.upgrade;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NIXUpgradeApplicationDAO {
    public void insert(NIXUpgradeApplication nixUpgradeApplication) throws Exception{
        ModifiedSqlGenerator.insert(nixUpgradeApplication);
    }

    public List<NIXUpgradeApplication> getApplicationByParent(long id) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(NIXUpgradeApplication.class, new NIXUpgradeApplicationConditionBuilder().
        Where()
        .parentEquals(id)
        .getCondition()
        );


    }
}
