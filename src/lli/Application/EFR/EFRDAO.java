package lli.Application.EFR;

import lli.connection.LLIConnectionConstants;
import util.ModifiedSqlGenerator;

import java.util.ArrayList;
import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class EFRDAO {


    Class<EFR> classObject = EFR.class;

    public List<EFR> getEFR(long apploacationID) throws Exception{

        if(apploacationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(apploacationID)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }

    public List<EFR> getIncompleteEFR(long applicationID,long vendorID) throws Exception{

        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .quotationStatusEquals((long) 0)
                                    .vendorIDEquals(vendorID)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }



    public void insertEFR(EFR efr) throws Exception{
        insert(efr);
    }

    public void updateEFRLLIApplication(EFR efr) throws Exception{
        ModifiedSqlGenerator.updateEntity(efr);
    }

    public List<EFR> getIncompleteEFRByAppID(long applicationId) throws Exception {
        if(applicationId>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationId)
                                    .quotationStatusEquals((long) LLIConnectionConstants.EFR_QUOTATION_INITIAL)
//
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }

    public List<EFR> getCompleteEFRByAppID(long applicationID,long userID) throws Exception {
        ArrayList<Long>quatuion=new ArrayList();

        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_GIVEN);
//        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_EXPIRED);
//        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_IGNORED);

        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .quotationStatusIn(quatuion)
                                    .vendorIDEquals(userID)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }


    public List<EFR> getCompleteEFRByAppID(long applicationID) throws Exception {
        ArrayList<Long>quatuion=new ArrayList();

        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_GIVEN);
        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_EXPIRED);
        quatuion.add(LLIConnectionConstants.EFR_QUOTATION_IGNORED);

        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .quotationStatusIn(quatuion)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }

    public List<EFR> getNotCompletedWOByVendor(long applicationID,long vendorID) throws Exception {


        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .workGivenEquals((long) 1)
                                    .vendorIDEquals(vendorID)
                                    .workCompletedEquals((long) 0)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }

    public List<EFR> getNotCompletedWO(long applicationID) throws Exception {


        if(applicationID>0){
            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .applicationIDEquals(applicationID)
                                    .workGivenEquals((long) 1)
                                    .workCompletedEquals((long) 0)
                                    .getCondition()
                    );

        }else{
            return ModifiedSqlGenerator.getAllObjectList(EFR.class);
        }

    }


    public List<EFR> getCompleteEFRByPopID(long popID,long applicationID,long officeId) throws Exception {


            return
                    ModifiedSqlGenerator.getAllObjectList(EFR.class,
                            new EFRConditionBuilder()
                                    .Where()
                                    .popIDEquals(popID)
                                    .applicationIDEquals(applicationID)
                                    .officeIDEquals(officeId)
                                    .workGivenEquals((long) 1)
                                    .getCondition()
                    );



    }

    public List<EFR> getSelectedEFRsByApplicationId(long applicationId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .applicationIDEquals(applicationId)
                .workGivenEquals(1L)
                .getCondition());
    }

    public List<EFR> efrByVendor(long userID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .vendorIDEquals(userID)
                .getCondition());
    }


    public List<EFR> efrQuationNotGivenByVendor(long userID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .vendorIDEquals(userID)
                .quotationStatusEquals((long) 0)
                .getCondition());
    }

    public List<EFR> efrWorkByVendor(long userID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .vendorIDEquals(userID)
                .workGivenEquals((long) 1)
                .workCompletedEquals((long) 0)
                .getCondition());
    }

    public List<EFR> efrByVendorandAppId(long appID,long userID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .vendorIDEquals(userID)
                .applicationIDEquals(appID)
                .getCondition());
    }


    public List<EFR> efrByVendorQuationNotCompletedandAppId(long appID,long userID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .vendorIDEquals(userID)
                .applicationIDEquals(appID)
                .quotationStatusEquals((long) 0)
                .getCondition());
    }


    public List<EFR> getAllEFRByApp(long applicationID) throws Exception {

        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .applicationIDEquals(applicationID)
                .getCondition());
    }

    public List<EFR> getAllEFRByAppAndVendor(long applicationID, long userID) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(EFR.class, new EFRConditionBuilder()
                .Where()
                .applicationIDEquals(applicationID)
                .vendorIDEquals(userID)
                .getCondition());
    }

    public List<EFR> getWorkCompleteEFRByPopID(long popID, long appID,long officeId) throws Exception {


        return
                ModifiedSqlGenerator.getAllObjectList(EFR.class,
                        new EFRConditionBuilder()
                                .Where()
                                .popIDEquals(popID)
                                .applicationIDEquals(appID)
                                .workGivenEquals((long) 1)
                                .workCompletedEquals((long) 1)
                                .loopDistanceIsApprovedEquals((long) 1)
                                .officeIDEquals(officeId)
                                .getCondition()
                );

    }

    public List<EFR> getHistoricalEFRByAppID(long applicationID) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(EFR.class,
                        new EFRConditionBuilder()
                                .Where()
                                .applicationIDEquals(applicationID)
                                .workGivenEquals((long) 1)
                                .workCompletedEquals((long) 1)
                                .getCondition()
                );
    }
}
