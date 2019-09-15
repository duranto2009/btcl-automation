package lli.asn;

import annotation.DAO;
import annotation.Transactional;
import login.LoginDTO;
import util.NavigationService;

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;

public class ASNService implements NavigationService {
    @DAO
    ASNDAO asnDAO;
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable<>(), loginDTO, objects);
    }
    @Transactional
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return asnDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Transactional
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        List<ASN> list = asnDAO.getLLIApplicationListByIDList((List<Long>) recordIDs);
        return list;
    }
}
