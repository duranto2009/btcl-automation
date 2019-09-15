package ip.ipUsage;

import annotation.DAO;
import annotation.Transactional;
import ip.IPBlock;
import ip.IPInventory.IPInventoryService;
import ip.IPUtility;
import ip.MethodReferences;
import login.LoginDTO;
import lombok.extern.log4j.Log4j;
import requestMapping.Service;
import util.KeyValuePair;
import util.NavigationService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Log4j
public class IPUsageService implements NavigationService{

    @DAO
    IPUsageDAO ipUsageDAO;

    @Service
    IPInventoryService ipInventoryService;


    @Transactional
    public void saveIPBlockUsage(IPBlockUsage usage) throws Exception {
//        CurrentTimeFactory.initializeCurrentTimeFactory();
//        long currentTime = CurrentTimeFactory.getCurrentTime();
        validateIPBlockUsage(usage);
//        usage.setCreationTime(currentTime);
//        usage.setLastModificationTime(currentTime);
//        usage.setActiveFrom(currentTime);
        ipUsageDAO.saveIPBlockUsage(usage);
//        CurrentTimeFactory.destroyCurrentTimeFactory();
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<KeyValuePair<Long, Long> >getIPBlockUsageByCritreia() throws Exception {
        List<IPBlockUsage> allUsedIPBlocks = ipUsageDAO.getAllIPBlockUsage();

        return allUsedIPBlocks
                .stream()
                .map(t->MethodReferences.newKeyValue_IP_STR_STR_TO_LONG_LONG.apply(new KeyValuePair<>(t.getFromIP(), t.getToIP())))
//                .sorted(Comparator.comparingLong(o -> (long)o.getKey()))
                .collect(Collectors.toList());

//        return null;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlock> getAvailableIPBlock(IPBlock block, int blockSize) throws Exception{
//        ipUsageDAO.getIPUsageInIPRange(block.getFromIP(), block.getToIP());
        long fromIP = IPUtility.ipv4ToLong(block.getFromIP());
        long toIP = IPUtility.ipv4ToLong(block.getToIP());
//        int blockSize = params.getBlockSize();

        List<KeyValuePair<Long, Long>> usageList = ipUsageDAO.getIPUsageInIPRange(block.getFromIP(), block.getToIP())
                .stream()
                .map(t->MethodReferences.newKeyValue_IP_STR_STR_TO_LONG_LONG.apply(new KeyValuePair<>(t.getFromIP(), t.getToIP())))
//                .map(MethodReferences.newKeyValuePair_IP_Long_Long_TO_STR_STR)
                .collect(Collectors.toList());
//                 .forEach(log::info);
        return LongStream.rangeClosed(fromIP, toIP)
                .boxed()
                .filter(t->t%blockSize==0)
                .filter(t->t+blockSize-1 <=toIP)
                .filter(t-> check(t, blockSize, usageList))
                .map(t->new KeyValuePair<>(t, t+blockSize-1))
                .map(MethodReferences.newKeyValuePair_IP_Long_Long_TO_STR_STR)
                .map(t->new IPBlock(t.getKey(), t.getValue()))
                .collect(Collectors.toList());
    }

    private boolean check(long candidate, int blockSize, List<KeyValuePair<Long, Long>> list) {
        for(long i = candidate; i< candidate + blockSize;i++) {
            KeyValuePair<Long, Long> matched = list.stream()
                    .filter(t->t.getKey()<=candidate && t.getValue()>=candidate)
                    .findAny()
                    .orElse(null);
            if(matched != null){
                return false;
            }

        }
        return true;

    }
    private boolean checkRange(long candidate , List<KeyValuePair<Long, Long>> list) {
        return list.stream()
                .anyMatch(t -> candidate >= t.getKey() && candidate <= t.getValue());


    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public List<Long> getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        return (List<Long>) ipUsageDAO.getIDsWithSearchCriteria(searchCriteria);
    }

    @Override@Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlockUsage> getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return (List<IPBlockUsage>) ipUsageDAO.getAllIPUsageByIDs((List<Long>)recordIDs);
    }



    private List<KeyValuePair<Long, Long>> getFakeUsedBlocksFromInventory(List<KeyValuePair<Long, Long>> inventoryBlocks) {
        List<KeyValuePair<Long, Long>> fakeUsedBlocks = new ArrayList<>();
        for(int i=1;i<inventoryBlocks.size();i++){
            KeyValuePair<Long, Long> nextPair = inventoryBlocks.get(i);
            KeyValuePair<Long, Long> prevPair = inventoryBlocks.get(i-1);
            if(prevPair.getValue() + 1 == nextPair.getKey())continue;
            KeyValuePair<Long, Long> fakeUsedBlock = MethodReferences.newKeyValue_LONG_LONG_TO_LONG_LONG.apply(prevPair.getValue() + 1, nextPair.getKey() - 1);
            fakeUsedBlocks.add(fakeUsedBlock);
        }

        return  fakeUsedBlocks;
    }

    private void validateIPBlockUsage(IPBlockUsage usage) {
        //TODO

    }

    public static void main(String [] args) throws Exception {

//        IPBlockUsage ipBlockUsage = IPBlockUsage.builder()
//                .fromIP("180.211.159.192" )
//                .toIP("180.211.159.195")
//                .isRealIP(true)
//                .parentIP(null)
//                .districtId(1L)
//                .purpose(IPConstants.Purpose.BTCL_INTERNAL)
//                .version(IPConstants.Version.IPv4)
//                .status(IPConstants.Status.ACTIVE)
//                .build();
//        ServiceDAOFactory.getService(IPUsageService.class).getAvailableIPBlock(5L, IPConstants.Version.IPv4, IPConstants.Type.PUBLIC, 256, ModuleConstants.Module_ID_LLI);
//        ServiceDAOFactory.getService(IPUsageService.class).saveIPBlockUsage(ipBlockUsage);

        ServiceDAOFactory.getService(IPUsageService.class).getAvailableIPBlock(new IPBlock("123.49.0.0", "123.49.0.255"), 4);
    }

    public void cancelUsageByUsageId(long ipUsageId)  {
        IPBlockUsage usage = null;
        try {
            usage = ipUsageDAO.getById(ipUsageId);
        } catch (Exception e) {
            log.fatal("error getting object from ip_usage by id " + ipUsageId);
            log.fatal(e.getMessage(), e);
        }
        if(usage != null ){
            usage.setDeleted(true);
            usage.setActiveTo(System.currentTimeMillis());
            try {
                ipUsageDAO.update(usage);
            } catch (Exception e) {
                log.fatal("error updating ip_usage for id " + usage.getId());
                log.fatal(e.getMessage());
            }
        }
    }


    @Transactional(transactionType = TransactionType.READONLY)
    public List<IPBlockUsage> getUsageBlocksByUsageIDs(List<Long> usageIds) throws Exception {
        return (List<IPBlockUsage>)getDTOs(usageIds);
    }

    @Transactional
    public void deletePublicIpUsageById(long id) throws Exception {
        ipUsageDAO.deletePublicIpUsageById(id);
    }
}
