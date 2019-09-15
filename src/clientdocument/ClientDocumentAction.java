package clientdocument;


import client.ClientTypeService;
import file.FileDTO;
import global.GlobalService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;
import util.KeyValuePair;

import java.util.ArrayList;
import java.util.List;

@ActionRequestMapping("client-document-type/")
public class ClientDocumentAction extends AnnotatedRequestMappingAction {

    @Service
    private GlobalService globalService;
    @Service
    private ClientDocumentService clientDocumentService;
    @Service
    private ClientTypeService clientTypeService;


    @RequestMapping(mapping = "get-required-docs", requestMethod = RequestMethod.GET)
    public List<KeyValuePair<ClientDocumentTypeDTO, Boolean>> getRequiredDocs(
            @RequestParameter("module") int module,
            @RequestParameter("type") int type,
            @RequestParameter("category") int category)
            throws Exception {

        List<KeyValuePair<ClientDocumentTypeDTO, Boolean>> docTypes = clientTypeService.getRequiredDocList(module, type, category);

        return docTypes;
    }

    @RequestMapping(mapping = "get-required-docs-with-common-files", requestMethod = RequestMethod.GET)
    public RequiredDocs getRequiredDocsWithCommonFiles(
            @RequestParameter("module") int module,
            @RequestParameter("type") int type,
            @RequestParameter("category") int category,
            @RequestParameter("prevModule") int prevModule,
            @RequestParameter("client") long client) throws Exception {
        RequiredDocs requiredDocs = new RequiredDocs();

        List<KeyValuePair<ClientDocumentTypeDTO, Boolean>> docTypes = clientTypeService.getRequiredDocList(module, type, category);

        List<ClientDocumentTypeDTO> documentTypeDTOS = new ArrayList<>();

        for (int i = 0; i < docTypes.size(); i++) {
            documentTypeDTOS.add(docTypes.get(i).getKey());
        }

        List<FileDTO> commonFiles = clientDocumentService
                .getPreviouslyUploadedCommonFiles(documentTypeDTOS, client);

        List<FileAttributes> files = new ArrayList<>();

        commonFiles
                .forEach(x -> {
                    try {
                        files.add(
                                new FileAttributes(x.getDocID(),
                                        clientDocumentService
                                                .getDocTypeNameByTypeId(
                                                        Long.parseLong(x.getDocTypeID()) % 1000 //70101=>101=>National ID
                                                ),
                                        x.getDocActualFileName(),
                                        x.getDocSizeStr()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        requiredDocs.setDocTypes(docTypes);
        requiredDocs.setCommonFiles(files);

        return requiredDocs;
    }


}
