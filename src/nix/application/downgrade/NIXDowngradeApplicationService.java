package nix.application.downgrade;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;

import java.util.List;

public class NIXDowngradeApplicationService {

    @DAO
    NIXDowngradeApplicationDAO nixDowngradeApplicationDAO;

    @Transactional
    public void insert(NIXDowngradeApplication nixDowngradeApplication) throws Exception{
        nixDowngradeApplicationDAO.insert(nixDowngradeApplication);
    }

    @Transactional
    public NIXDowngradeApplication getApplicationByParent(long id)throws Exception {
        return nixDowngradeApplicationDAO.getApplicationByParent(id)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Downgrade Application Found with parent id " + id));
    }
}
