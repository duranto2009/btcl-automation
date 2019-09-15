package util;

import login.LoginDTO;

import java.util.Collection;
import java.util.Hashtable;

public interface NavigationService {
	Collection getIDs(LoginDTO loginDTO, Object... objects ) throws Exception;
	Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects ) throws Exception;
	Collection getDTOs(Collection recordIDs, Object... objects ) throws Exception;
}