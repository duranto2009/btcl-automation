package nix.efr;

import annotation.Transactional;
import nix.constants.NIXConstants;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

public class NIXEFRDAO {

    public List<NIXEFR> getNotCompletedWO(long applicationID) throws Exception {
        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationID)
                                    .workGivenEquals(1)
                                    .workCompletedEquals((long) 0)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }

    }
    public List<NIXEFR> getNotCompletedWOByVendor(long applicationID, long vendorID) throws Exception {


        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationID)
                                    .workGivenEquals(1)
                                    .vendorEquals(vendorID)
                                    .workCompletedEquals((long) 0)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }

    }


    @Transactional
    public List<NIXEFR> getCompleteEFRByAppID(long applicationID,long userID) throws Exception {
        ArrayList<Integer> quatuion = new ArrayList();
        quatuion.add(NIXConstants.EFR_QUOTATION_GIVEN);
        quatuion.add(NIXConstants.EFR_QUOTATION_EXPIRED);
        quatuion.add(NIXConstants.EFR_QUOTATION_IGNORED);
        if (applicationID > 0) {
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationID)
                                    .quotationStatusIn(quatuion)
                                    .vendorEquals(userID)
                                    .getCondition()
                    );

        } else {
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }
    }

    @Transactional
    public List<NIXEFR> getCompleteEFRByAppID(long applicationID) throws Exception{

        ArrayList<Integer> quatuion=new ArrayList();
        quatuion.add(NIXConstants.EFR_QUOTATION_GIVEN);
        quatuion.add(NIXConstants.EFR_QUOTATION_EXPIRED);
        quatuion.add(NIXConstants.EFR_QUOTATION_IGNORED);

        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationID)
                                    .quotationStatusIn(quatuion)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }
    }

    public List<NIXEFR> getIncompleteEFR(long applicationId, long vendorID)throws Exception {


        if(applicationId>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationId)
                                    .quotationStatusEquals(NIXConstants.EFR_QUOTATION_PENDING)
                                    .vendorEquals(vendorID)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }
    }

    public void insertApplication(NIXEFR efr)throws Exception {
        ModifiedSqlGenerator.insert(efr);
    }

    public List<NIXEFR> efrByVendor(long userID) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class, new NIXEFRConditionBuilder()
                .Where()
                .vendorEquals(userID)
                .getCondition());
    }

    public List<NIXEFR> efrByVendorandAppId(long appId, long userId) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class, new NIXEFRConditionBuilder()
                .Where()
                .vendorEquals(userId)
                .applicationEquals(appId)
                .getCondition());
    }

    public void updateApplication(NIXEFR efr) throws Exception{
        ModifiedSqlGenerator.updateEntity(efr);
    }

    public List<NIXEFR> getIncomleteEFRByAppID(long applicationId)throws Exception {
        if(applicationId>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                            new NIXEFRConditionBuilder()
                                    .Where()
                                    .applicationEquals(applicationId)
                                    .quotationStatusEquals(NIXConstants.EFR_QUOTATION_PENDING)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class);
        }

    }

    public List<NIXEFR> getCompleteEFRByPopID(long popId, long appID)throws Exception {
        return  ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                        new NIXEFRConditionBuilder()
                                .Where()
                                .popEquals(popId)
                                .applicationEquals(appID)
                                .workGivenEquals( 1)
                                .getCondition()
                );
    }


    public List<NIXEFR> getCompleteEFRByPopID(long popID,long applicationID,long officeId) throws Exception {


        return
                ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                        new NIXEFRConditionBuilder()
                                .Where()
                                .popEquals(popID)
                                .applicationEquals(applicationID)
                                .officeEquals(officeId)
                                .workGivenEquals(1)
                                .getCondition()
                );



    }

    public List<NIXEFR> getSelectedEFRsByApplicationId(long applicationId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class, new NIXEFRConditionBuilder()
                .Where()
                .applicationEquals(applicationId)
                .workGivenEquals( 1)
                .getCondition());
    }


    public List<NIXEFR> getWorkCompleteEFRByPopID(long popID, long appID) throws Exception {


        return
                ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                        new NIXEFRConditionBuilder()
                                .Where()
                                .popEquals(popID)
                                .applicationEquals(appID)
                                .workGivenEquals(1)
                                .workCompletedEquals((long) 1)
                               // .approvedDistanceEquals((long) 1)
                                .getCondition()
                );

    }

    public List<NIXEFR> getAllEFRByApp(long applicationID)throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class, new NIXEFRConditionBuilder()
                .Where()
                .applicationEquals(applicationID)
                .getCondition());
    }

    public List<NIXEFR> getAllEFRByAppAndVendor(long id, long userID) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(NIXEFR.class, new NIXEFRConditionBuilder()
                .Where()
                .applicationEquals(id)
                .vendorEquals(userID)
                .getCondition());
    }

    public List<NIXEFR> getHistoricalEFRByAppID(long id)throws Exception {
        return  ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                        new NIXEFRConditionBuilder()
                                .Where()
                                .applicationEquals(id)
                                .workGivenEquals(1)
                                .workCompletedEquals((long)1)
                                .getCondition()
                );
    }

    List<NIXEFR> getCompletedEFRByLoop(long loopId, long appID)throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                        new NIXEFRConditionBuilder()
                                .Where()
                                .loopIdEquals(loopId)
                                .applicationEquals(appID)
                                .workGivenEquals(1)
                                .getCondition()
                );
    }

    public List<NIXEFR> getWorkCompletedEFRByLocalLoop(long loopId, long applicationID)throws Exception {
        return
            ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,
                    new NIXEFRConditionBuilder()
                            .Where()
                            .loopIdEquals(loopId)
                            .applicationEquals(applicationID)
                            .workGivenEquals(1)
                            .workCompletedEquals((long) 1)
                            // .approvedDistanceEquals((long) 1)
                            .getCondition()
            );

    }

    public NIXEFR getCompletedEFRByLocalLoopID(long loopId) throws Exception{
        List<NIXEFR>nixefrs = ModifiedSqlGenerator.getAllObjectList(NIXEFR.class,new NIXEFRConditionBuilder()
                .Where()
                .loopIdEquals(loopId)
                .workGivenEquals(1)
                .workCompletedEquals((long)1)
                .getCondition());
        return nixefrs.stream().findFirst().orElse(null);
    }
}
