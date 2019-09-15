
/**
 * @author Touhid
 */

package file.upload;

import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import file.FileTypeConstants;
import file.FileUploadUtils;
import global.GlobalService;
import login.LoginDTO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import requestMapping.Service;
import sessionmanager.SessionConstants;
import util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileFormService {

    FileFormDAO fileFormDAO = new FileFormDAO();

    @Service
    GlobalService globalService;

    private boolean doesValidRequestTypeExist(HttpServletRequest request) {
        if (request.getParameterMap().containsKey("requestType") && StringUtils.isNumeric(request.getParameter("requestType"))) {
            int requestType = Integer.parseInt(request.getParameter("requestType"));
            if (requestType >= 1 && requestType <= 3) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidGetRequest(HttpServletRequest request) {
        if (doesValidRequestTypeExist(request)) {
            if (Integer.parseInt(request.getParameter("requestType")) == 2 || Integer.parseInt(request.getParameter("requestType")) == 3) {
                return true;
            }
        }
        return false;
    }

    private JsonObject generateFailedUploadResponseFileJSON(String msg) {
        JsonObject temp = new JsonObject();
        temp.addProperty("responseCode", "2");
        temp.addProperty("msg", msg);
        return temp;
    }

    private JsonObject generateSuccessfulUploadResponseFileJSON(String msg) {
        JsonObject temp = new JsonObject();
        temp.addProperty("responseCode", "1");
        temp.addProperty("msg", msg);
        return temp;
    }

    public void addNewFile(FileForm fileForm) throws Exception {
        fileFormDAO.addNewFile(fileForm);
    }


    private boolean isUploadRequestValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonArray responseFileListJSON = new JsonArray();

        //Files cannot be uploaded without logging in
        LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
        if (loginDTO == null) {
            responseFileListJSON.add(generateFailedUploadResponseFileJSON("File cannot be uploaded without logging in"));
            response.getWriter().write(responseFileListJSON.toString());
            return false;
        }
        //Check if path does exist/can be created
        String basePath = FileServletConstants.BASE_PATH + FileServletConstants.TEMP_UPLOAD_DIR;
        File folder = new File(basePath);
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                responseFileListJSON.add(generateFailedUploadResponseFileJSON("Problem in directory creation"));
                response.getWriter().write(responseFileListJSON.toString());
                return false;
            }
        }
        //Check Multi-part request
        if (!ServletFileUpload.isMultipartContent(request)) {
            responseFileListJSON.add(generateFailedUploadResponseFileJSON("Invalid Request Format"));
            response.getWriter().write(responseFileListJSON.toString());
            return false;
        }
        return true;
    }


    @Transactional
    public void deleteFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long fileId = Long.parseLong(request.getParameter("id"));

        FileForm fileForm = fileFormDAO.getFileById(fileId);
        deleteFileFromDirectory(fileForm);

        fileFormDAO.deleteFileById(fileId);


        List<FileVsState> states = globalService.getAllObjectListByCondition(
                FileVsState.class, new FileVsStateConditionBuilder()
                        .Where()
                        .fileIdEquals(fileId)
                        .getCondition()
        );


        for (FileVsState state : states) {
            ModifiedSqlGenerator.deleteHardEntityByID(FileVsState.class, state.getId());
        }


    }


    @Transactional
    public JsonArray uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();

        List<FileForm> filesToBeAdded = new ArrayList<>();

        //An array of JsonObject will be responded
        JsonArray responseFileListJSON = new JsonArray();
        response.setContentType("application/json");

        if (isUploadRequestValid(request, response)) {
            ServletFileUpload uploadHandler = new ServletFileUpload(new DiskFileItemFactory());

            LoginDTO loginDTO = (login.LoginDTO) request.getSession(true).getAttribute(SessionConstants.USER_LOGIN);
            String fileNamePrefix = (loginDTO.getIsAdmin() ? loginDTO.getUserID() : loginDTO.getAccountID()) + "_" + request.getSession().getId() + "_";
            String contextPath = request.getSession(true).getServletContext().getContextPath();

            try {
                List<FileItem> items = uploadHandler.parseRequest(request);

                FileForm fileForm = new FileForm();

                System.out.println("number of items: " + items.size());
                for (FileItem item : items) {
                    if (!item.isFormField()) {
                        JsonObject responseFileJSON = new JsonObject();
                        File file = new File(FileServletConstants.BASE_PATH + FileServletConstants.TEMP_UPLOAD_DIR, fileNamePrefix + item.getName());
                        System.out.println(FileServletConstants.BASE_PATH + FileServletConstants.TEMP_UPLOAD_DIR + fileNamePrefix + item.getName());

                        fileForm.setRealName(item.getName());
                        fileForm.setLocalName(fileNamePrefix + item.getName());
                        fileForm.setOwnerId(loginDTO.getIsAdmin() ? loginDTO.getUserID() : loginDTO.getAccountID());
                        fileForm.setSize(item.getSize());
                        fileForm.setUploadTime(System.currentTimeMillis());
                        fileForm.setDirectoryPath(FileServletConstants.BASE_PATH + FileServletConstants.TEMP_UPLOAD_DIR + fileNamePrefix + item.getName());
                        fileForm.setDeleted(false);

                        try (FileOutputStream fout = new FileOutputStream(file);
                             BufferedOutputStream bout = new BufferedOutputStream(fout);
                             BufferedInputStream bin = new BufferedInputStream(item.getInputStream());) {

                            byte buf[] = new byte[2048];
                            while ((bin.read(buf)) != -1) {
                                bout.write(buf);
                            }
                            bin.close();
                            bout.close();
                            fout.close();
                        } catch (Exception e) {
                            responseFileJSON = generateFailedUploadResponseFileJSON("File Upload Unsuccessful");
                            continue;
                        }

                        Parser parser = new AutoDetectParser();
                        ContentHandler handler = new BodyContentHandler(-1);
                        Metadata metadata = new Metadata();

                        FileInputStream parserFIS = new FileInputStream(file);

                        parser.parse(parserFIS, handler, metadata, new ParseContext());

                        parserFIS.close();


                        //Extension Check
                        boolean isAllowed = FileUploadUtils.getMimeTypeAllowed(metadata);
                        boolean isExtensionAllowed = FileServletConstants.allowedExtensions.contains(FileUploadUtils.getSuffix(file.getName()));
                        String mimetype = FileUploadUtils.getMimeType(metadata);

                        if (file.canExecute()) {
                            file.setExecutable(false);// forcefully
                        }

                        if (!isAllowed || !isExtensionAllowed) {
                            try {
                                file.delete();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            responseFileJSON = generateFailedUploadResponseFileJSON("File Upload Unsuccessful");
                            continue;
                        } else {
                            responseFileJSON = generateSuccessfulUploadResponseFileJSON("File Upload Successful");
                            responseFileJSON.addProperty("directoryPath", fileForm.getDirectoryPath());
                            responseFileJSON.addProperty("size", item.getSize());
                            responseFileJSON.addProperty("type", mimetype);
                            responseFileJSON.addProperty("downloadUrl", "file/download.do?id=" + DatabaseConnectionFactory
                                    .getCurrentDatabaseConnection()
                                    .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(FileForm.class)));
                            responseFileJSON.addProperty("deleteUrl", "file/delete.do?id=" + DatabaseConnectionFactory
                                    .getCurrentDatabaseConnection()
                                    .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(FileForm.class)));
                        }
                        responseFileListJSON.add(responseFileJSON);
                    } else {
                        if (item.getString() != null) {
                            String fieldName = item.getFieldName();
                            String fieldValue = item.getString();
                            switch (fieldName) {
                                case "moduleId":
                                    fileForm.setModuleId(Long.parseLong(fieldValue));
                                    break;
                                case "componentId":
                                    fileForm.setComponentId(Long.parseLong(fieldValue));
                                    break;
                                case "applicationId":
                                    fileForm.setApplicationId(Long.parseLong(fieldValue));
                                    break;
                                case "stateId":
                                    long fileId = DatabaseConnectionFactory
                                            .getCurrentDatabaseConnection()
                                            .getNextIDWithoutIncrementing(ModifiedSqlGenerator.getTableName(FileForm.class));
                                    addNewStateForAFile(fileId, Long.parseLong(fieldValue));
                                    break;
                            }
                        }
                    }

                }
                addNewFile(fileForm);
            } catch (FileUploadException e) {
                e.printStackTrace();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (TikaException e) {
                e.printStackTrace();
            }
        }
        CurrentTimeFactory.destroyCurrentTimeFactory();
        return responseFileListJSON;
    }

    @Transactional
    public void downloadFile(HttpServletRequest request, HttpServletResponse response) {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            FileForm fileForm = fileFormDAO.getFileById(id);

            String filePath = fileForm.getDirectoryPath();

            File file = new File(filePath);
            download(fileForm, file, response);

        } catch (Exception e) {

        }
    }

    private void download(FileForm fileForm, File file, HttpServletResponse response) throws Exception {
        FileInputStream inputStream = new FileInputStream(file);
        int fileLength = inputStream.available();

        response.setContentLength(fileLength);
        String headerKey = "Content-Disposition";
        String headerValue = String.format("attachment; filename=\"%s\"", fileForm.getRealName());
        response.setHeader(headerKey, headerValue);

        // writes the file to the client
        ServletOutputStream outputStream = response.getOutputStream();

        byte[] outputByte = new byte[FileTypeConstants.BUFFER_SIZE];
        // copy binary content to output stream
        while (inputStream.read(outputByte, 0, FileTypeConstants.BUFFER_SIZE) != -1) {
            outputStream.write(outputByte, 0, FileTypeConstants.BUFFER_SIZE);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    public void deleteFileFromDirectory(FileForm fileForm) throws IOException {
        File finalFile = new File(fileForm.getDirectoryPath());
        if (finalFile.isFile() && finalFile.exists()) {
            try {
                finalFile.delete();
            } catch (Exception ex) {
            }
        } else {
            throw new FileNotFoundException("No file found with this name");
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FileForm> getAllFiles(long moduleId, long componentId, long applicationId, long stateId) throws Exception {
        List<FileForm> files = new ArrayList<>();
        List<FileVsState> fileVsStates = fileFormDAO.getAllFilesByStateId(stateId);
        for (FileVsState fileVsState : fileVsStates) {
            FileForm fileForm = ModifiedSqlGenerator.getObjectByID(FileForm.class, fileVsState.getFileId());

            if (fileForm.getApplicationId() == applicationId &&
                    fileForm.getModuleId() == moduleId &&
                    fileForm.getComponentId() == componentId) {

                files.add(fileForm);
            }
        }
        return files;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FileForm> getAllFilesByApplicationIdAndStateId(long applicationId, long stateId) throws Exception {
        return fileFormDAO.getAllFilesByApplicationIdAndStateId(applicationId, stateId);
    }


    @Transactional
    public void addNewStateForAFile(long fileId, long stateId) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        FileVsState fileVsState = new FileVsState();
        fileVsState.setFileId(fileId);
        fileVsState.setStateId(stateId);

        fileFormDAO.addNewStateForAFile(fileVsState);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FileForm> getAllFilesOfAUser(long userId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FileForm.class,
                " Where " + ModifiedSqlGenerator.getColumnName(FileForm.class, "ownerId") +
                        " = " + userId);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<FileForm> getAllFilesByApplicationId(long applicationId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(FileForm.class,
                " Where " + ModifiedSqlGenerator.getColumnName(FileForm.class, "applicationId") +
                        " = " + applicationId);
    }


    public static void main(String[] args) throws Exception {
        List<FileForm> fileForms = ServiceDAOFactory.getService(FileFormService.class).getAllFiles(7, 2, 231002, 25);
        for (FileForm fileForm : fileForms
        ) {
            System.out.println(fileForm.getId());
        }
    }


    //written for upstream
    @Transactional(transactionType = TransactionType.READONLY)
    public List<FileForm> getAllFiles(long applicationId) throws Exception {
        List<FileForm> files = globalService.getAllObjectListByCondition(
                FileForm.class, new FileFormConditionBuilder()
                        .Where()
                        .applicationIdEquals(applicationId)
                        .getCondition()
        );

        if (files.isEmpty()) {
            System.out.println("No files found for application id " + applicationId);
        }
        return files;
    }
}
