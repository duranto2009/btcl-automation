package coLocation.inventory;

import util.ModifiedSqlGenerator;

import java.util.List;

public class CoLocationInventoryTemplateDAO {
    public List<CoLocationInventoryTemplateDTO> getInventoryTemplate(int type) throws Exception {

            return
                    ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryTemplateDTO.class,
                            new CoLocationInventoryTemplateDTOConditionBuilder()
                                    .Where()
                                    .typeEquals(type)
//                                    .isIgnoredEquals(0)
                                    .getCondition()
                    );




    }

    public CoLocationInventoryTemplateDTO getInventoryTemplateByID(long id) throws Exception {

        List<CoLocationInventoryTemplateDTO> list =
                ModifiedSqlGenerator.getAllObjectList(CoLocationInventoryTemplateDTO.class,
                        new CoLocationInventoryTemplateDTOConditionBuilder()
                                .Where()
                                .idEquals(id)
//                                    .isIgnoredEquals(0)
                                .getCondition()
                );
        if(list == null || list.size()<1)return null;
        else return list.get(0);




    }
}
