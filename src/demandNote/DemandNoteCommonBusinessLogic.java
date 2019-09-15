package demandNote;

import client.RegistrantTypeConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.repository.AllClientRepository;
import vpn.client.ClientDetailsDTO;

public class DemandNoteCommonBusinessLogic {
    public boolean isSkipable(BillDTO billDTO, int moduleId){

        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(billDTO.getClientID(), moduleId);
        if(clientDetailsDTO==null) {
            throw new RequestFailureException("No client Details found for the corresponding invoice: " +billDTO.getID());
        }
        return billDTO.getPaymentStatus() == BillDTO.UNPAID && clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT;

    }

    public boolean isUnskipable(BillDTO billDTO, int moduleId) {
        ClientDetailsDTO clientDetailsDTO = AllClientRepository.getInstance().getModuleClientByClientIDAndModuleID(billDTO.getClientID(), moduleId);
        if(clientDetailsDTO==null) {
            throw new RequestFailureException ("No client Details found for the corresponding invoice: " + billDTO.getID());
        }
        return billDTO.getPaymentStatus() == BillDTO.SKIPPED && clientDetailsDTO.getRegistrantType() == RegistrantTypeConstants.GOVT;

    }

    public void canBeCancelled(BillDTO billDTO) {
        if(billDTO.getPaymentStatus() != BillDTO.UNPAID){
            throw new RequestFailureException("Demand note with invoice ID "
                    +billDTO.getID()+" can not be cancelled");
        }
    }

}
