package lli.adviceNote;

import annotation.TableName;
import api.ClientAPI;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillConstants;
import common.pdf.PdfMaterial;
import ip.MethodReferences;
import lli.Application.ReviseClient.ReviseDTO;
import lli.Application.ReviseClient.ReviseService;
import lli.connection.LLIConnectionConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nix.constants.NIXConstants;
import nix.revise.NIXReviseDTO;
import nix.revise.NIXReviseService;
import officialLetter.OfficialLetter;
import officialLetter.OfficialLetterConcern;
import officialLetter.OfficialLetterService;
import officialLetter.ReferType;
import permission.PermissionRepository;
import role.RoleDTO;
import user.UserDTO;
import user.UserRepository;
import util.KeyValuePair;
import util.ServiceDAOFactory;
import util.TimeConverter;
import vpn.client.ClientDetailsDTO;
import vpn.clientContactDetails.ClientContactDetailsDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("official_letter")
@NoArgsConstructor
public class TDAdviceNote extends ClientAdviceNote implements PdfMaterial {

    public TDAdviceNote ( long appId, long clientId ) {
        super(appId, clientId);
    }

    public TDAdviceNote (OfficialLetter ol) {
        setId(ol.getId());
        setModuleId(ol.getModuleId());
        setClientId(ol.getClientId());
        setApplicationId(ol.getApplicationId());
        setOfficialLetterType(ol.getOfficialLetterType());
        setClassName(ol.getClassName());
        setCreationTime(ol.getCreationTime());
        setLastModificationTime(ol.getLastModificationTime());
        setDeleted(ol.isDeleted());
    }

    @Override
    public Map<String, Object> getPdfParameters() throws Exception {
        Map<String, Object> map = new HashMap<>();
        populatePdfParameters(map);
        return map;
    }

    @Override
    public String getResourceFile() {
        //change it
        return BillConstants.LLI_CONNECTION_ADVICE_NOTE;
    }

    public void populatePdfParameters(Map<String, Object> map) throws Exception {
        populateCommonParameters(map);
        populateClientParameters(map);
        if(this.moduleId == ModuleConstants.Module_ID_NIX){
            NIXReviseDTO reviseDTO = ServiceDAOFactory.getService(NIXReviseService.class).getappById(this.getApplicationId());
            map.put("application_type", NIXConstants.nixapplicationTypeNameMap.get(reviseDTO.getApplicationType()));
        }
        else {
            ReviseDTO reviseDTO = ServiceDAOFactory.getService(ReviseService.class).getappById(this.getApplicationId());
            map.put("application_type", LLIConnectionConstants.applicationTypeNameMap.get(reviseDTO.getApplicationType()));
        }


    }
    private void populateCommonParameters(Map<String, Object> map) throws Exception {
        map.put("application_id", Long.toString(this.getApplicationId()));

        map.put("btcl_heading_advice_note", "../../assets/images/btcl_logo_heading_advice_note.png");
        map.put("advice_note_number", String.valueOf(this.getId()));
        map.put("date", TimeConverter.getDateTimeStringByMillisecAndDateFormat(this.getCreationTime(), "dd/MM/yyyy"));
        List<OfficialLetterConcern> list = ServiceDAOFactory.getService(OfficialLetterService.class).getRecipientListByOfficialLetterId(this.getId());
        OfficialLetterConcern toDesignation = list.stream().filter(t->t.getReferType()== ReferType.TO).findFirst().orElse(null);
        if(toDesignation == null){
            throw new RequestFailureException("NO TO Designation Found");
        }
        UserDTO user = UserRepository.getInstance().getUserDTOByUserID(toDesignation.getRecipientId());
        if(user == null){
            throw new RequestFailureException("No user found for id " + toDesignation.getRecipientId() );
        }
        RoleDTO roleDTO = PermissionRepository.getInstance().getRoleDTOByRoleID(user.getRoleID());
        if(roleDTO == null ){
            throw new RequestFailureException("No Role found for user id " + toDesignation.getRecipientId() );
        }
        String roleName = roleDTO.getRoleName();

        map.put("recipient", "Server Room");
        List<OfficialLetterConcern> ccList = list.stream().filter(t->t.getReferType()==ReferType.CC).collect(Collectors.toList());

        List<UserDTO> ccUsers  = ccList.stream().map(t->UserRepository.getInstance().getUserDTOByUserID(t.getRecipientId())).collect(Collectors.toList());
        AtomicInteger atomicInteger = new AtomicInteger(0);
        List<KeyValuePair<String, String>>ccListKeyValuePair = ccUsers.stream()
                .map(t->{
                    atomicInteger.getAndIncrement();
                    return MethodReferences.newKeyValueString_String.apply(atomicInteger.toString() + ". " + t.getFullName() + ",", t.getDesignation()+", " + t.getDepartmentName());
                })
                .collect(Collectors.toList());
        map.put("CC", ccListKeyValuePair);
        UserDTO sender = UserRepository.getInstance().getUserDTOByUserID(toDesignation.getSenderId());
        map.put("sender_name", sender.getFullName());
        map.put("sender_designation", sender.getDesignation() + ", " + user.getDepartmentName());
    }
    private void populateClientParameters(Map<String, Object> map)  throws Exception {
        KeyValuePair<ClientDetailsDTO, ClientContactDetailsDTO> pair =
                ClientAPI.getInstance().getPairOfClientDetailsAndClientContactDetails(
                        this.getClientId(), this.getModuleId(), ClientContactDetailsDTO.BILLING_CONTACT);
        ClientDetailsDTO clientDetailsDTO = pair.key;
        ClientContactDetailsDTO contactDetailsDTO = pair.value;

        map.put("client_name", clientDetailsDTO.getName());
        map.put("username", clientDetailsDTO.getLoginName() );
        map.put("mobile_number", contactDetailsDTO.getPhoneNumber());
        //these two needs modification
        //defaults used for now
        map.put("client_type", "Govt");
        map.put("client_category", "ISP");
        // ends here
        map.put("client_billing_address",contactDetailsDTO.getAddress());
    }
}