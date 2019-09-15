package common.action;

import annotation.ForwardedAction;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
@ActionRequestMapping("configuration/")
public class ConfigurationAction extends AnnotatedRequestMappingAction {

    @ForwardedAction
    @RequestMapping(mapping = "view", requestMethod = RequestMethod.GET)
    private String getConfigurationPage() { return "configuration-page"; }
}
