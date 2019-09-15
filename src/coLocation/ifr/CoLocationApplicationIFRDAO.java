package coLocation.ifr;

import coLocation.application.CoLocationApplicationDTOConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class CoLocationApplicationIFRDAO {
    Class<CoLocationApplicationIFRDTO> classObject = CoLocationApplicationIFRDTO.class;

    public CoLocationApplicationIFRDTO getIFR(long id) throws Exception {

        ArrayList<CoLocationApplicationIFRDTO>coLocationApplicationIFRDTOS=new ArrayList<>();
        if (id > 0) {
            coLocationApplicationIFRDTOS=
                    (ArrayList<CoLocationApplicationIFRDTO>) ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .idEquals(id)
                                    .getCondition()
                    );

        }

        if(coLocationApplicationIFRDTOS.size()>0){
            return coLocationApplicationIFRDTOS.get(0);
        }else {
            return null;
        }


    }


//    public List<IFR> getDistinctSelectedPOP(long appID) throws Exception{
//
//
//            return
//                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
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

    public List<CoLocationApplicationIFRDTO> getIFRByAppID(long appID, long state) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
//                                    .isSelectedEquals(0)
//                                    .stateEquals(state)
                                    //todo : check impact
//                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class);
        }

    }

    public List<CoLocationApplicationIFRDTO> getIFRByAppID(long appID) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                        new CoLocationApplicationIFRDTOConditionBuilder()
                                .Where()
                                .applicationIDEquals(appID)
//                                .isIgnoredEquals(0)
                                .getCondition()
                );


    }
    public List<CoLocationApplicationIFRDTO> getIFRByAppIDAndRole(long appID,long role) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                        new CoLocationApplicationIFRDTOConditionBuilder()
                                .Where()
                                .applicationIDEquals(appID)
                                .userRoleIdEquals((int) role)
//                                .isIgnoredEquals(0)
                                .getCondition()
                );


    }


    public List<CoLocationApplicationIFRDTO> getSelectedIFRByAppID(long appID, long state) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
//                                    .isSelectedEquals(1)
//                                    .stateEquals(state)
//                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class);
        }

    }

    public List<CoLocationApplicationIFRDTO> getSelectedIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
//                                    .isSelectedEquals(1)
//                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );

        } else {
            return null;
        }

    }

    public List<CoLocationApplicationIFRDTO> getIncompleteIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
                                    .isReplied(true)
                                    .isSelected(true)
                                    .isCompleted(false)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class);
        }

    }

    public List<CoLocationApplicationIFRDTO> getNotRepliedIFRByAppID(long appID) throws Exception {

        if (appID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class,
                            new CoLocationApplicationIFRDTOConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(appID)
                                    .isReplied(false)
                                    .isCompleted(false)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(CoLocationApplicationIFRDTO.class);
        }

    }


    public void insertIFR(CoLocationApplicationIFRDTO ifr) throws Exception {
        insert(ifr);
    }

    public void updateIFRCoLocationApplication(CoLocationApplicationIFRDTO ifr) throws Exception {
        ModifiedSqlGenerator.updateEntity(ifr);
    }
}
