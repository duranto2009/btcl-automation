package entity.efr;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import login.LoginDTO;
import user.UserRepository;
import util.TransactionType;

import java.util.List;

public class EFRService {


    @Transactional
    public List<EFR> setVolatile(List<EFR> efrList){
        for (EFR efr:efrList
        ) {
            if(efr.getVendorID()>0){
                efr.setVendorName(UserRepository.getInstance().getUserDTOByUserID(efr.getVendorID()).getFullName());
            }
        }
        return efrList;
    }


}
