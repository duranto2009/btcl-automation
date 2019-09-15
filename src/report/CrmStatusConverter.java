package report;

import crm.CrmComplainDTO;

public class CrmStatusConverter implements ColumnConvertor{
	
	public String convert(Object status) {
		return (String) CrmComplainDTO.mapComplainStatusStringToStatusID.get((Integer )status);
	}

}
