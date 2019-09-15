package entity.facade;

import annotation.Transactional;
import application.ApplicationService;
import common.ModuleConstants;
import common.RequestFailureException;
import common.pdf.AsyncPdfService;
import entity.efr.EFR;
import entity.efr.EFRConditionBuilder;
import entity.office.Office;
import exception.NoDataFoundException;
import global.GlobalService;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import officialLetter.OfficialLetterService;
import officialLetter.RecipientElement;
import requestMapping.Service;
import vpn.application.VPNApplication;
import vpn.application.VPNApplicationLink;
import vpn.workOrder.VPNWorkOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j
public class WorkOrderGenerationFacade {

    @Service private GlobalService globalService;
    @Service private ApplicationService applicationService;
    @Service private OfficialLetterService officialLetterService;

    @SuppressWarnings("unchecked")
    @Transactional
    public void generateGenericWorkOrder (int moduleId, LoginDTO loginDTO, Object... objects) throws Exception {
        if(moduleId == ModuleConstants.Module_ID_VPN) {
            if(objects == null ||  objects.length != 3){
                throw new RequestFailureException("Invalid Parameters Found to generate VPN Work Order; " +
                        "Try invoking method with List<VPNApplicationLink> as 3rd Param." +
                        " Application ID as 4th Param." + " Client ID as 5th Param."
                        );
            }
            generateVPNWorkOrder((List<VPNApplicationLink>)objects[0], (Long)objects[1], (Long)objects[2], loginDTO);
        }
    }

    /***
     *          collect local office based efr into a list
     *          collect remote office based efr into a list
     *          merge them
     *          group the merged list into a vendor wise map
     *          for each entry of the map generate work order
     * @param links List<VPNApplicationLinks>
     */
    private void generateVPNWorkOrder(List<VPNApplicationLink> links, long appId, long clientId, LoginDTO loginDTO) throws Exception {
//        log.info("");
        List<Long> officeIds = getDistinctOfficeIds(links);
        if(officeIds.isEmpty())return;
        List<EFR> efrs = getSelectedEFRs(officeIds);

        Map<Long, List<EFR>> mapOfSelectedEFRsToVendorId = efrs.stream()
                .collect(
                        Collectors.groupingBy(EFR::getVendorID)
                );
//        Application application = applicationService.getApplicationByApplicationId(links.get(0).getVpnApplicationId());

        log.info("Generating All Work Orders");
        mapOfSelectedEFRsToVendorId.entrySet()
                .forEach(t-> {
                    try {
                        generateVPNWorkOrder(appId, clientId, loginDTO, t);
                    } catch (Exception e) {
                        log.fatal(e.getMessage());
                        e.printStackTrace();
                    }
                });
        log.info("Done All Work Orders");
    }

    private void generateVPNWorkOrder(long appId, long clientId, LoginDTO loginDTO, Map.Entry<Long, List<EFR>> entry) throws Exception {

        long vendorId = entry.getKey();
        List<EFR> selectedEFRs = entry.getValue();
        log.info("Generating work order for vendor id " + vendorId);

        VPNWorkOrder vpnWorkOrder = new VPNWorkOrder(appId, clientId, vendorId, selectedEFRs );
        List<RecipientElement> recipientElements = officialLetterService.getWorkOrderSpecificRecipientElement(vendorId, clientId, loginDTO.getUserID());
        officialLetterService.saveOfficialLetterTransactionalIndividual(vpnWorkOrder,recipientElements, loginDTO.getUserID());
        selectedEFRs.forEach(t->{
            t.setWorkOrderNumber(vpnWorkOrder.getId());
            globalService.update(t);

        });
        AsyncPdfService.getInstance().accept(vpnWorkOrder);
        log.info("Done work order for vendor id " + vendorId);
    }


    /***
     *
     * @param officeIds parameter Long representing office id
     * @return List of Selected EFRs which does not have previous work order
     * @throws NoDataFoundException if no selected EFR found by provided office ids
     */
    public List<EFR> getSelectedEFRs(List<Long> officeIds) throws Exception {
        List<EFR> efrs = globalService.getAllObjectListByCondition(EFR.class,
                new EFRConditionBuilder()
                        .Where()
                        .officeIdIn(officeIds)
                        .isSelected(true)
                        .workOrderNumberIsNull(true)
                        .getCondition()
        );
        if(efrs.isEmpty()) {
            throw new NoDataFoundException("No Selected EFR Found to send work order");
        }

        log.info("Selected EFR From DB");
        efrs.forEach(log::info);
        return efrs;
    }

    /***
     *
     * @param links List of VPNApplicationLinks
     * @return for Each Link it collects remote and local office ids note-> local end office ids are same so we need distinct
     */
    public List<Long> getDistinctOfficeIds(List<VPNApplicationLink> links) {
        List<Long> officeIds=  links.stream()
                .flatMap(link -> Stream.of(link.getLocalOffice(), link.getRemoteOffice()))
//                .peek(System.out::println)
                .filter(Objects::nonNull)
//                .peek(System.out::println)
                .map(Office::getId)
//                .peek(System.out::println)
                .distinct()
//                .peek(System.out::println)
                .collect(Collectors.toList());

        log.info("Eligible Distinct Local and Remote Office Ids");
        officeIds.forEach(log::info);
        return officeIds;
    }
}
