package dashboard;

import annotation.ForwardedAction;
import com.google.gson.JsonObject;
import login.LoginDTO;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.KeyValuePair;

import java.util.List;

@ActionRequestMapping("dashboard")
public class DashboardAction extends AnnotatedRequestMappingAction {

    @Service DashboardEngine dashboardEngine;

    @ForwardedAction
    @RequestMapping(mapping = "", requestMethod = RequestMethod.GET)
    private String getDashboardPage() {
        return "dashboard-page";
    }

    //API

    @RequestMapping(mapping= "/get-data", requestMethod = RequestMethod.GET)
    public KeyValuePair<List<DashboardCounterCard>, List<DashboardMainFrame>> getDashboardData(LoginDTO loginDTO) throws Exception {
        return dashboardEngine.getData(loginDTO);
    }

}
