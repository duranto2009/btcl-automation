package file.upload;

import annotation.JsonPost;
import com.google.gson.JsonArray;
import lombok.extern.log4j.Log4j;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@ActionRequestMapping("file/")
@Log4j
public class FileAction extends AnnotatedRequestMappingAction {
    @Service
    FileFormService fileFormService;

    //LLI, NIX

    @RequestMapping(mapping = "download", requestMethod = RequestMethod.GET)
    public ActionForward download(HttpServletRequest request, HttpServletResponse response, @RequestParameter("id") long id) {

//        System.out.println("inside download");
        fileFormService.downloadFile(request, response);
        return null;
    }

    @RequestMapping(mapping = "delete", requestMethod = RequestMethod.GET)
    public ActionForward delete(HttpServletRequest request, HttpServletResponse response, @RequestParameter("id") long id) throws Exception {
        fileFormService.deleteFile(request, response);
        return null;
    }

    @RequestMapping(mapping = "upload", requestMethod = RequestMethod.POST)
    public JsonArray upload(ActionMapping action, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        return fileFormService.uploadFile(request, response);
    }

    @JsonPost
    @RequestMapping(mapping = "get-db-files", requestMethod = RequestMethod.POST)
    public List<FileForm> getDBFiles
            (
                    @RequestParameter(isJsonBody = true, value = "moduleId") long moduleId,
                    @RequestParameter(isJsonBody = true, value = "componentId") long componentId,
                    @RequestParameter(isJsonBody = true, value = "applicationId") long applicationId,
                    @RequestParameter(isJsonBody = true, value = "stateId") long stateId)

            throws Exception {
        System.out.println(moduleId + " " + componentId + " " + applicationId + " " + stateId);
        return fileFormService.getAllFiles(moduleId, componentId, applicationId, stateId);
    }

    //Upstream
    @JsonPost
    @RequestMapping(mapping = "get-db-files-by-applicationId", requestMethod = RequestMethod.POST)
    public List<FileForm> getDBFilesByApplicationId
    (@RequestParameter(isJsonBody = true, value = "applicationId") long applicationId)
            throws Exception {
        System.out.println(applicationId);
        return fileFormService.getAllFiles(applicationId);

    }


}
