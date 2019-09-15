package vpn.clientContactDetails;

import annotation.DAO;
import annotation.Transactional;

public class ClientContactDetailService {
    @DAO
    ClientContactDetailDAO clientContactDetailDAO;

    @Transactional
    public void updateClientContactDetails(ClientContactDetailsDTO clientContactDetailsDTO)throws Exception{
        clientContactDetailDAO.updateClientContactDetails(clientContactDetailsDTO);
    }
}
