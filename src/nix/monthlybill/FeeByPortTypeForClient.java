package nix.monthlybill;
import lombok.Data;
@Data
public class FeeByPortTypeForClient {


	String portType;
	int portCount;
	double portCost;

}
