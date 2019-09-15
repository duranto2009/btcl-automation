package report;

import common.RequestFailureException;
import util.SqlPair;

public interface SubqueryBuilder {
	String createSubquery(String value, Integer... moduleID) throws RequestFailureException;
}
