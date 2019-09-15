/**
 *
 */
package clientdocument;

import annotation.Transactional;
import file.FileDTO;
import file.FileDTOConditionBuilder;
import global.GlobalService;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.ModifiedSqlGenerator;

import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Touhid
 *
 */
public class ClientDocumentService {

    ClientDocumentDAO clientDocumentDAO = new ClientDocumentDAO();

    @Service
    private GlobalService globalService;

    @Transactional
    public void insertDocumentForAClient(Blob document, String description) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        ClientDocumentDTO clientDocumentDTO = new ClientDocumentDTO();
        clientDocumentDTO.setDocument(document);
        clientDocumentDTO.setDescription(description);
        clientDocumentDTO.setCommentDocument(false);
        clientDocumentDTO.setDeleted(false);
        clientDocumentDTO.setLastModificationTime(System.currentTimeMillis());
        clientDocumentDAO.insertDocumentForAClient(clientDocumentDTO);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public void insertDocumentForAClient(Blob document) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        ClientDocumentDTO clientDocumentDTO = new ClientDocumentDTO();

        clientDocumentDTO.setDocument(document);

        clientDocumentDTO.setCommentDocument(false);
        clientDocumentDTO.setDeleted(false);
        clientDocumentDTO.setLastModificationTime(System.currentTimeMillis());

        clientDocumentDAO.insertDocumentForAClient(clientDocumentDTO);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public void deleteDocumentForAClient(ClientDocumentDTO clientDocumentDTO) throws Exception {
        clientDocumentDTO.setDeleted(true);
        clientDocumentDAO.deleteDocumentForAClient(clientDocumentDTO);
    }

    public boolean doesAlreadyExist(String name, List<ClientDocumentTypeDTO> clientDocumentTypeDTOs) {

        if (clientDocumentTypeDTOs != null) {
            for (ClientDocumentTypeDTO clientDocumentTypeDTO : clientDocumentTypeDTOs) {
                if (clientDocumentTypeDTO.getName().equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getNextDocTypeId(List<ClientDocumentTypeDTO> clientDocumentTypeDTOs) {

        long maxId = 0;

        if (clientDocumentTypeDTOs != null) {
            for (ClientDocumentTypeDTO clientDocumentTypeDTO : clientDocumentTypeDTOs) {
                if (clientDocumentTypeDTO.getDocTypeId() > maxId) {
                    maxId = clientDocumentTypeDTO.getDocTypeId();
                }
            }
        }
        long docTypeId = maxId + 1;

        return docTypeId;
    }

    public List<ClientDocumentTypeDTO> getAllDocumentTypes() {
        List<ClientDocumentTypeDTO> clientDocumentTypeDTOs = null;
        try {
            clientDocumentTypeDTOs = ModifiedSqlGenerator.getAllObjectList(ClientDocumentTypeDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return clientDocumentTypeDTOs;
    }

    public List<FileDTO> getPreviouslyUploadedCommonFiles(
            List<ClientDocumentTypeDTO> docTypes,
            long clientId) throws Exception {

        Set<Long> commonFileIds = docTypes
                .stream()
                .filter(ClientDocumentTypeDTO::isGlobal)
                .map(ClientDocumentTypeDTO::getDocTypeId)
                .collect(Collectors.toSet());

        List<FileDTO> commonFiles = globalService.getAllObjectListByCondition(
                FileDTO.class, new FileDTOConditionBuilder()
                        .Where()
                        .docEntityIDEquals(clientId)
                        .getCondition()
        ).stream()
                .filter(t -> commonFileIds.contains(Long.valueOf(t.getDocTypeID()) % 1000))
                .collect(Collectors.toList());

        return commonFiles;

    }

    public String getDocTypeNameByTypeId(long docTypeId) throws Exception {

        List<ClientDocumentTypeDTO> docs =
                globalService.getAllObjectListByCondition(ClientDocumentTypeDTO.class,
                        new ClientDocumentTypeDTOConditionBuilder()
                                .Where()
                                .docTypeIdEquals(docTypeId)
                                .getCondition());


        if (docs.isEmpty()) {
            return "";
        } else {
            return docs.get(0).getName();
        }
    }


    public Map<Integer, Boolean> getAllDocsWithIsGlobalProperty() {

        Map<Integer, Boolean> map = new HashMap<>();

        List<ClientDocumentTypeDTO> docs = null;
        try {
            docs = globalService.getAllObjectListByCondition(ClientDocumentTypeDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        docs.forEach(x -> map.put((int) x.getDocTypeId(), x.isGlobal()));

        return map;
    }

}
