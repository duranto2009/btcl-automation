package coLocation.accounts.commonCost;

import coLocation.accounts.VariableCost.VariableCostDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AllVariableUnitCharge {
    VariableCostDTO rackCharge;
    VariableCostDTO powerCharge;
    VariableCostDTO fiberCharge;
    VariableCostDTO floorSpaceCharge;
    public double getTotalCost() {
        return Stream.of(rackCharge, fiberCharge, powerCharge, floorSpaceCharge)
                .mapToDouble(VariableCostDTO::getPrice)
                .sum();
    }
}
