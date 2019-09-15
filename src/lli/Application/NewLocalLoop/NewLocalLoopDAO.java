package lli.Application.NewLocalLoop;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class NewLocalLoopDAO {

    Class<NewLocalLoop> classObject = NewLocalLoop.class;





    public void insertlocalLoop(NewLocalLoop NewlocalLoop) throws Exception{
        insert(NewlocalLoop);
    }

    public void update(NewLocalLoop NewlocalLoop) throws Exception{
        ModifiedSqlGenerator.updateEntity(NewlocalLoop);
    }


    public List<NewLocalLoop> getLocalLoop(long applicationID) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(NewLocalLoop.class,
                        new NewLocalLoopConditionBuilder()
                                .Where()
                                .applicationIDEquals(applicationID)
                                .getCondition()
                );
    }



    public List<NewLocalLoop> getLocalLoopByOffice(long officeID) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(NewLocalLoop.class,
                        new NewLocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(officeID)
                                .getCondition()
                );
    }

    public List<NewLocalLoop> getLocalLoopByOfficeAndPop(long officeID, long popId) throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(NewLocalLoop.class,
                        new NewLocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(officeID)
                                .popIDEquals(popId)
                                .getCondition()
                );
    }


    public List<NewLocalLoop> getLocalLoopByOfficeAndApplication(long officeID, long applicationId)throws Exception {
        return
                ModifiedSqlGenerator.getAllObjectList(NewLocalLoop.class,
                        new NewLocalLoopConditionBuilder()
                                .Where()
                                .officeIDEquals(officeID)
                                .applicationIDEquals(applicationId)
                                .getCondition()
                );
    }
}
