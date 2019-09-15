package ip.IPInventory;

import annotation.DAO;
import annotation.Transactional;
import common.ModuleConstants;
import ip.*;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import util.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Raihan on 10/14/2018.
 */
@Log4j
public class IPInventoryService implements NavigationService {

    @DAO
    IPInventoryDAO ipInventoryDAO;



    private void validateIPBlockInventory(IPBlockInventory block) throws Exception {
        //TODO
        //region check
        //from address validity
        //to address validity
        //range validity i.g. from <= to
        //overlap check region independant

    }

    @Transactional
    public void saveIPBlockInventory(IPBlockInventory block) throws Exception{
        validateIPBlockInventory(block);
        ipInventoryDAO.saveIPBlockInventory(block);
    }

    //Test
    public void insert() throws Exception {
        long currentTime = System.currentTimeMillis();
        IPBlockInventory block = new IPBlockInventory();
        block.setRegionId(3);
        block.setFromIP("127.0.0.1");
        block.setToIP("127.0.0.255");
//        block.setType(IPConstants.Type.PRIVATE);
//        block.setVersion(IPConstants.Version.IPv4);
        block.setCreationTime(currentTime);
        block.setLastModificationTime(currentTime);
        block.setActiveFrom(currentTime);
        CurrentTimeFactory.initializeCurrentTimeFactory();
        ServiceDAOFactory.getService(IPInventoryService.class).saveIPBlockInventory(block);
        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    //Test
    @Transactional(transactionType = TransactionType.READONLY)
    public void getOne() throws Exception {
        IPBlockInventory block = ipInventoryDAO.getOneById(6001);
        System.out.println(block.toString());
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlockInventory> getAvailableIPBlocksByCriteria(long regionId, IPConstants.Version version, IPConstants.Type type, int blockSize, int moduleId) throws Exception {
        // get valid ipBlocks from inventory i.e. is_deleted = false, active_to = Long.Max_value with specified regionId; Set S
        // get used block from ip_usage. Set U
        // return S - U
        Predicate<IPBlockInventory> greaterThanSpecifiedBlockSize = (i)-> {
            try {
                return IPUtility.ipv4ToLong(i.getToIP()) - IPUtility.ipv4ToLong(i.getFromIP()) + 1 >= blockSize;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
        return ipInventoryDAO.getAllValidIPBlocksFromInventoryByCriteria(regionId, version, type, moduleId)
                .stream()
                .filter(greaterThanSpecifiedBlockSize)
                .collect(Collectors.toList());
    }

    public static void main(String [] args) throws Exception{
        ServiceDAOFactory.getService(IPInventoryService.class)
                .getInventoryIPBlocks(6L, IPConstants.Version.IPv4, IPConstants.Type.PUBLIC, ModuleConstants.Module_ID_LLI);

    }


    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Override
    @Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return ipInventoryDAO.getIDsWithSearchCriteria(searchCriteria);
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return ipInventoryDAO.getAllInventoriesByIds((List<Long>)recordIDs);
    }
    @Transactional(transactionType = TransactionType.READONLY)
     public List<KeyValuePair<IPBlock, String>> getInventoryIPBlocks(long regionId, IPConstants.Version version, IPConstants.Type type, int moduleID) throws Exception {
        //default value of block size first level suggestion is currently 256
        List<IPBlockInventory> list = ipInventoryDAO.getAllValidIPBlocksFromInventoryByCriteria(regionId, version, type, moduleID);
        List<KeyValuePair<IPBlock, String>> listOfBlocks = new ArrayList<>();
        for(IPBlockInventory block: list){
            listOfBlocks.addAll(getStandardIPBlocksByOctetNumber(block, 2));
        }
        listOfBlocks.forEach(log::info);
        return listOfBlocks;
    }

    private static List<KeyValuePair<IPBlock, String>> getStandardIPBlocksByOctetNumber(IPBlockInventory block, int number) {
        List<KeyValuePair<IPBlock, String>>list = new ArrayList<>();
        try {

            boolean isSameOctet = IPUtility.isSameOctet(block.getFromIP(), block.getToIP(), number);
            if(isSameOctet) {
                list.add(new KeyValuePair<>(
                        new IPBlock(block.getFromIP(), block.getToIP()),
                        block.getRemarks()
                ));
            }else{
                long lastInABlock = IPUtility.getLastIPLong(block.getFromIP());
                long toIPLong = IPUtility.ipv4ToLong(block.getToIP());
                String firstIP = block.getFromIP();
                while(lastInABlock < toIPLong){
                    list.add(
                            new KeyValuePair<>(
                                    new IPBlock(firstIP, IPUtility.longToipv4String(lastInABlock)),
                                    block.getRemarks()
                            ));
                    lastInABlock ++;
                    firstIP = IPUtility.longToipv4String(lastInABlock);
                    lastInABlock += 255;
                }
                list.add(
                        new KeyValuePair<>(
                                new IPBlock(firstIP, IPUtility.longToipv4String(toIPLong)),
                                block.getRemarks()
                        ));
            }
            return list;
        }catch (Exception e){
            log.fatal(e.getMessage());
        }
        return list;

    }
}
