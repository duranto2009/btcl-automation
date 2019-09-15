package coLocation.inventory;

import login.LoginDTO;
import requestMapping.Service;
import util.ModifiedSqlGenerator;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static util.ModifiedSqlGenerator.*;

public class CoLocationInventoryDAO {

    void updateCoLocationInventory(CoLocationInventoryDTO coLocationInventoryDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(coLocationInventoryDTO);
    }


    void insertCoLocationInventory(CoLocationInventoryDTO coLocationInventoryDTO) throws Exception {
        insert(coLocationInventoryDTO);
    }

    List<CoLocationInventoryDTO> getFreeInventory(long catType, long templateType, double availableAmount) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryDTO.class,
                        new CoLocationInventoryDTOConditionBuilder()
                                .Where()
                                .catagoryIDEquals(catType)
                                .templateIDEquals(templateType)
                                .availableAmountGreaterThanEquals(availableAmount)
                                .orderByavailableAmountAsc()
                                .getCondition()
                );


    }

    CoLocationInventoryDTO getInventoryByID(long id) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(CoLocationInventoryDTO.class, id);
    }

    List<CoLocationInventoryDTO> getUnusedFreeInventory(long catType, long templateType, double availableAmount) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryDTO.class,
                        new CoLocationInventoryDTOConditionBuilder()
                                .Where()
                                .catagoryIDEquals(catType)
                                .templateIDEquals(templateType)
                                .availableAmountGreaterThanEquals(availableAmount)
                                .isUsed(false)
                                .orderByavailableAmountAsc()
                                .getCondition()
                );
    }

    public List<Long> getIDsWithSearchCriteria(Hashtable<String, String> hashtable, LoginDTO loginDTO) throws Exception {
        ResultSet rs = getResultSetBySqlPair(
                new CoLocationInventoryDTOConditionBuilder()
                        .selectId()
                        .fromCoLocationInventoryDTO()
                        .Where()
                        .catagoryIDEqualsString(hashtable.getOrDefault("Category Name", ""))
//                        .Where()
//                        .(!loginDTO.getIsAdmin() ? loginDTO.getAccountID() : null)
                        .getSqlPair()
        );

        return getSingleColumnListByResultSet(rs, Long.class);
    }

    public List<CoLocationInventoryDTO> getDTOListByIDList(List<Long> idList) throws Exception {
        return getObjectListByIDList(CoLocationInventoryDTO.class, idList);
    }
}
