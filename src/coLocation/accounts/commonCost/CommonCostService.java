package coLocation.accounts.commonCost;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;

import java.util.List;

public class CommonCostService {

    @DAO
    CommonCostDAO commonCostDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CommonCostTemplateDTO> getAllCommonCostType(int appType) throws Exception {
        List<CommonCostTemplateDTO> commonCostTemplateDTOS = commonCostDAO.getAllCommonCostType(appType);
        if (commonCostTemplateDTOS.size()<1) {
            throw new RequestFailureException("No Data found in colocation common cost config template for type "+appType);
        }

        return commonCostTemplateDTOS;
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public CommonCostDTO getAllFixedCostOfApplication(int type) throws Exception {
        List<CommonCostDTO> commonCostDTOS = commonCostDAO.getAllFixedCostOfApplication(type);
        if (commonCostDTOS.size()>1) {
            throw new RequestFailureException("Multiple Configuration found for CoLocation Common Cost: type " + type);
        }

        return commonCostDTOS
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Configuration Found for type " + type));
    }
}
