package report;


public class PaymentGatewayConverter implements ColumnConvertor{

	public String convert(Object gatewayType) {
		String gatewayName = "";
		int gateway = (int)gatewayType;
		switch(gateway){
		case 1 : 
			gatewayName = "Teletalk";
			break;
		case 2: 
			gatewayName = "Bank";
			break;
		case 3:
			gatewayName = "SSL";
			break;
		}
		return gatewayName;
	}
}
