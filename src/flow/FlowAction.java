package flow;

import annotation.ForwardedAction;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author maruf
 */
@ActionRequestMapping("flow")
public class FlowAction extends AnnotatedRequestMappingAction {

    private static final Logger LOG = LoggerFactory.getLogger(FlowAction.class);
    private final Gson gson = new Gson();

    @Service
    FlowService flowService;

    @RequestMapping(mapping = "/test/test", requestMethod = RequestMethod.GET)
    public JsonElement test(HttpServletRequest request, HttpServletResponse response) {
        return json(flowService.test(request, response));
    }

    @ForwardedAction
    @RequestMapping(mapping = "/admin", requestMethod = RequestMethod.GET)
    public String adminView() {
        return "admin";
    }

    @RequestMapping(mapping = "/api/modules", requestMethod = RequestMethod.GET)
    public JsonElement getModules() throws Exception {
        return json(flowService.getModuleIdNameMap());
    }

    @RequestMapping(mapping = "/api/roles", requestMethod = RequestMethod.GET)
    public JsonElement getRoles() throws Exception {
        return json(flowService.getRoleIdNameMap());
    }

//    @RequestMapping(mapping = "/api/flows", requestMethod = RequestMethod.GET)
//    public JsonElement getFlows(HttpServletRequest request) {
//        try {
//            return json(flowService.getFlowsByModule(Integer.parseInt(request.getParameter("module"))));
//        } catch (Exception ex) {
//            LOG.error(ex.toString(), ex);
//        }
//        return json(null);
//    }

    @RequestMapping(mapping = "/api/states", requestMethod = RequestMethod.GET)
    public JsonElement getStates(HttpServletRequest request) {
        try {
            String flowIdStr = request.getParameter("flow");
            if (flowIdStr != null)
                if (flowIdStr.length() > 0)
                    return json(flowService.getStatesByFlow(Integer.parseInt(flowIdStr)));
            return json(flowService.getStates());
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return json(null);
    }

    @RequestMapping(mapping = "/api/next-states", requestMethod = RequestMethod.GET)
    public JsonElement getNextStates(HttpServletRequest request) {
        try {
            return json(flowService.getTransitionsFrom(Integer.parseInt(request.getParameter("state"))));
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return json(null);
    }

    @RequestMapping(mapping = "/api/transitions", requestMethod = RequestMethod.GET)
    public JsonElement getAllTransitions() {
        try {
            return json(flowService.getAllTransitions());
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return json(null);
    }

    @RequestMapping(mapping = "/api/transition-roles", requestMethod = RequestMethod.GET)
    public JsonElement getTransitionRoles(HttpServletRequest request) {
        try {
            String transitionStr = request.getParameter("transition");
            if (transitionStr != null)
                if (transitionStr.length() > 0)
                    return json(flowService.getRolesByTransition(Integer.parseInt(transitionStr)));
            return json(flowService.getAllTransitionRoles());
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return json(null);
    }

    private JsonElement json(Object obj) {
        return gson.toJsonTree(obj);
    }
}
