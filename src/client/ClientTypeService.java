package client;

import annotation.Transactional;
import client.classification.*;
import clientdocument.ClientDocumentTypeDTO;
import clientdocument.ClientDocumentTypeDTOConditionBuilder;
import common.ModuleConstants;
import common.ObjectPair;
import common.RequestFailureException;
import common.repository.AllClientRepository;
import global.GlobalService;
import org.apache.log4j.Logger;
import requestMapping.Service;
import util.KeyValuePair;
import util.ModifiedSqlGenerator;
import util.ServiceDAOFactory;
import util.TransactionType;
import vpn.client.ClientDetailsDTO;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ClientTypeService {

    public static Logger logger = Logger.getLogger(ClientTypeService.class);


    @Service
    private GlobalService globalService;

    public Map<Long, List<KeyValuePair<Long, RegistrantTypeDTO>>> getAllRegTypeMappedByRegTypeId() throws Exception {
        GlobalService globalService = ServiceDAOFactory.getService(GlobalService.class);
        Map<Long, List<RegistrantTypesInAModuleDTO>> mapRegTypeVsModule = globalService.getAllObjectListByCondition(
                RegistrantTypesInAModuleDTO.class,
                new RegistrantTypesInAModuleDTOConditionBuilder()
                        .getCondition()
        ).stream()
                .collect(
                        Collectors.groupingBy(RegistrantTypesInAModuleDTO::getModuleId)
                );

        Map<Long, RegistrantTypeDTO> mapRegistrantTypes = globalService.getAllObjectListByCondition(
                RegistrantTypeDTO.class,
                new RegistrantTypeDTOConditionBuilder()
                        .getCondition()
        ).stream()
                .collect(Collectors.toMap(RegistrantTypeDTO::getId, Function.identity()));

        return mapRegTypeVsModule.entrySet()
                .stream()
                .map(t -> t.getValue()
                        .stream()
                        .map(x -> new KeyValuePair<>(t.getKey(), mapRegistrantTypes.getOrDefault(x.getRegistrantTypeId(), null)))
                        .collect(Collectors.toList())
                )
                .flatMap(List::stream)
                .collect(Collectors.groupingBy(KeyValuePair::getKey));


    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<ObjectPair<Integer, String>>
    getRegistrantCategoryListByModuleIDAndRegistrantType(Integer moduleID,
                                                         Integer registrantType) throws Exception {
        List<ObjectPair<Integer, String>> objectPairs = new ArrayList<>();

        long regTypeInAModuleId = getRegistrantTypeInAModuleId(moduleID, registrantType);

        List<Long> ids = globalService.getAllObjectListByCondition(
                RegistrantCategoriesInATypeDTO.class,
                new RegistrantCategoriesInATypeDTOConditionBuilder()
                        .Where()
                        .registrantTypeInAModuleIdEquals(regTypeInAModuleId)
                        .getCondition())
                .stream()
                .map(x -> x.getRegistrantCategoryId())
                .collect(Collectors.toList());

        List<RegistrantCategoryDTO> categories = globalService.getAllObjectListByCondition(RegistrantCategoryDTO.class,
                new RegistrantCategoryDTOConditionBuilder()
                        .Where()
                        .registrantCategoryIdIn(ids)
                        .getCondition())
                .stream()
                .sorted(Comparator.comparing(RegistrantCategoryDTO::getRegistrantCategoryId))
                .collect(Collectors.toList());

        categories.forEach(x -> objectPairs.add(new ObjectPair(x.getRegistrantCategoryId(), x.getName())));

        return objectPairs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<ClientRegistrationDocumentDescrptor>
    getIdentityListByModuleIDAndRegistrantTypeAndRegistrantCategory(Integer moduleID,
                                                                    Integer registrantType,
                                                                    Integer registrantCategory) throws Exception {
        List<ClientRegistrationDocumentDescrptor> requiredDocs = new ArrayList<>();
        List<Integer> alternativeDocumentSuffixList = new ArrayList<>();

        long regCategoryInATypeId = getRegistrantCategoryInATypeUnderAModuleId(moduleID, registrantType, registrantCategory);

        List<RequiredDocsInACategoryDTO> ids = globalService
                .getAllObjectListByCondition(RequiredDocsInACategoryDTO.class,
                        new RequiredDocsInACategoryDTOConditionBuilder()
                                .Where()
                                .registrantCategoryInATypeIdEquals(regCategoryInATypeId)
                                .getCondition());

        List<ClientDocumentTypeDTO> docs = globalService.getAllObjectListByCondition(ClientDocumentTypeDTO.class,
                new ClientDocumentTypeDTOConditionBuilder()
                        .Where()
                        .docTypeIdIn(ids.stream().map(x -> x.getDocumentId()).collect(Collectors.toList()))
                        .getCondition())
                .stream()
                .sorted(Comparator.comparing(ClientDocumentTypeDTO::getDocTypeId))
                .collect(Collectors.toList());

        for (int i = 0; i < ids.size(); i++) {

            ClientRegistrationDocumentDescrptor clientRegistrationDocumentDescrptor = new ClientRegistrationDocumentDescrptor(
                    moduleID, (int) docs.get(i).getDocTypeId(), FieldType.BOTH, ids.get(i).isMandatory(),
                    alternativeDocumentSuffixList);
            clientRegistrationDocumentDescrptor.name = docs.get(i).getName();

            requiredDocs.add(clientRegistrationDocumentDescrptor);
        }

        return requiredDocs;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<ClientRegistrationDocumentDescrptor> getIndividualIdentityListByModuleID(Integer moduleID) {
        return ClientTypeConstants.getClientRegistrationDocumentDescriptorListByModuleID(moduleID);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<ObjectPair<Integer, String>> getRegistrantTypeListByModuleID(int moduleID) throws Exception {

        if (moduleID == 0) {
            throw new RequestFailureException("Invalid Module");
        }
        List<ObjectPair<Integer, String>> registrantTypes = new ArrayList<>();

        List<Long> ids = ModifiedSqlGenerator
                .getAllObjectList(RegistrantTypesInAModuleDTO.class, new RegistrantTypesInAModuleDTOConditionBuilder()
                        .Where()
                        .moduleIdEquals((long) moduleID)
                        .getCondition())
                .stream()
                .map(RegistrantTypesInAModuleDTO::getRegistrantTypeId)
                .collect(Collectors.toList());

        List<RegistrantTypeDTO> types = globalService.getAllObjectListByCondition(RegistrantTypeDTO.class,
                new RegistrantTypeDTOConditionBuilder()
                        .Where()
                        .regTypeIdIn(ids)
                        .getCondition())
                .stream()
                .sorted(Comparator.comparing(RegistrantTypeDTO::getRegTypeId))
                .collect(Collectors.toList());

        types.forEach(x -> registrantTypes.add(new ObjectPair<Integer, String>((int) x.getRegTypeId(), x.getName())));

        return registrantTypes;
    }

    /**
     * For only VPN and LLI Clients only.
     * Map is not ready for VPN. Thus using switch case for now. After Map Population, VPN Clients will get their Category from the same map
     *
     * @author dhrubo
     */
    public static Integer getClientCategoryByModuleIDAndClientID(Integer moduleID, Long clientID) throws Exception {

        logger.debug("getClientCategoryByModuleIDAndClientID called");
        switch (moduleID) {
            case ModuleConstants.Module_ID_VPN:
                return ClientCategoryConstants.Category_1;
            case ModuleConstants.Module_ID_LLI:

                ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getVpnClientByClientID(clientID, moduleID);

                logger.debug("clientDetailsDTO=" + clientDetailsDTO);

                Integer registrantType = clientDetailsDTO.getRegistrantType();
                Long registrantCategory = clientDetailsDTO.getRegistrantCategory();

                logger.debug("registrantType=" + registrantType + ", registrantCategory=" + registrantCategory);

                //get from database here
                Integer tariffCategory = ServiceDAOFactory.getService(ClientClassificationService.class).getClientTariffCategory(moduleID, registrantType, registrantCategory);
//			Integer tariffCategory = ClientTypeConstants.ClientCategoryIDToCatIDToRegTypeIDToModuleID.get(moduleID).get(registrantType).getOrDefault(registrantCategory, 1);
                if (null == tariffCategory) {
                    throw new RequestFailureException("Invalid Tariff Category");
                }
                return tariffCategory;
            default:
                return null;
        }
    }


    public long getRegistrantTypeInAModuleId(long moduleID, long registrantType) throws Exception {

        return globalService.getAllObjectListByCondition(RegistrantTypesInAModuleDTO.class,
                new RegistrantTypesInAModuleDTOConditionBuilder()
                        .Where()
                        .registrantTypeIdEquals(registrantType)
                        .moduleIdEquals(moduleID)
                        .getCondition())
                .get(0)
                .getId();

    }

    public long getRegistrantCategoryInATypeUnderAModuleId(int moduleId, int regType, int regCategory) throws Exception {
        long regTypeInAModuleId = getRegistrantTypeInAModuleId((long) moduleId, (long) regType);

        return globalService.getAllObjectListByCondition(RegistrantCategoriesInATypeDTO.class,
                new RegistrantCategoriesInATypeDTOConditionBuilder()
                        .Where()
                        .registrantCategoryIdEquals((long) regCategory)
                        .registrantTypeInAModuleIdEquals(regTypeInAModuleId)
                        .getCondition())
                .get(0)
                .getId();


    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<KeyValuePair<ClientDocumentTypeDTO, Boolean>>
    getRequiredDocList(Integer moduleID,
                       Integer registrantType,
                       Integer registrantCategory) throws Exception {

        long regCategoryInATypeId = getRegistrantCategoryInATypeUnderAModuleId(moduleID, registrantType, registrantCategory);

        List<RequiredDocsInACategoryDTO> ids = globalService
                .getAllObjectListByCondition(RequiredDocsInACategoryDTO.class,
                        new RequiredDocsInACategoryDTOConditionBuilder()
                                .Where()
                                .registrantCategoryInATypeIdEquals(regCategoryInATypeId)
                                .getCondition());

        List<ClientDocumentTypeDTO> docs = new ArrayList<>();

        docs = globalService.getAllObjectListByCondition(ClientDocumentTypeDTO.class,
                new ClientDocumentTypeDTOConditionBuilder()
                        .Where()
                        .docTypeIdIn(ids.stream().map(x -> x.getDocumentId()).collect(Collectors.toList()))
                        .getCondition())
                .stream()
                .sorted(Comparator.comparing(ClientDocumentTypeDTO::getDocTypeId))
                .collect(Collectors.toList());


        List<KeyValuePair<ClientDocumentTypeDTO, Boolean>> pairs = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            pairs.add(new KeyValuePair<>(docs.get(i), ids.get(i).isMandatory()));
        }


        return pairs;
    }


    public static void main(String args[]) throws Exception {
        System.out.println(ServiceDAOFactory.getService(ClientTypeService.class)
                .getRegistrantCategoryListByModuleIDAndRegistrantType(7, 1));
    }

}
