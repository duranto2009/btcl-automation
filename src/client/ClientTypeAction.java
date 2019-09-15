package client;

import client.classification.ClientClassificationService;
import client.classification.RegistrantSubCategoryDTO;
import common.ObjectPair;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@ActionRequestMapping("ClientType")
public class ClientTypeAction extends AnnotatedRequestMappingAction {

    @Service
    ClientTypeService clientTypeService;

    @Service
    ClientClassificationService clientClassificationService;

    @RequestMapping(mapping = "/GetRegistrantCategory", requestMethod = RequestMethod.GET)
    public List<ObjectPair<Integer, String>> getRegistrantCategoryByRegistrantType(@RequestParameter("registrantType") Integer registrantType,
                                                                                   @RequestParameter("moduleID") Integer moduleID) throws Exception {
        return clientTypeService.getRegistrantCategoryListByModuleIDAndRegistrantType(moduleID, registrantType);
    }

    @RequestMapping(mapping = "/GetCompanyIdentityList", requestMethod = RequestMethod.GET)
    public List<ClientRegistrationDocumentDescrptor> getCompanyIdentityList(@RequestParameter("registrantType") Integer registrantType, @RequestParameter("moduleID") Integer moduleID, @RequestParameter("registrantCategory") Integer registrantCategory) throws Exception {
        return clientTypeService.getIdentityListByModuleIDAndRegistrantTypeAndRegistrantCategory(moduleID, registrantType, registrantCategory);
    }

    @RequestMapping(mapping = "/GetIndividualIdentityList", requestMethod = RequestMethod.GET)
    public List<ClientRegistrationDocumentDescrptor> getIndividualIdentityList(@RequestParameter("moduleID") Integer moduleID) throws Exception {
        return clientTypeService.getIndividualIdentityListByModuleID(moduleID);
    }

    @RequestMapping(mapping = "/GetRegistrantTypesInAModule", requestMethod = RequestMethod.GET)
    List<ObjectPair<Integer, String>> getRegistrantTypeListByModuleID(@RequestParameter("moduleID") Integer moduleID) throws Exception {
        return clientTypeService.getRegistrantTypeListByModuleID(moduleID);
    }

    @RequestMapping(mapping = "/getSubCategoriesUnderACategory", requestMethod = RequestMethod.GET)
    List<ObjectPair<Long, String>> getSubCategoriesUnderACategory(@RequestParameter("registrantCategory") Long parentCategory) throws Exception {
        List<RegistrantSubCategoryDTO> dtos = clientClassificationService.getAllSubCategoriesInACategory(parentCategory);
        List<ObjectPair<Long, String>> objectPairs = new ArrayList<>();

        for (RegistrantSubCategoryDTO dto : dtos) {
            objectPairs.add(new ObjectPair(dto.getRegistrantSubCategoryId(), dto.getName()));
        }
        System.out.println(objectPairs);
        return objectPairs;
    }

    @RequestMapping(mapping = "/getAllModules", requestMethod = RequestMethod.All)
    List<ObjectPair<Long, String>> getAllModules() throws Exception {
        return clientClassificationService.getAllModules();
    }

    @RequestMapping(mapping = "/getAllRegistrantTypes", requestMethod = RequestMethod.All)
    List<ObjectPair<Long, String>> getAllRegistrantTypes() throws Exception {
        return clientClassificationService.getAllRegistrantTypes();
    }


    @RequestMapping(mapping = "/getAllRegistrantCategories", requestMethod = RequestMethod.All)
    List<ObjectPair<Long, String>> getAllRegistrantCategories() throws Exception {
        return clientClassificationService.getAllRegistrantCategories();
    }



    @RequestMapping(mapping = "/getTariffCategories", requestMethod = RequestMethod.All)
    List<ObjectPair<Long, String>> getAllTariffCategories() throws Exception {
        return clientClassificationService.getAllTariffCategories();
    }





}
