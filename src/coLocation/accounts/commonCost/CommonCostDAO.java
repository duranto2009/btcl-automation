package coLocation.accounts.commonCost;

import util.ModifiedSqlGenerator;

import java.util.List;

public class CommonCostDAO {

    public List<CommonCostTemplateDTO> getAllCommonCostType( int applicationType) throws Exception {


        List<CommonCostTemplateDTO> commonCostTemplateDTOS =
                ModifiedSqlGenerator.getAllObjectList(CommonCostTemplateDTO.class,
                        new CommonCostTemplateDTOConditionBuilder()
                                .Where()
                                .applicationTypeEquals(applicationType)
                                .getCondition()
                );


        return commonCostTemplateDTOS;
    }

    public List<CommonCostDTO> getAllFixedCostOfApplication( int type) throws Exception {


        List<CommonCostDTO> commonCostDTOS =
                ModifiedSqlGenerator.getAllObjectList(CommonCostDTO.class,
                        new CommonCostDTOConditionBuilder()
                                .Where()
                                .typeIDEquals(type)
                                .getCondition()
                );


        return commonCostDTOS;
    }
}
