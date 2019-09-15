package officialLetter;

import common.ClientDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import user.UserDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecipientElement {

    long ID;
    String label;
    RecipientType recipientType;
    ReferType referType;

    RecipientElement(UserDTO userDTO){
        this.ID = userDTO.getUserID();
        this.label = userDTO.getUsername();
        this.recipientType = userDTO.isBTCLPersonnel() ? RecipientType.BTCL_OFFICIAL : RecipientType.VENDOR;

    }
    RecipientElement(ClientDTO userDTO){
        this.ID = userDTO.getClientID();
        this.label = userDTO.getLoginName();
        this.recipientType = RecipientType.CLIENT;
    }

    public static RecipientElement getRecipientElementFromUser(UserDTO userDTO) {
        return new RecipientElement(userDTO);
    }

    public static RecipientElement getRecipientElementFromUserAndReferType(UserDTO user, ReferType referType){
        RecipientElement recipientElement = new RecipientElement(user);
        recipientElement.setReferType(referType);
        return recipientElement;
    }
    public static RecipientElement getRecipientElementFromClientAndReferType(ClientDTO client, ReferType referType){
        RecipientElement recipientElement = new RecipientElement(client);
        recipientElement.setReferType(referType);
        return recipientElement;
    }
}
