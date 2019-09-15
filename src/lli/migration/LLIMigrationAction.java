package lli.migration;


import annotation.ForwardedAction;
import annotation.JsonPost;
import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lombok.extern.log4j.Log4j;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.List;

@ActionRequestMapping("lli-migration/")
@Log4j
public class LLIMigrationAction extends AnnotatedRequestMappingAction {

    @Service
    private GlobalService globalService;

    @Service
    private LLIMigrationService lliMigrationService;

    @ForwardedAction
    @RequestMapping(mapping = "get-connection-add-panel", requestMethod = RequestMethod.GET)
    public String getConnectionAddPanel(){
        return "add-new-connection";
    }

    @JsonPost
    @RequestMapping(mapping = "add-new-connection", requestMethod = RequestMethod.POST)
    public void addNewConnection(
            @RequestParameter(isJsonBody = true, value = "lliConnections") List<LLIConnection> lliConnections){

        lliConnections
                .forEach(t-> {
                    try {
                        lliMigrationService.addNewConnection(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

}
