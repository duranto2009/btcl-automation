package nix.application.upgrade;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;
import nix.application.upgrade.NIXUpgradeApplicationDAO;
import nix.application.upgrade.NIXUpgradeApplication;

public class NIXUpgradeApplicationService {

    @DAO
    NIXUpgradeApplicationDAO nixUpgradeApplicationDAO;

    @Transactional
    public void insert(NIXUpgradeApplication nixUpgradeApplication)throws Exception {
        nixUpgradeApplicationDAO.insert(nixUpgradeApplication);
    }

    @Transactional
    public NIXUpgradeApplication getApplicationByParent(long id)throws Exception {
        return  nixUpgradeApplicationDAO.getApplicationByParent(id)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Upgrade Application Found by parent application id " + id));
    }
}
