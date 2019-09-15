package lli.Application.IFR;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class IFRDAO {

    Class<IFR> classObject = IFR.class;

    public List<IFR> getIFR(long id) throws Exception {

        if (id > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(IFR.class,
                            new IFRConditionBuilder()
                                    .Where()
                                    .idEquals(id)
                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(IFR.class);
        }


    }


//    public List<IFR> getDistinctSelectedPOP(long appID) throws Exception{
//
//
//            return
//                    ModifiedSqlGenerator.getAllObjectList(IFR.class,
//                            new IFRConditionBuilder()
//                                    .Where()
//                                    .PopID
//                                    .idEquals(appID)
//                                    .isSelectedEquals(1)
//                                    .getCondition()
//                    );
//
//
//
//    }

    public List<IFR> getIFRByAppID(long appID, long state) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(IFR.class,
                            new IFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
//                                    .isSelectedEquals(0)
//                                    .stateEquals(state)
                                    //todo : check impact
                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(IFR.class);
        }

    }

    public List<IFR> getIFRByAppID(long appID) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(IFR.class,
                        new IFRConditionBuilder()
                                .Where()
                                .applicationIDEquals(appID)
                                .isIgnoredEquals(0)
                                .getCondition()
                );


    }


    public List<IFR> getSelectedIFRByAppID(long appID, long state) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(IFR.class,
                            new IFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
                                    .isSelectedEquals(1)
                                    .stateEquals(state)
                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(IFR.class);
        }

    }

    public List<IFR> getSelectedIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(IFR.class,
                            new IFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
                                    .isSelectedEquals(1)
                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(IFR.class);
        }

    }


    public void insertIFR(IFR ifr) throws Exception {
        insert(ifr);
    }

    public void updateIFRLLIApplication(IFR ifr) throws Exception {
        ModifiedSqlGenerator.updateEntity(ifr);
    }


}
