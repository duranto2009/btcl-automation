package location;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import util.TransactionType;

import java.util.List;

public class AreaService {

    @DAO
    AreaDAO areaDAO;

    @Transactional(transactionType=TransactionType.READONLY)
    public List<Area> getArea(long zone) throws Exception {
        List<Area> areas= areaDAO.getArea(zone);
        if(areas == null) {
            throw new RequestFailureException("No Division found ");
        }

        return areas;
    }
}
