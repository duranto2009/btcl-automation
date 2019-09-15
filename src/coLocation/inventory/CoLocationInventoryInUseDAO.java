package coLocation.inventory;

import util.ModifiedSqlGenerator;

import java.util.List;

import static util.ModifiedSqlGenerator.insert;

public class CoLocationInventoryInUseDAO {


    public void updateCoLocationInventoryInUse(CoLocationInventoryInUseDTO coLocationInventoryInUseDTO) throws Exception {
        ModifiedSqlGenerator.updateEntity(coLocationInventoryInUseDTO);
    }


    public void insertCoLocationInventoryinUse(CoLocationInventoryInUseDTO coLocationInventoryInUseDTO) throws Exception {
        insert(coLocationInventoryInUseDTO);
    }
    public List<CoLocationInventoryInUseDTO> getInventoryInUseByClient(long clientID) throws Exception {

            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryInUseDTO.class,
                            new CoLocationInventoryInUseDTOConditionBuilder()
                                    .Where()
                                    .clientIDEquals(clientID)
                                    .statusEquals(1)
                                    .getCondition()
                    );




    }


    public List<CoLocationInventoryInUseDTO> getInventoryInUseByConnection(long conID) throws Exception {

        return
                ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryInUseDTO.class,
                        new CoLocationInventoryInUseDTOConditionBuilder()
                                .Where()
                                .connectionIDEquals(conID)
//                                .statusEquals(1)
                                //TODO Check status
                                .getCondition()
                );
    }




}
