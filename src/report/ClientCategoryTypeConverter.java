package report;

import vpn.client.ClientForm;

public class ClientCategoryTypeConverter implements ColumnConvertor{
	
	public String convert(Object status) {
		return (String) ClientForm.CLIENT_TYPE_STR.get(status);
	}
}
