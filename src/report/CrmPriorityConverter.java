package report;

import crm.CrmComplainDTO;

public class CrmPriorityConverter implements ColumnConvertor{
	
	public String convert(Object priority) {
		return (String) CrmComplainDTO.mapComplainPriorityStringToPriorityID.get((Integer )priority);
	}

}
