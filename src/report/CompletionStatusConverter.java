package report;

import request.RequestStatus;

public class CompletionStatusConverter implements ColumnConvertor{
	
	public String convert(Object status) {
		return (String) RequestStatus.reqStatusMap.get((Integer)status);//CommonDAO.getActivationStatusNameStatic((Integer) status);
	}

}
