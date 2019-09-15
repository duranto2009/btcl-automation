package file.upload;

import annotation.JsonPost;
import com.google.gson.JsonArray;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import util.SOP;
import util.ServiceDAOFactory;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final int FILE_REQUEST_TYPE_UPLOAD = 1;
    private static final int FILE_REQUEST_TYPE_DOWNLOAD = 2;
    private static final int FILE_REQUEST_TYPE_REMOVE = 3;

    FileFormService fileFormService = ServiceDAOFactory.getService(FileFormService.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {

        if (fileFormService.isValidGetRequest(request)) {
            executeFileManagement(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            fileFormService.uploadFile(request, response);
        } catch (Exception e) {

        }
    }

    private void executeFileManagement(HttpServletRequest request, HttpServletResponse response) {
        int requestType = Integer.parseInt(request.getParameter("requestType"));
        switch (requestType) {
            case FILE_REQUEST_TYPE_UPLOAD:
                break;
            case FILE_REQUEST_TYPE_DOWNLOAD:
                fileFormService.downloadFile(request, response);
                break;
            case FILE_REQUEST_TYPE_REMOVE:
                break;
        }
    }
}
