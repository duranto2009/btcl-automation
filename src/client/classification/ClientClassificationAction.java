package client.classification;


import annotation.ForwardedAction;
import annotation.JsonPost;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.RequestMapping;
import requestMapping.annotation.RequestMethod;

@ActionRequestMapping("client-classification")
public class ClientClassificationAction extends AnnotatedRequestMappingAction {

    @Service
    private ClientClassificationService clientClassificationService;


    @ForwardedAction
    @RequestMapping(mapping = "/modify-client-tariff-category-page", requestMethod = RequestMethod.All)
    public String getClientTariffCategoryModifyPage() {
        return "modify-client-tariff-category-page";

    }


    @ForwardedAction
    @RequestMapping(mapping = "/add-client-type-page", requestMethod = RequestMethod.All)
    public String getClientTypeAddPage() {
        return "add-client-type-page";

    }

    @ForwardedAction
    @RequestMapping(mapping = "/add-client-category-page", requestMethod = RequestMethod.All)
    public String getClientCategoryAddPage() {
        return "add-client-category-page";

    }

    @ForwardedAction
    @RequestMapping(mapping = "/modify-client-type-page", requestMethod = RequestMethod.All)
    public String getClientTypeModifyPage() {
        return "modify-client-type-page";

    }


    @JsonPost
    @RequestMapping(mapping = "/modify-client-type", requestMethod = RequestMethod.All)
    public void modifyClientType(
            @RequestParameter(isJsonBody = true, value = "typeId") Long typeId,
            @RequestParameter(isJsonBody = true, value = "typeName") String typeName
    ) throws Exception {

        clientClassificationService.modifyClientType(typeId, typeName);
    }


    @ForwardedAction
    @RequestMapping(mapping = "/modify-client-category-page", requestMethod = RequestMethod.All)
    public String getClientCategoryModifyPage() {
        return "modify-client-category-page";

    }


    @JsonPost
    @RequestMapping(mapping = "/modify-client-category", requestMethod = RequestMethod.All)
    public void modifyClientCategory(
            @RequestParameter(isJsonBody = true, value = "categoryId") Long categoryId,
            @RequestParameter(isJsonBody = true, value = "categoryName") String categoryName
    ) throws Exception {

        clientClassificationService.modifyClientCategory(categoryId, categoryName);
    }


    @JsonPost
    @RequestMapping(mapping = "/add-type-under-a-module", requestMethod = RequestMethod.All)
    public void addTypeUnderAModule(@RequestParameter(isJsonBody = true, value = "moduleId") Integer moduleId,
                                    @RequestParameter(isJsonBody = true, value = "typeName") String typeName)
            throws Exception {

        clientClassificationService.addRegistrantTypeInAModule(moduleId, typeName);
    }

    @JsonPost
    @RequestMapping(mapping = "/add-category-under-a-type", requestMethod = RequestMethod.All)
    public void addTypeUnderAModule(@RequestParameter(isJsonBody = true, value = "moduleId") Integer moduleId,
                                    @RequestParameter(isJsonBody = true, value = "typeId") Long typeId,
                                    @RequestParameter(isJsonBody = true, value = "categoryName") String categoryName,
                                    @RequestParameter(isJsonBody = true, value = "tariffCatId") Integer tariffCatId)
            throws Exception {

        clientClassificationService.addRegistrantCategoryInATypeUnderAModule(moduleId, typeId, categoryName, tariffCatId);
    }

    @JsonPost
    @RequestMapping(mapping = "/get-tariff-category", requestMethod = RequestMethod.All)
    public Integer getClientTariffCategory(
            @RequestParameter(isJsonBody = true, value = "moduleId") Integer moduleId,
            @RequestParameter(isJsonBody = true, value = "typeId") Integer typeId,
            @RequestParameter(isJsonBody = true, value = "categoryId") Long categoryId
    ) throws Exception {
        return clientClassificationService.getClientTariffCategory(moduleId, typeId, categoryId);
    }

    @JsonPost
    @RequestMapping(mapping = "/modify-tariff-category", requestMethod = RequestMethod.All)
    public void modifyTariffCategory(
            @RequestParameter(isJsonBody = true, value = "moduleId") Integer moduleId,
            @RequestParameter(isJsonBody = true, value = "typeId") Integer typeId,
            @RequestParameter(isJsonBody = true, value = "categoryId") Long categoryId,
            @RequestParameter(isJsonBody = true, value = "tariffCatId") Integer tariffCatId
    ) throws Exception {
        clientClassificationService.modifyClientTariffCategory(moduleId, typeId, categoryId, tariffCatId);
    }

}
