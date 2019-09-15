package lli.Application;

import common.client.ClientDTO;
import lli.Application.EFR.EFR;
import user.UserDTO;

import java.util.List;

public class WorkOrderDTO {
    UserDTO userDTO;
    LLIApplication lliApplication;
    ClientDTO clientDTO;
    List<EFR> efrs;
    List<UserDTO> vendors;

    public WorkOrderDTO() {
    }

    public WorkOrderDTO(UserDTO userDTO, LLIApplication lliApplication, ClientDTO clientDTO, List<EFR> efrs, List<UserDTO> vendors) {
        this.userDTO = userDTO;
        this.lliApplication = lliApplication;
        this.clientDTO = clientDTO;
        this.efrs = efrs;
        this.vendors = vendors;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public LLIApplication getLliApplication() {
        return lliApplication;
    }

    public void setLliApplication(LLIApplication lliApplication) {
        this.lliApplication = lliApplication;
    }

    public ClientDTO getClientDTO() {
        return clientDTO;
    }

    public void setClientDTO(ClientDTO clientDTO) {
        this.clientDTO = clientDTO;
    }

    public List<EFR> getEfrs() {
        return efrs;
    }

    public void setEfrs(List<EFR> efrs) {
        this.efrs = efrs;
    }

    public List<UserDTO> getVendors() {
        return vendors;
    }

    public void setVendors(List<UserDTO> vendors) {
        this.vendors = vendors;
    }
}
