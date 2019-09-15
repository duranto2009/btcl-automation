package nix.ifr;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class NIXIFRDAO {

    public List<NIXIFR> getIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXIFR.class,
                            new NIXIFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(appID)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(NIXIFR.class);
        }

    }

    public List<NIXIFR> getSelectedIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXIFR.class,
                            new NIXIFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(appID)
                                    .selectedEquals(1)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(NIXIFR.class);
        }

    }

    public void insertIFR(NIXIFR ifr)throws Exception {
        insert(ifr);
    }

    public void updateIFRApplication(NIXIFR ifr) throws Exception{
        ModifiedSqlGenerator.updateEntity(ifr);
    }
}
