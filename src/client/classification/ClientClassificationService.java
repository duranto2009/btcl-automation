/**
 *
 */
package client.classification;

import annotation.Transactional;
import client.module.ClientModuleDTO;
import client.module.ClientModuleSubscriptionDAO;
import common.ObjectPair;
import exception.NoDataFoundException;
import global.GlobalService;
import requestMapping.Service;
import util.CurrentTimeFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Touhid
 */
public class ClientClassificationService {

    ClientClassificationDAO clientClassificationDAO = new ClientClassificationDAO();

    @Service
    GlobalService globalService;

    /**
     * @author Touhid
     */
    @Transactional(transactionType = TransactionType.READONLY)
    public ClientDetailsDTO getClientDetailsDTOByClientId(int moduleId, long clientId) throws Exception {
        return clientClassificationDAO.getClientDetailsDTOByClientId(moduleId, clientId);
    }


    @Transactional
    public long addRegistrantType(String registrantTypeName) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();
        List<RegistrantTypeDTO> registrantTypeDTOs = clientClassificationDAO.getAllRegistrantTypes();
        if (clientClassificationDAO.doesRegistrantTypeAlreadyExist(registrantTypeName, registrantTypeDTOs)) {
            return globalService.getAllObjectListByCondition(RegistrantTypeDTO.class,
                    new RegistrantTypeDTOConditionBuilder()
                            .Where()
                            .nameEquals(registrantTypeName).getCondition()).get(0).getId();
        }

        long regTypeId = clientClassificationDAO.getNextRegistrantTypeId(registrantTypeDTOs);

        RegistrantTypeDTO registrantTypeDTO = new RegistrantTypeDTO();
        registrantTypeDTO.setRegTypeId(regTypeId);
        registrantTypeDTO.setName(registrantTypeName);
        registrantTypeDTO.setDeleted(false);
        registrantTypeDTO.setLastModificationTime(System.currentTimeMillis());
        clientClassificationDAO.addRegistrantType(registrantTypeDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();

        return registrantTypeDTO.getId();
    }

