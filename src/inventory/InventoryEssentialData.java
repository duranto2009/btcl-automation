package inventory;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InventoryEssentialData {
    long districtId, popId, rsId, portId, vlanId;
}
