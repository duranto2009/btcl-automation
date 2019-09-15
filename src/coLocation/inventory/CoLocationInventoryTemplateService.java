package coLocation.inventory;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.ModifiedSqlGenerator;
import util.TransactionType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CoLocationInventoryTemplateService {
    @DAO private CoLocationInventoryTemplateDAO dao;

    @Transactional(transactionType= TransactionType.READONLY)
    public List<CoLocationInventoryTemplateDTO> getInventoryTemplate(int type) throws Exception {
        List<CoLocationInventoryTemplateDTO> inventoryTemplate= dao.getInventoryTemplate(type);
        if(inventoryTemplate == null) {
            throw new RequestFailureException("No Data found ");
        }
        return inventoryTemplate;
    }


    @Transactional(transactionType= TransactionType.READONLY)
    public CoLocationInventoryTemplateDTO getInventoryTemplateByID(long type) throws Exception {
       CoLocationInventoryTemplateDTO inventoryTemplate= dao.getInventoryTemplateByID(type);
        if(inventoryTemplate == null) {
            throw new RequestFailureException("No Data found ");
        }
        return inventoryTemplate;
    }

    @Transactional (transactionType = TransactionType.READONLY)
    public Map<Long, CoLocationInventoryTemplateDTO> getCoLocationInventoryTemplateMap() throws Exception {
        List<CoLocationInventoryTemplateDTO> list = ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryTemplateDTO.class);
        return list
                .stream()
                .collect(
                        Collectors.toMap(CoLocationInventoryTemplateDTO::getId, Function.identity()
                        )
                );
    }



}
