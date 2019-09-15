package ip;

import common.ServiceDAO;
import util.ServiceDAOFactory;

public class TestingIP {

    public static void main(String[] args) throws Exception {
        ServiceDAOFactory.getService(IPService.class).insertNewSubRegion(10, "test_sub_region");
    }
}
