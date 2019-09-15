package nix.localloop;

import nix.application.localloop.NIXApplicationLocalLoop;

import nix.application.localloop.NIXApplicationLocalLoopConditionBuilder;
import util.ModifiedSqlGenerator;

import java.util.List;
import java.util.stream.Collectors;

public class NIXLocalLoopDAO {
    public void insertLocalLoop(NIXLocalLoop nixLocalLoop) throws Exception{
        ModifiedSqlGenerator.insert(nixLocalLoop);
    }

    public List<NIXLocalLoop> getLocalLoopsByOfficeID(long officeId)throws Exception {
        List<NIXLocalLoop>nixLocalLoops = ModifiedSqlGenerator.getAllObjectList(NIXLocalLoop.class,
                new NIXLocalLoopConditionBuilder()
                    .Where()
                    .officeEquals(officeId)
                    .getCondition()
        );
        return nixLocalLoops;
    }

    public List<NIXLocalLoop> getLocalLoopsByConID(long connectionID)throws Exception {
        return  ModifiedSqlGenerator.getAllObjectList(NIXLocalLoop.class,new NIXLocalLoopConditionBuilder()
                                .Where()
                                .connectionEquals(connectionID)
                                .getCondition());

    }

    public List<NIXApplicationLocalLoop> getApplicationLocalLoopsByConID(long connectionID)throws Exception {
        List<NIXLocalLoop> nixLocalLoops = getLocalLoopsByConID(connectionID);
        List<NIXApplicationLocalLoop> nixApplicationLocalLoops =  ModifiedSqlGenerator.getAllObjectList(NIXApplicationLocalLoop.class,new NIXApplicationLocalLoopConditionBuilder()
                .Where()
                .idIn(
                        nixLocalLoops.stream()
                                .map(NIXLocalLoop::getApplicationLocalLoop)
                                .collect(Collectors.toList())
                )
                .getCondition());
        return nixApplicationLocalLoops;
    }

    public List<NIXLocalLoop> getLocalLoopByApplicationID(long id) throws Exception{
        return ModifiedSqlGenerator.getAllObjectList(NIXLocalLoop.class,new NIXLocalLoopConditionBuilder()
                        .Where()
                        .applicationLocalLoopEquals(id)
                        .getCondition());
    }
}
