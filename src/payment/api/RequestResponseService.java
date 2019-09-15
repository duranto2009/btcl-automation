package payment.api;

import annotation.Transactional;
import util.ModifiedSqlGenerator;

public class RequestResponseService {
	@Transactional
	public void insert(RequestResponseHistory requestResponseHistory) throws Exception{
		ModifiedSqlGenerator.insert(requestResponseHistory);
	}
}
