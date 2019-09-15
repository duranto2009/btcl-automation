package nix.application.downgrade;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NIXDowngradeApplicationDAO {

    public void insert(NIXDowngradeApplication nixDowngradeApplication)throws Exception {
        ModifiedSqlGenerator.insert(nixDowngradeApplication);
    }

    List<NIXDowngradeApplication> getApplicationByParent(long id) throws Exception{

        return ModifiedSqlGenerator.getAllObjectList(NIXDowngradeApplication.class,
                new NIXDowngradeApplicationConditionBuilder().
                    Where()
                    .parentEquals(id)
                    .getCondition()
                );
    }

}
