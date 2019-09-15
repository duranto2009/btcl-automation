package ip;

import annotation.Transactional;
import com.google.gson.JsonArray;
import common.ObjectPair;
import common.RequestFailureException;
import ip.IPInventory.IPBlockInventory;
import ip.IPInventory.IPInventoryService;
import ip.ipRouting.IPRoutingInfo;
import ip.ipRouting.IPRoutingService;
import ip.ipUsage.IPBlockUsage;
import ip.ipUsage.IPUsageService;
import ip.ipVsLLIConnection.IPvsConnection;
import ip.ipVsLLIConnection.IPvsConnectionService;
import ip.ipVsNIXConnection.NIXIPvsConnection;
import ip.ipVsNIXConnection.NIXIPvsConnectionService;
import ip.privateIP.PrivateIPBlock;
import ip.privateIP.PrivateIPService;
import ip.subRegion.IPSubRegion;
import ip.subRegion.IPSubRegionService;
import lli.connection.LLIConnectionConstants;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class IPService {

    @Service
    IPUsageService ipUsageService;

    @Service
    IPRoutingService ipRoutingService;

    @Service
    IPvsConnectionService iPvsConnectionService;
    @Service
    NIXIPvsConnectionService nixiPvsConnectionService;

    @Service
    IPInventoryService ipInventoryService;

    @Service
    IPSubRegionService ipSubRegionService;

    @Service
    PrivateIPService privateIPService;


    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlockUsage> getIPBlockUsageByConnectionId(long connectionId) throws Exception {
        List<IPvsConnection> connections = iPvsConnectionService.getByConnectionId(connectionId);
        List<Long> usageIds = connections.stream().map(IPvsConnection::getIpUsageId).collect(Collectors.toList());
        return ipUsageService.getUsageBlocksByUsageIDs(usageIds);

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlock> getIPBlockListByConnectionId(long connectionId) throws Exception {
        return getIPBlockUsageByConnectionId(connectionId)
                .stream()
                .map(t -> new IPBlock(t.getFromIP(), t.getToIP()))
                .collect(Collectors.toList());

    }


    public JsonArray getIPBlocksByConnectionId(long connectionId) throws Exception {
        return iPvsConnectionService.getIPBlocksByConnectionId(connectionId);
    }

    public IPRoutingInfo getIPRoutingSuggestion(long fromIP, long toIP) {
        IPRoutingInfo routingInfo = ipRoutingService.getIPRoutingByIPRange(fromIP, toIP);
        log.info("routingInfo\n" + routingInfo);
        return routingInfo;
    }

    @Transactional
    public void save(IPBlockUsage ipBlockUsage, IPRoutingInfo routingInfo) throws Exception {
        ipUsageService.saveIPBlockUsage(ipBlockUsage);
        log.info("Saved in usage table");
        ipRoutingService.saveIPRoutingInfo(routingInfo);
        log.info("Saved in routing info table");
        IPvsConnection ipVsConnection = IPvsConnection
                .builder()
                .connectionId(1L)
                .ipUsageId(ipBlockUsage.getId())
                .routingInfoId(routingInfo.getId())
                .connectionId(1L)
                .creationTime(CurrentTimeFactory.getCurrentTime())
                .activeFrom(CurrentTimeFactory.getCurrentTime())
                .usageType(LLIConnectionConstants.IPUsageType.MANDATORY)
                .build();
        iPvsConnectionService.save(ipVsConnection);
        log.info("Saved in ip_vs_connection");

    }

    @Transactional
    public void allocateIPAddress(List<IPBlockForConnection> blocks) {
        blocks.forEach(block -> {
            try {
                allocateIPAddress(block);
            } catch (Exception e) {
                log.fatal(e.getMessage());
                log.fatal("Error saving block " + block.toString());
                throw new RequestFailureException("Error saving block " + block.getFromIP() + " - " + block.getToIP());
            }
        });
    }

    @Transactional
    public void allocateNIXIPAddress(List<NIXIPBlockForConnection> blocks) {
        blocks.forEach(block -> {
            try {
                allocateNIXIPAddress(block);
            } catch (Exception e) {
                log.fatal(e.getMessage());
                log.fatal("Error saving block " + block.toString());
                throw new RequestFailureException("Error saving block " + block.getFromIP() + " - " + block.getToIP());
            }
        });
    }

    private void allocateIPAddress(IPBlockForConnection block) throws Exception {
        long currentTime = CurrentTimeFactory.getCurrentTime();
        IPBlockUsage ipBlockUsage = IPBlockUsage
                .builder()
                .fromIP(block.getFromIP())
                .toIP(block.getToIP())
                .version(block.getVersion())
                .type(block.getType())
                .purpose(block.getPurpose())
                .regionId(block.getRegionId())
                .subRegionId(block.getSubRegionId())
                .creationTime(currentTime)
                .activeFrom(currentTime)
                .build();
        ipUsageService.saveIPBlockUsage(ipBlockUsage);
        IPvsConnection ipVsConnection = IPvsConnection
                .builder()
                .connectionId(block.getConnectionId())
                .ipUsageId(ipBlockUsage.getId())
                .routingInfoId(0L)
                .creationTime(currentTime)
                .activeFrom(currentTime)
                .usageType(block.getUsageType())
                .build();
        iPvsConnectionService.save(ipVsConnection);

    }

    private void allocateNIXIPAddress(NIXIPBlockForConnection block) throws Exception {
        long currentTime = CurrentTimeFactory.getCurrentTime();
        IPBlockUsage ipBlockUsage = IPBlockUsage
                .builder()
                .fromIP(block.getFromIP())
                .toIP(block.getToIP())
                .version(block.getVersion())
                .type(block.getType())
                .purpose(block.getPurpose())
                .regionId(block.getRegionId())
                .subRegionId(block.getSubRegionId())
                .creationTime(currentTime)
                .activeFrom(currentTime)
                .build();
        ipUsageService.saveIPBlockUsage(ipBlockUsage);
        NIXIPvsConnection ipVsConnection = NIXIPvsConnection
                .builder()
                .connectionId(block.getConnectionId())
                .ipUsageId(ipBlockUsage.getId())
                .routingInfoId(0L)
                .creationTime(currentTime)
                .activeFrom(currentTime)
                .usageType(block.getUsageType())
                .build();
        nixiPvsConnectionService.save(ipVsConnection);

    }

    List<IPBlock> getAvailableIPBlock(IPBlock block, int blockSize) throws Exception {
        log.info("suggestion: 2nd Level");
        List<IPBlock> output = ipUsageService.getAvailableIPBlock(block, blockSize);
        if(output.isEmpty()){
            throw new RequestFailureException("All IPs from " + block.fromIP + " - " + block.toIP + " are used");
        }
        return output;
    }

    public void insertNewSubRegion(long regionId, String subRegionName) throws Exception {
        ipSubRegionService.insertNewSubRegion(regionId, subRegionName);
    }

    public void saveIPBlockInventory(IPBlockInventory block) throws Exception {
        long currentTime = CurrentTimeFactory.getCurrentTime();
        block.setActiveFrom(currentTime);
        block.setCreationTime(currentTime);
        ipInventoryService.saveIPBlockInventory(block);
    }

    public List<KeyValuePair<IPBlock, String>> getIPBlockSuggestion(FreeIPBlockParams params) throws Exception {
        return ipInventoryService.getInventoryIPBlocks(params.getRegionId(), params.getVersion(), params.getType(), params.getModuleId());
    }


    public void addNewPrivateIP(PrivateIPBlock block) throws Exception {
        long currentTime = CurrentTimeFactory.getCurrentTime();
        block.setActiveFrom(currentTime);
        block.setCreationTime(currentTime);
        privateIPService.addNewPrivateIP(block);
    }

    @Transactional
    public void deallocateIPsByConnectionId(long connectionId) throws Exception {
        //TODO raihan if necessary : do validation
        List<IPvsConnection> iPvsConnections = iPvsConnectionService.getIPVsConnectionByConnectionId(connectionId);
        iPvsConnections.forEach(t -> {
            t.setDeleted(true);
            t.setActiveTo(System.currentTimeMillis());

        });
        iPvsConnections.forEach(iPvsConnectionService::update);

        iPvsConnections.forEach(t -> ipUsageService.cancelUsageByUsageId(t.getIpUsageId()));

    }

    @Transactional
    public void deallocateIPsByUsageId(long usageId) throws Exception {
        //TODO raihan if necessary : do validation
        ipUsageService.cancelUsageByUsageId(usageId);
        IPvsConnection iPvsConnection = iPvsConnectionService.getIPVsConnectionByUsageId(usageId);
        if (iPvsConnection != null) {
            iPvsConnection.setDeleted(true);
            iPvsConnection.setLastModificationTime(System.currentTimeMillis());
            iPvsConnectionService.update(iPvsConnection);
        }
    }

    public List<IPSubRegion> getAllSubRegions() throws Exception {
        return ipSubRegionService.getAllSubRegions();
    }

    public List<IPSubRegion> getAllSubRegionsByRegionId(long regionId) throws Exception {
        return ipSubRegionService.getAllSubRegionsByRegionId(regionId);
    }


    public static void main(String[] args) throws Exception {
        CurrentTimeFactory.initializeCurrentTimeFactory();
        ServiceDAOFactory.getService(IPService.class).getIPBlockUsageByConnectionId(110001L).forEach(log::info);

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlockUsage> getIPBlockUsageByConnectionIdAndUsageType(long connectionId, LLIConnectionConstants.IPUsageType usageType, IPConstants.Purpose purpose) throws Exception {
        if (purpose == IPConstants.Purpose.LLI_CONNECTION) {
            List<IPvsConnection> iPvsConnections = iPvsConnectionService.getByConnectionId(connectionId)
                    .stream()
                    .filter(t -> t.getUsageType() == usageType)
                    .collect(Collectors.toList());
            return iPvsConnections.isEmpty() ?
                    Collections.emptyList() :
                    ipUsageService.getUsageBlocksByUsageIDs(
                            iPvsConnections.stream()
                                    .map(IPvsConnection::getIpUsageId)
                                    .collect(Collectors.toList())
                    );
        }
        else if( purpose == IPConstants.Purpose.NIX_CONNECTION){
            List<NIXIPvsConnection> iPvsConnections = ServiceDAOFactory.getService(NIXIPvsConnectionService.class).getByConnectionId(connectionId)
                    .stream()
                    .filter(t -> t.getUsageType() == usageType)
                    .collect(Collectors.toList());
            return iPvsConnections.isEmpty() ?
                    Collections.emptyList() :
                    ipUsageService.getUsageBlocksByUsageIDs(
                            iPvsConnections.stream()
                                    .map(NIXIPvsConnection::getIpUsageId)
                                    .collect(Collectors.toList())
                    );
        }
        return null;
    }

    /***
     * save ipBlockUsage DTO in DB
     * @param usage : IPBlockUsage
     *
     */
    @Transactional
    public void saveIPBlockUsage(IPBlockUsage usage) throws Exception {
        usage.setCreationTime(CurrentTimeFactory.getCurrentTime());
        usage.setActiveFrom(CurrentTimeFactory.getCurrentTime());
        ipUsageService.saveIPBlockUsage(usage);

    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<ObjectPair<String, String>> getAllSubRegionsWithParentRegion() throws Exception {
        return ipSubRegionService.getAllSubRegionsWithParentRegion();
    }
}
