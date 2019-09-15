package lli.Application.AdditionalIP;

import annotation.DAO;
import annotation.Transactional;

public class LLIAdditionalIPService {

    @DAO
    LLIAddtionalIPDAO lliAddtionalIPDAO;

    @Transactional
    public void insertAdditionalIPApplication(LLIAdditionalIP additionalIP) throws  Exception{
        lliAddtionalIPDAO.insertAdditionalIPApplication(additionalIP);
    }
    @Transactional
    public LLIAdditionalIP getAdditionalIPByApplication(long applicationID)throws Exception {
       return lliAddtionalIPDAO.getAdditionalIPByApplication(applicationID);
    }
}
