package officialLetter;

import annotation.DAO;
import annotation.Transactional;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import common.ClientDTO;
import common.ModuleConstants;
import common.RequestFailureException;
import common.bill.BillDTO;
import common.pdf.PdfMaterial;
import common.repository.AllClientRepository;
import flow.FlowService;
import global.GlobalService;
import ip.MethodReferences;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import user.UserDTO;
import user.UserRepository;
import user.UserService;
import util.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class OfficialLetterService implements NavigationService {

    @DAO private OfficialLetterDAO officialLetterDAO;

    @Service private UserService userService;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<OfficialLetterConcern> getRecipientListByOfficialLetterId(long id) throws  Exception{
        return officialLetterDAO.getOfficialConcernsByOfficialLetterId(id);
    }

    @Transactional
    public<T extends BillDTO & PdfMaterial> void saveDemandNoteAsOfficialLetter(Class<T> classObject, int moduleId, long appId, long clientId, long senderId) throws Exception {
        OfficialLetter officialLetter = getOfficialLetter(classObject, OfficialLetterType.DEMAND_NOTE, moduleId, appId, clientId);
        List<RecipientElement> recipientElements = getDemandNoteSpecificRecipientElement(clientId, senderId);
        saveOfficialLetter(officialLetter, recipientElements, senderId);
    }

    private RecipientElement getRecipientElementByIdAndReferType(long id, ReferType referType, boolean isClient) throws Exception {
        if(isClient){
            ClientDTO clientDTO = AllClientRepository.getInstance().getClientByClientID(id);
            return RecipientElement.getRecipientElementFromClientAndReferType(clientDTO, referType);
        }else {

            if(id == -1) {
                // System
                return null;
            }else {
                // Must be user
                UserDTO userDTO = UserRepository.getInstance().getUserDTOByUserID(id);
                return RecipientElement.getRecipientElementFromUserAndReferType(userDTO, referType);
            }
        }
    }

    /**
     *
     * @param clientId TO
     * @param senderId CC, CDGM CC
     * @return List of RecipientElement
     * @throws Exception
     */
    public List<RecipientElement> getDemandNoteSpecificRecipientElement (long clientId, long senderId) throws Exception {
        UserDTO cdgmUser = userService.getCDGMUserDTO();
        RecipientElement client = getRecipientElementByIdAndReferType(clientId, ReferType.TO, true);
        RecipientElement sender = getRecipientElementByIdAndReferType(senderId, ReferType.CC, false);
        if(senderId != cdgmUser.getUserID()) {
            RecipientElement cdgm = getRecipientElementByIdAndReferType(cdgmUser.getUserID(), ReferType.CC, false);
            return Stream.of( client, sender, cdgm).collect(Collectors.toList());
        }else{
            return (sender == null) ? Stream.of(client).collect(Collectors.toList()) : Stream.of(client, sender).collect(Collectors.toList());

        }
    }

    /**
     * @param vendorId TO
     * @param clientId CC
     * @param senderId CC, CDGM CC
     * @return List of RecipientElement
     * @throws Exception
     */
    public List<RecipientElement> getWorkOrderSpecificRecipientElement(long vendorId, long clientId, long senderId) throws Exception {
        UserDTO cdgmUser = userService.getCDGMUserDTO();
        RecipientElement vendor = getRecipientElementByIdAndReferType(vendorId, ReferType.TO, false);
        RecipientElement client = getRecipientElementByIdAndReferType(clientId, ReferType.CC, true);
        RecipientElement sender = getRecipientElementByIdAndReferType(senderId, ReferType.CC, false);
        if(senderId != cdgmUser.getUserID()) {
            RecipientElement cdgm = getRecipientElementByIdAndReferType(cdgmUser.getUserID(), ReferType.CC, false);
            return Stream.of(vendor, client, sender, cdgm).collect(Collectors.toList());
        }else{
            return Stream.of(vendor, client, sender).collect(Collectors.toList());
        }
    }

    @Transactional
    public void saveOfficialLetterTransactionalDefault(OfficialLetter officialLetter, List<RecipientElement> recipientElements, long senderId) throws Exception {
        saveOfficialLetter(officialLetter, recipientElements, senderId);
    }

    @Transactional(transactionType = TransactionType.INDIVIDUAL_TRANSACTION)
    public void saveOfficialLetterTransactionalIndividual(OfficialLetter officialLetter, List<RecipientElement> recipientElements, long senderId) throws Exception {
        saveOfficialLetter(officialLetter, recipientElements, senderId);
    }
    private  void saveOfficialLetter(OfficialLetter officialLetter, List<RecipientElement> recipientElements, long senderId) throws Exception {
        officialLetterDAO.insertOfficialLetter(officialLetter);
        List<OfficialLetterConcern> listOfConcerns = recipientElements.stream()
                .filter(Objects::nonNull)
                .map(t-> this.createDocumentConcern( t, senderId, officialLetter ) )
                .collect(Collectors.toList());

        listOfConcerns.forEach(officialLetterDAO::insertDocumentConcern);
    }



    private OfficialLetterConcern createDocumentConcern(
            RecipientElement recipientElement,
            long senderId,
            OfficialLetter officialLetter
    ) {
        return OfficialLetterConcern.builder()
                .recipientId(recipientElement.getID())
                .recipientType(recipientElement.getRecipientType())
                .senderId(senderId)
                .senderType(this.getSenderType(senderId))
                .referType(recipientElement.getReferType())
                .officialLetterId(officialLetter.getId())
                .build();
    }

    private SenderType getSenderType(long senderId) {
        UserDTO user = null;
        try{
            user = UserRepository.getInstance().getUserDTOByUserID(senderId);
            return user.isBTCLPersonnel()?SenderType.BTCL_OFFICIAL:SenderType.VENDOR;
        }catch (Exception e){
            try {
                ClientDTO client = AllClientRepository.getInstance().getClientByClientID(senderId);
                return SenderType.CLIENT;
            }catch (Exception ex){
                if(senderId == -1){
                    return SenderType.SYSTEM;
                }else {
                    throw new RequestFailureException("Invalid Sender For Official Letter. No user or client found with id " + senderId);
                }
            }
        }
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return officialLetterDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return officialLetterDAO.getDTOs((List<Long>) recordIDs);
    }

    @Transactional (transactionType = TransactionType.READONLY)
    public JsonArray getOfficialLettersByApplicationIdAndModuleId(long appId, int moduleId) throws Exception {
        return officialLetterDAO.getOfficialLettersByApplicationIdAndModuleId(appId, moduleId);
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<OfficialLetter> getOfficialLettersByApplicationIdAndLetterTypeAndModuleId(long id, OfficialLetterType type, int moduleId) throws Exception {
        List<OfficialLetter> list = officialLetterDAO.getOfficialLettersByAppIdAndTypeAndModule(id, type, moduleId);
        if(list.isEmpty()) {
            throw new RequestFailureException("No " + type.name()+" Information found for app id " + id + " module " + ModuleConstants.ActiveModuleMap.getOrDefault(moduleId, "N/A"));
        }
        return list;
    }


    public List<RecipientElement> getCCRecipientElements(JsonArray userArray){
        List<RecipientElement> ccList = new ArrayList<>();
        if (userArray != null) {
            for (JsonElement userElement : userArray) {

                JsonObject object = userElement.getAsJsonObject();
                long id = object.get("ID").getAsLong();
                String name = object.get("label").getAsString();
                RecipientType type = Enum.valueOf(RecipientType.class, object.get("recipientType").getAsString());
                ccList.add(new RecipientElement(id, name, type, ReferType.CC));
            }

        }
        return ccList;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<RecipientElement> getToRecipientElements(int state) {
        return ServiceDAOFactory.getService(FlowService.class).getRolesByNextState(state)
                .stream()
                .flatMap(
                        t-> UserRepository.getInstance()
                                .getUsersByRoleID(t.getId())
                                .stream()

                )
                .map(t-> MethodReferences.newRecipientElement.apply(t, ReferType.TO))
                .collect(Collectors.toList());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public long getSenderByAppIdAndVendorIdAndOfficialLetterType(int applicationId, int vendorId, OfficialLetterType workOrder) throws Exception {
        return officialLetterDAO.getSenderByAppIdAndVendorIdAndOfficialLetterType(applicationId, vendorId, workOrder);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<RecipientElement> getAllCCAndToList(JsonArray userArray, int state) {
        return Stream.concat(getToRecipientElements(state).stream(), getCCRecipientElements(userArray).stream()).collect(Collectors.toList());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public OfficialLetter getWorkOrderByApplicationVendorModule(long appId, long vendorId, int moduleId) throws Exception {
        List<OfficialLetter> officialLetters =
                getOfficialLettersByApplicationIdAndLetterTypeAndModuleId(appId, OfficialLetterType.WORK_ORDER, moduleId);

        OfficialLetterConcern vendorConcern = officialLetters
                .stream()
                .map(t-> getConcernsByOfficialLetterId(t.getId()))
                .flatMap(Collection::stream)
                .filter(workOrderToRecipient())
                .filter(t->t.getRecipientId() == vendorId)
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Work Order Recipient Found for recipient id " + vendorId + " appId " + appId));

       return officialLetters.stream()
                .filter(t->vendorConcern.getOfficialLetterId() == t.getId())
                .findFirst()
                .orElseThrow(()->new RequestFailureException("No Work Order Instance Found for vendor concern id " + vendorConcern.getId()));
    }
    private List<OfficialLetterConcern> getConcernsByOfficialLetterId(long officialLetterId) {
        try {
            return getRecipientListByOfficialLetterId(officialLetterId);
        }catch (Exception e){
            log.fatal(e.getMessage());
            throw new RequestFailureException(e.getMessage());
        }
    }

    public Predicate<OfficialLetterConcern> workOrderToRecipient () {
        return toRecipient().and(ocOrFiberDivision());
    }

    public Predicate<OfficialLetterConcern> toRecipient() {
        return t->t.getReferType() == ReferType.TO;
    }

    public Predicate< OfficialLetterConcern> ocOrFiberDivision() {
        return t->t.getRecipientType() == RecipientType.BTCL_OFFICIAL
                || t.getRecipientType() == RecipientType.VENDOR;
    }


    public Predicate<? super OfficialLetterConcern> ccRecipient() {
        return t-> t.getReferType() == ReferType.CC;
    }

    public OfficialLetter getOfficialLetter(Class<? extends PdfMaterial> classObject, OfficialLetterType type, int moduleId, long appId, long clientId) {
        return OfficialLetter.builder()
                .officialLetterType(type)
                .className(classObject.getCanonicalName())
                .moduleId(moduleId)
                .applicationId(appId)
                .clientId(clientId)
                .creationTime(System.currentTimeMillis())
                .lastModificationTime(System.currentTimeMillis())
                .isDeleted(false)
                .build();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public OfficialLetter getofficialLetterById(long officialLetterId) throws Exception{
        return officialLetterDAO.getOfficialLetterById(officialLetterId);
    }
}
