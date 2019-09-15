package lli.Application.AdditionalPort;

import annotation.DAO;
import annotation.Transactional;
import lli.Application.AdditionalIP.LLIAdditionalIP;

public class LLIAdditionalPortService {

    @DAO
    LLIAddtionalPortDAO lliAddtionalPortDAO;

    @Transactional
    public void insertAdditionalPortApplication(AdditionalPort additionalPort) throws  Exception{
        lliAddtionalPortDAO.insertAdditionalPortApplication(additionalPort);
    }

    @Transactional
    public AdditionalPort getAdditionalPortByApplication(long applicationID)throws Exception {
       return lliAddtionalPortDAO.getAdditionalPortByApplication(applicationID);
    }
}
