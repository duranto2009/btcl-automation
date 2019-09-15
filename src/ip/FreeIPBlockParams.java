package ip;

import lombok.Data;

@Data
public class FreeIPBlockParams {
    long regionId;
    IPConstants.Version version;
    IPConstants.Type type;
    int blockSize;
    int moduleId;
}
