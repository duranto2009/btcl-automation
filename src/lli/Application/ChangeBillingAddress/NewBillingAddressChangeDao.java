package lli.Application.ChangeBillingAddress;

import util.ModifiedSqlGenerator;

import java.util.List;

public class NewBillingAddressChangeDao {
    public void insertNewBillingAddress(LLINewBillingAddressApplication lliChangeBillingAddressApplication)throws Exception {
        ModifiedSqlGenerator.insert(lliChangeBillingAddressApplication);
    }

    public void updateItem(LLINewBillingAddressApplication lliChangeBillingAddressApplication) throws Exception{
        ModifiedSqlGenerator.updateEntity(lliChangeBillingAddressApplication);

    }

    public LLINewBillingAddressApplication getNewBillAddressByAppId(long appId)throws  Exception{
        List<LLINewBillingAddressApplication> list = ModifiedSqlGenerator.getAllObjectList(LLINewBillingAddressApplication.class,
                new LLINewBillingAddressApplicationConditionBuilder()
                        .Where()
                        .applicationIDEquals(appId)
                        .getCondition()


        );
        if(list.size()>0) return list.get(0);
        return null;
    }
}
