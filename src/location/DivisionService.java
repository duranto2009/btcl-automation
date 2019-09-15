package location;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import org.apache.poi.util.SystemOutLogger;
import requestMapping.RequestParameter;
import util.TransactionType;

import java.util.List;

public class DivisionService {

    @DAO
    DivisionDAO divisionDAO;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Division> getDivision(long id) throws Exception {

        List<Division> divisionLIst= divisionDAO.getDivision(id);
        if(divisionLIst == null) {
            throw new RequestFailureException("No Division found ");
        }

        return divisionLIst;
    }
}
