package nix.application.close;

import annotation.DAO;
import annotation.Transactional;
import common.RequestFailureException;

public class NIXCloseApplicationService {
    @DAO
    NIXCloseApplicationDAO nixCloseApplicationDAO;

    @Transactional
    public void insert(NIXCloseApplication nixCloseApplication)throws Exception{
        nixCloseApplicationDAO.insert(nixCloseApplication);
    }

    @Transactional
    public NIXCloseApplication getApplicationByParent(long id)throws Exception {
        return nixCloseApplicationDAO.getApplicationByParent(id)
                .stream()
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Parent Application Found for Close Application, parent app Id: " + id));
    }
}