    @Transactional
    public void updateRegistrantType(String oldTypeName, String newTypeName) throws Exception {
        List<RegistrantTypeDTO> registrantTypeDTOs = clientClassificationDAO.getAllRegistrantTypes();
        CurrentTimeFactory.initializeCurrentTimeFactory();

        if (registrantTypeDTOs != null) {
            for (RegistrantTypeDTO registrantTypeDTO : registrantTypeDTOs) {
                if (registrantTypeDTO.getName().equalsIgnoreCase(oldTypeName)) {
                    registrantTypeDTO.setName(newTypeName);
                    registrantTypeDTO.setLastModificationTime(System.currentTimeMillis());

                    clientClassificationDAO.updateRegistrantType(registrantTypeDTO);
                    break;
                }
            }
        }
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public void deleteRegistrantType(String registrantTypeName) throws Exception {

        List<RegistrantTypeDTO> registrantTypeDTOs = clientClassificationDAO.getAllRegistrantTypes();
        CurrentTimeFactory.initializeCurrentTimeFactory();

        if (registrantTypeDTOs != null) {
            for (RegistrantTypeDTO registrantTypeDTO : registrantTypeDTOs) {
                if (registrantTypeDTO.getName().equalsIgnoreCase(registrantTypeName)) {
                    registrantTypeDTO.setDeleted(true);
                    clientClassificationDAO.deleteRegistrantType(registrantTypeDTO);
                    break;
                }
            }
        }
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional
    public long addRegistrantCategory(String registrantCategoryName) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        List<RegistrantCategoryDTO> registrantCategoryDTOs = clientClassificationDAO.getAllRegistrantCategories();

        if (clientClassificationDAO.doesRegistrantCategoryAlreadyExist(registrantCategoryName, registrantCategoryDTOs)) {
            return globalService.getAllObjectListByCondition(RegistrantCategoryDTO.class,
                    new RegistrantCategoryDTOConditionBuilder()
                            .Where()
                            .nameEquals(registrantCategoryName)
                            .getCondition()
            ).get(0).getId();

        }

        long regCategoryId = clientClassificationDAO.getNextRegistrantCategoryId(registrantCategoryDTOs);

        RegistrantCategoryDTO registrantCategoryDTO = new RegistrantCategoryDTO();
        registrantCategoryDTO.setRegistrantCategoryId(regCategoryId);
        registrantCategoryDTO.setName(registrantCategoryName);
        registrantCategoryDTO.setDeleted(false);
        registrantCategoryDTO.setLastModificationTime(System.currentTimeMillis());
        clientClassificationDAO.addRegistrantCategory(registrantCategoryDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();

        return registrantCategoryDTO.getId();
    }

    @Transactional
    public void addRegistrantTypeInAModule(long moduleId, long registrantTypeId) throws Exception {

        ClientModuleSubscriptionDAO clientModuleSubscriptionDAO = new ClientModuleSubscriptionDAO();
        if (!clientModuleSubscriptionDAO.moduleExists(moduleId))
            return;

        if (!clientClassificationDAO.doesRegistrantTypeAlreadyExist(registrantTypeId))
            return;
        CurrentTimeFactory.initializeCurrentTimeFactory();
        RegistrantTypesInAModuleDTO registrantTypesInAModuleDTO = new RegistrantTypesInAModuleDTO();
        registrantTypesInAModuleDTO.setModuleId(moduleId);
        registrantTypesInAModuleDTO.setRegistrantTypeId(registrantTypeId);
        registrantTypesInAModuleDTO.setDeleted(false);
        registrantTypesInAModuleDTO.setLastModificationTime(System.currentTimeMillis());

        clientClassificationDAO.addRegistrantTypeInAModule(registrantTypesInAModuleDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }


    @Transactional
    public void addRegistrantTypeInAModule(int moduleId, String typeName) throws Exception {

        //create new type first
        long regTypeId = addRegistrantType(typeName);

        //insert this new type under a module
        CurrentTimeFactory.initializeCurrentTimeFactory();
        RegistrantTypesInAModuleDTO dto = new RegistrantTypesInAModuleDTO();
        dto.setModuleId(moduleId);
        dto.setRegistrantTypeId(regTypeId);
        dto.setDeleted(false);
        dto.setLastModificationTime(System.currentTimeMillis());
        globalService.save(dto);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public long getNextClientTypeId() throws Exception {
        List<RegistrantTypeDTO> typeDTOS = globalService.getAllObjectListByCondition(
                RegistrantTypeDTO.class);
        long maxId = 0;
        for (RegistrantTypeDTO typeDTO : typeDTOS) {
            if (typeDTO.getRegTypeId() > maxId)
                maxId = typeDTO.getRegTypeId();
        }
        return maxId + 1;
    }


    @Transactional
    public void addRegistrantCategoryInATypeUnderAModule(long moduleId, long registrantTypeId,
                                                         long registrantCategoryId) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();

        long registrantTypeInAModuleId = clientClassificationDAO.getRegistrantTypeInAModuleId(moduleId,
                registrantTypeId);

        if (registrantTypeInAModuleId == -1)
            return;

        if (!clientClassificationDAO.doesRegistrantCategoryAlreadyExist(registrantCategoryId))
            return;

        RegistrantCategoriesInATypeDTO registrantCategoriesInATypeDTO = new RegistrantCategoriesInATypeDTO();

        registrantCategoriesInATypeDTO.setRegistrantTypeInAModuleId(registrantTypeInAModuleId);
        registrantCategoriesInATypeDTO.setRegistrantCategoryId(registrantCategoryId);

        registrantCategoriesInATypeDTO.setDeleted(false);
        registrantCategoriesInATypeDTO.setLastModificationTime(System.currentTimeMillis());

        clientClassificationDAO.addRegistrantCategoryInATypeUnderAModule(registrantCategoriesInATypeDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }


    @Transactional
    public void addRegistrantCategoryInATypeUnderAModule(int moduleId, long typeId,
                                                         String categoryName, int tariffCatId) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();

        long registrantTypeInAModuleId = clientClassificationDAO.getRegistrantTypeInAModuleId(moduleId,
                typeId);

        if (registrantTypeInAModuleId == -1)
            return;

        //create reg cat first
        long catId = addRegistrantCategory(categoryName);

        //then add it under a type

        RegistrantCategoriesInATypeDTO registrantCategoriesInATypeDTO = new RegistrantCategoriesInATypeDTO();
        registrantCategoriesInATypeDTO.setRegistrantTypeInAModuleId(registrantTypeInAModuleId);
        registrantCategoriesInATypeDTO.setRegistrantCategoryId(catId);
        registrantCategoriesInATypeDTO.setTariffCatId(tariffCatId);
        registrantCategoriesInATypeDTO.setDeleted(false);
        registrantCategoriesInATypeDTO.setLastModificationTime(System.currentTimeMillis());

        clientClassificationDAO.addRegistrantCategoryInATypeUnderAModule(registrantCategoriesInATypeDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }


    @Transactional
    public void addRequiredDocInARegistrantCategory(long moduleId, long registrantTypeId, long registrantCategoryId,
                                                    long documentId, boolean isMandatory) throws Exception {

        CurrentTimeFactory.initializeCurrentTimeFactory();

        long registrantTypeInAModuleId = clientClassificationDAO.getRegistrantTypeInAModuleId(moduleId,
                registrantTypeId);

        if (registrantTypeInAModuleId == -1)
            return;

        long registrantCategoryInATypeId = clientClassificationDAO
                .getRegistrantCategoryInATypeId(registrantTypeInAModuleId, registrantCategoryId);

        if (registrantCategoryInATypeId == -1)
            return;

        RequiredDocsInACategoryDTO requiredDocsInACategoryDTO = new RequiredDocsInACategoryDTO();

        requiredDocsInACategoryDTO.setRegistrantCategoryInATypeId(registrantCategoryInATypeId);

//		check if the document type Id is valid here
// 		May be not needed as only available options will be shown

        requiredDocsInACategoryDTO.setDocumentId(documentId);
        requiredDocsInACategoryDTO.setMandatory(isMandatory);
        requiredDocsInACategoryDTO.setDeleted(false);
        requiredDocsInACategoryDTO.setLastModificationTime(System.currentTimeMillis());

        clientClassificationDAO.addRequiredDocInARegistrantCategory(requiredDocsInACategoryDTO);

        CurrentTimeFactory.destroyCurrentTimeFactory();

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public RegistrantTypeDTO getClientRegistrantTypeByRegistrantTypeId(long id) throws Exception {
        return clientClassificationDAO.getClientTypeById(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new NoDataFoundException("No registrant type with id " + id + " found"));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public String getClientTypeById(long id) throws Exception {
        return getClientRegistrantTypeByRegistrantTypeId(id).getName();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public String getClientCategoryById(long id) throws Exception {
        return clientClassificationDAO.getClientCategoryById(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    long getClientCategoryIdByClientIdAndModuleId(long clientId, int moduleId) throws Exception {
        return clientClassificationDAO.getClientCategoryIdByClientIdAndModuleId(clientId, moduleId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public String getClientSubCategoryById(long id) throws Exception {
        return clientClassificationDAO.getClientSubCategoryById(id);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<RegistrantSubCategoryDTO> getAllSubCategoriesInACategory(long categoryId) throws Exception {
        return clientClassificationDAO.getAllSubCategoriesInACategory(categoryId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Integer getClientTariffCategory(int moduleId, int registrantType, long registrantCategory) throws Exception {
        long registrantTypeInAModuleId = clientClassificationDAO.getRegistrantTypeInAModuleId((long) moduleId,
                (long) registrantType);
        if (registrantTypeInAModuleId == -1)
            return (int) 1;
        else {
            return clientClassificationDAO.getClientTariffCategory(registrantTypeInAModuleId, registrantCategory);
        }
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public RegistrantCategoryDTO getRegistrantCategoryByClientAndModuleId(long clientId, int moduleId) throws Exception {
        long catId = getClientCategoryIdByClientIdAndModuleId(clientId, moduleId);
        return clientClassificationDAO.getDiscountRateForClient(catId);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<ObjectPair<Long, String>> getAllModules() throws Exception {
        List<ClientModuleDTO> modules = globalService.getAllObjectListByCondition(ClientModuleDTO.class);

        List<ObjectPair<Long, String>> list = new ArrayList<>();

        for (ClientModuleDTO dto : modules) {
            list.add(new ObjectPair<>(dto.getId(), dto.getName()));
        }
        return list;
    }

    public List<ObjectPair<Long, String>> getAllRegistrantTypes() throws Exception {
        List<RegistrantTypeDTO> dtos = globalService.getAllObjectListByCondition(RegistrantTypeDTO.class);

        List<ObjectPair<Long, String>> pairs = new ArrayList<>();
        for (RegistrantTypeDTO dto : dtos) {
            pairs.add(new ObjectPair<>(dto.getId(), dto.getName()));
        }

        return pairs;
    }

    public void modifyClientType(Long typeId, String typeName) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        RegistrantTypeDTO typeDTO = globalService.findByPK(RegistrantTypeDTO.class, typeId);
        typeDTO.setName(typeName);
        typeDTO.setLastModificationTime(System.currentTimeMillis());
        globalService.update(typeDTO);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    public void modifyClientCategory(Long categoryId, String categoryName) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        RegistrantCategoryDTO categoryDTO = globalService.findByPK(RegistrantCategoryDTO.class, categoryId);
        categoryDTO.setName(categoryName);
        categoryDTO.setLastModificationTime(System.currentTimeMillis());
        globalService.update(categoryDTO);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    public List<ObjectPair<Long, String>> getAllRegistrantCategories() throws Exception {

        List<RegistrantCategoryDTO> dtos = globalService.getAllObjectListByCondition(RegistrantCategoryDTO.class);

        List<ObjectPair<Long, String>> pairs = new ArrayList<>();
        for (RegistrantCategoryDTO dto : dtos) {
            pairs.add(new ObjectPair<>(dto.getId(), dto.getName()));
        }

        return pairs;
    }

    public List<ObjectPair<Long, String>> getAllTariffCategories() throws Exception {
        List<ClientTariffCategoryDTO> dtos = globalService.getAllObjectListByCondition(ClientTariffCategoryDTO.class);

        List<ObjectPair<Long, String>> pairs = new ArrayList<>();
        for (ClientTariffCategoryDTO dto : dtos) {
            pairs.add(new ObjectPair<>(dto.getId(), dto.getName()));
        }

        return pairs;
    }

    public void modifyClientTariffCategory(Integer moduleId, Integer typeId, Long categoryId, Integer tariffCatId) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();

        long typeInAModuleId = globalService.getAllObjectListByCondition(RegistrantTypesInAModuleDTO.class,
                new RegistrantTypesInAModuleDTOConditionBuilder()
                        .Where()
                        .moduleIdEquals((long) moduleId)
                        .registrantTypeIdEquals((long) typeId)
                        .getCondition()).get(0).getId();

        RegistrantCategoriesInATypeDTO dto = globalService.getAllObjectListByCondition(RegistrantCategoriesInATypeDTO.class,
                new RegistrantCategoriesInATypeDTOConditionBuilder()
                        .Where()
                        .registrantTypeInAModuleIdEquals(typeInAModuleId)
                        .registrantCategoryIdEquals(categoryId)
                        .getCondition())
                .get(0);

        dto.setTariffCatId(tariffCatId);
        dto.setLastModificationTime(System.currentTimeMillis());

        globalService.update(dto);

        CurrentTimeFactory.destroyCurrentTimeFactory();
    }
}
