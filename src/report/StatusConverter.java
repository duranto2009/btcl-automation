package report;

import common.CommonDAO;

public class StatusConverter implements ColumnConvertor{
	
	public String convert(Object status) {
		return status == null ? "Not Found" : (String) CommonDAO.getActivationStatusNameStatic((Integer) status);
	}

}
