/**
 *
 */
package client.classification;

import annotation.Transactional;
import common.RequestFailureException;
import exception.NoDataFoundException;
import util.ModifiedSqlGenerator;
import util.SqlGenerator;
import vpn.client.ClientDetailsDTO;

import java.util.List;

import static util.ModifiedSqlGenerator.getColumnName;

/**
 * @author Touhid
 */
public class ClientClassificationDAO {


    //TODO touhid; Please remove conditional logic from DAO layer and insert inside service layer.
    public void addRegistrantType(RegistrantTypeDTO registrantTypeDTO) throws Exception {
        ModifiedSqlGenerator.insert(registrantTypeDTO);
    }

    public void updateRegistrantType(RegistrantTypeDTO registrantTypeDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(registrantTypeDTO);
    }

    public void deleteRegistrantType(RegistrantTypeDTO registrantTypeDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(registrantTypeDTO);
    }

    public boolean doesRegistrantTypeAlreadyExist(String registrantTypeName,
                                                  List<RegistrantTypeDTO> registrantTypeDTOs) {

        if (registrantTypeDTOs != null) {
            for (RegistrantTypeDTO registrantTypeDTO : registrantTypeDTOs) {
                if (registrantTypeDTO.getName().equalsIgnoreCase(registrantTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public long getNextRegistrantTypeId(List<RegistrantTypeDTO> registrantTypeDTOs) {

        long maxId = 0;

        if (registrantTypeDTOs != null) {
            for (RegistrantTypeDTO registrantTypeDTO : registrantTypeDTOs) {
                if (registrantTypeDTO.getRegTypeId() > maxId) {
                    maxId = registrantTypeDTO.getRegTypeId();
                }
            }
        }
        long regTypeId = maxId + 1;

        return regTypeId;
    }

    public List<RegistrantTypeDTO> getAllRegistrantTypes() {
        List<RegistrantTypeDTO> registrantTypeDTOs = null;
        try {
            registrantTypeDTOs = ModifiedSqlGenerator.getAllObjectList(RegistrantTypeDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrantTypeDTOs;
    }

    public boolean doesRegistrantTypeAlreadyExist(long registrantTypeId) {

        List<RegistrantTypeDTO> registrantTypeDTOs = getAllRegistrantTypes();

        for (RegistrantTypeDTO registrantTypeDTO : registrantTypeDTOs) {
            if (registrantTypeDTO.getRegTypeId() == registrantTypeId)
                return true;
        }
        return false;
    }

    public void addRegistrantTypeInAModule(RegistrantTypesInAModuleDTO registrantTypesInAModuleDTO) throws Exception {
        ModifiedSqlGenerator.insert(registrantTypesInAModuleDTO);
    }

    // untested
    public List<RegistrantCategoryDTO> getAllRegistrantCategories() {
        List<RegistrantCategoryDTO> registrantCategoryDTOs = null;
        try {
            registrantCategoryDTOs = ModifiedSqlGenerator.getAllObjectList(RegistrantCategoryDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registrantCategoryDTOs;
    }

    // untested
    public boolean doesRegistrantCategoryAlreadyExist(String registrantCategoryName,
                                                      List<RegistrantCategoryDTO> registrantCategoryDTOs) {
        if (registrantCategoryDTOs != null) {
            for (RegistrantCategoryDTO registrantCategoryDTO : registrantCategoryDTOs) {
                if (registrantCategoryDTO.getName().equalsIgnoreCase(registrantCategoryName)) {
                    return true;
                }
            }
        }
        return false;
    }

    // untested
    public long getNextRegistrantCategoryId(List<RegistrantCategoryDTO> registrantCategoryDTOs) {
        long maxId = 0;

        if (registrantCategoryDTOs != null) {
            for (RegistrantCategoryDTO registrantCategoryDTO : registrantCategoryDTOs) {
                if (registrantCategoryDTO.getRegistrantCategoryId() > maxId) {
                    maxId = registrantCategoryDTO.getRegistrantCategoryId();
                }
            }
        }
        long regCategoryId = maxId + 1;

        return regCategoryId;
    }

    public void addRegistrantCategory(RegistrantCategoryDTO registrantCategoryDTO) throws Exception {
        ModifiedSqlGenerator.insert(registrantCategoryDTO);
    }

    public long getRegistrantTypeInAModuleId(long moduleId, long registrantTypeId) throws Exception {
        List<RegistrantTypesInAModuleDTO> registrantTypesInAModuleDTOs = ModifiedSqlGenerator
                .getAllObjectList(RegistrantTypesInAModuleDTO.class, new RegistrantTypesInAModuleDTOConditionBuilder()
                        .Where().moduleIdEquals(moduleId).registrantTypeIdEquals(registrantTypeId).getCondition());

        if (registrantTypesInAModuleDTOs != null) {
            long registrantTypeInAModuleId = registrantTypesInAModuleDTOs.get(0).getId();
            return registrantTypeInAModuleId;
        } else
            return -1;
    }

    public void addRegistrantCategoryInATypeUnderAModule(RegistrantCategoriesInATypeDTO registrantCategoriesInATypeDTO)
            throws Exception {
        ModifiedSqlGenerator.insert(registrantCategoriesInATypeDTO);
    }

    public boolean doesRegistrantCategoryAlreadyExist(long registrantCategoryId) {
        List<RegistrantCategoryDTO> registrantCategoryDTOs = getAllRegistrantCategories();
        for (RegistrantCategoryDTO registrantCategoryDTO : registrantCategoryDTOs) {
            if (registrantCategoryDTO.getRegistrantCategoryId() == registrantCategoryId)
                return true;
        }
        return false;
    }

    public boolean doesRegistrantCategoryAlreadyExist(String catName) {
        List<RegistrantCategoryDTO> registrantCategoryDTOs = getAllRegistrantCategories();
        for (RegistrantCategoryDTO registrantCategoryDTO : registrantCategoryDTOs) {
            if (registrantCategoryDTO.getName().equalsIgnoreCase(catName))
                return true;
        }
        return false;
    }


    public long getRegistrantCategoryInATypeId(long registrantTypeInAModuleId, long registrantCategoryId)
            throws Exception {
        List<RegistrantCategoriesInATypeDTO> registrantCategoriesInATypeDTOs = ModifiedSqlGenerator.getAllObjectList(
                RegistrantCategoriesInATypeDTO.class,
                new RegistrantCategoriesInATypeDTOConditionBuilder().Where()
                        .registrantTypeInAModuleIdEquals(registrantTypeInAModuleId)
                        .registrantCategoryIdEquals(registrantCategoryId).getCondition());

        if (registrantCategoriesInATypeDTOs != null) {
            return registrantCategoriesInATypeDTOs.get(0).getId();
        } else
            return -1;
    }

    public void addRequiredDocInARegistrantCategory(RequiredDocsInACategoryDTO requiredDocsInACategoryDTO) throws Exception {
        ModifiedSqlGenerator.insert(requiredDocsInACategoryDTO);
    }

    @Transactional
    public RegistrantCategoryDTO getRegistrantCategoryDTOById(long registrantCategoryId) throws Exception {

        List<RegistrantCategoryDTO> registrantCategoryDTOs = ModifiedSqlGenerator.getAllObjectList(RegistrantCategoryDTO.class);

        for (RegistrantCategoryDTO registrantCategoryDTO : registrantCategoryDTOs) {
            if (registrantCategoryDTO.getRegistrantCategoryId() == registrantCategoryId)
                return registrantCategoryDTO;
        }
        return null;
    }

    List<RegistrantTypeDTO> getClientTypeById(long id) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(RegistrantTypeDTO.class,
                " where " +
                        getColumnName(RegistrantTypeDTO.class, "regTypeId") +
                        " = " +
                        id
        );
    }

    public String getClientCategoryById(long id) throws Exception {
        List<RegistrantCategoryDTO> registrantTypeDTOS = ModifiedSqlGenerator.getAllObjectList(RegistrantCategoryDTO.class,
                " where " +
                        getColumnName(RegistrantCategoryDTO.class, "registrantCategoryId") +
                        " = " +
                        id
        );

        if (registrantTypeDTOS != null) return registrantTypeDTOS.get(0).getName();
        else {
            throw new RequestFailureException("no registrant category with id " + id + " found");
        }
    }

    public long getClientCategoryIdByClientIdAndModuleId(long clientId, int moduleId) throws Exception {
        List<ClientDetailsDTO> clientDetailsDTOS = ModifiedSqlGenerator.getAllObjectList(ClientDetailsDTO.class,
                " where " +
                        SqlGenerator.getForeignKeyColumnName(ClientDetailsDTO.class) +
                        " = " +
                        clientId +
                        " and " +
                        getColumnName(ClientDetailsDTO.class, "moduleID") +
                        " = " +
                        moduleId
        );

        if (clientDetailsDTOS.size()!=0) return clientDetailsDTOS.get(0).getRegistrantCategory();
        else {
            throw new RequestFailureException("no registrant category with client id " + clientId + " and module id " + moduleId + " found");
        }
    }


    public List<RegistrantSubCategoryDTO> getAllSubCategoriesInACategory(long categoryId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(RegistrantSubCategoryDTO.class,
                " Where " +
                        ModifiedSqlGenerator.getColumnName(RegistrantSubCategoryDTO.class, "parentCategoryId") +
                        " = " +
                        categoryId
        );
    }


    public Integer getClientTariffCategory(long registrantTypeInAModuleId, long registrantCategory) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(RegistrantCategoriesInATypeDTO.class,
                " Where " +
                        getColumnName(RegistrantCategoriesInATypeDTO.class, "registrantTypeInAModuleId") +
                        " = " +
                        registrantTypeInAModuleId +
                        " and " +
                        getColumnName(RegistrantCategoriesInATypeDTO.class, "registrantCategoryId") +
                        " = " +
                        registrantCategory).get(0).getTariffCatId();
    }

    public ClientDetailsDTO getClientDetailsDTOByClientId(int moduleId, long clientId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(
                ClientDetailsDTO.class,
                " Where " +
                        getColumnName(ClientDetailsDTO.class, "moduleID") +
                        " = " +
                        moduleId +
                        " and " +
                        getColumnName(ClientDetailsDTO.class, "clientID") +
                        " = " +
                        clientId).get(0);
    }


    public String getClientSubCategoryById(long id) throws Exception {
        List<RegistrantSubCategoryDTO> dtos = ModifiedSqlGenerator.getAllObjectList(RegistrantSubCategoryDTO.class, " Where " +
                getColumnName(RegistrantSubCategoryDTO.class, "registrantSubCategoryId") +
                " = " +
                id);
        if (dtos.size() == 0)
            return "N/A";
        return dtos.get(0).getName();
    }

    RegistrantCategoryDTO getDiscountRateForClient(long catId) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(
                RegistrantCategoryDTO.class,
                " Where " +
                        getColumnName(RegistrantCategoryDTO.class, "registrantCategoryId") +
                        " = " +
                        catId)
                .stream()
                .findFirst()
                .orElseThrow(()->new NoDataFoundException("No Registrant Category Found by reg cat id " + catId));
    }
}
