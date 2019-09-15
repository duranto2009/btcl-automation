package upstream.contract;

import annotation.DAO;
import annotation.Transactional;

import common.ModuleConstants;
import common.RequestFailureException;
import flow.FlowService;
import flow.entity.FlowState;
import global.GlobalService;
import login.LoginDTO;
import login.LoginService;
import requestMapping.Service;
import upstream.circuitInfo.CircuitInformationDTO;
import upstream.circuitInfo.CircuitInformationService;
import util.NavigationService;
import util.TransactionType;

import java.util.*;
import java.util.stream.Collectors;

public class UpstreamContractService implements NavigationService {

    @Service
    GlobalService globalService;

    @DAO
    UpstreamContractDAO contractDAO;

    @Service
    FlowService flowService;

    @Service
    private CircuitInformationService circuitInformationService;


    @Transactional
    public void saveContract(UpstreamContract contract) throws Exception {
        globalService.save(contract);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamContract> getContractsByBandwidthType(long bandwidthTypeId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .typeOfBandwidthIdEquals(bandwidthTypeId)
                        .getCondition());

        if (contracts.isEmpty()) {
            throw new RequestFailureException("No contract found with bandwidth type id " + bandwidthTypeId);
        }
        return contracts;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamContract> getContractsByBTCLServiceLocation(long serviceLocationId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .btclServiceLocationIdEquals(serviceLocationId)
                        .getCondition());

        if (contracts.isEmpty()) {
            throw new RequestFailureException("No contract found with BTCL Service Location id " + serviceLocationId);
        }
        return contracts;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamContract> getContractsByProviderId(long providerId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .selectedProviderIdEquals(providerId)
                        .getCondition());

        if (contracts.isEmpty()) {
            throw new RequestFailureException("No contract found with provider id " + providerId);
        }
        return contracts;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamContract> getContractsByMediaType(long mediaTypeId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .mediaIdEquals(mediaTypeId)
                        .getCondition());

        if (contracts.isEmpty()) {
            throw new RequestFailureException("No contract found with media id " + mediaTypeId);
        }
        return contracts;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<UpstreamContract> getContractsByProviderLocation(long providerLocationId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .providerLocationIdEquals(providerLocationId)
                        .getCondition());

        if (contracts.isEmpty()) {
            throw new RequestFailureException("No contract found with provider location id " + providerLocationId);
        }
        return contracts;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDs(LoginDTO loginDTO, Object... objects) throws Exception {
        return getIDsWithSearchCriteria(new Hashtable(), loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getIDsWithSearchCriteria(Hashtable searchCriteria, LoginDTO loginDTO, Object... objects) throws Exception {
        searchCriteria.put("moduleId", Integer.toString(ModuleConstants.Module_ID_UPSTREAM));
        return contractDAO.getIDsWithSearchCriteria(searchCriteria, loginDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    @Override
    public Collection getDTOs(Collection recordIDs, Object... objects) throws Exception {
        return contractDAO.getDTOs((List<Long>) recordIDs);
    }

    public UpstreamContract getContractByContractId(long contractID) throws Exception {
        UpstreamContract contract = globalService.findByPK(UpstreamContract.class, contractID);

        if (contract == null) {
            throw new RequestFailureException("No contract found with contract id " + contractID);
        }
        contract.setCircuitInformationDTOs(circuitInformationService.getAllCircuitsByContractId(contract.getContractId()));
        return contract;
    }


    @Transactional
    public List<UpstreamContract> getAllContractsByHistoryId(long historyId) throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder()
                        .Where()
                        .contractHistoryIdEquals(historyId)
                        .getCondition());

        if (contracts.isEmpty())
            throw new RequestFailureException("No contracts found with history id " + historyId);

        return contracts.stream().map(x -> {
            try {
                x.setCircuitInformationDTOs(circuitInformationService.getAllCircuitsByContractId(x.getContractId()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return x;
        }).collect(Collectors.toList());
    }

    @Transactional
    public List<UpstreamContract> getAllActiveContracts() throws Exception {

        List<UpstreamContract> contracts = globalService.getAllObjectListByCondition(UpstreamContract.class,
                new UpstreamContractConditionBuilder().Where()
//                        .contractHistoryIdEquals(historyId)
                        .activeToGreaterThan(System.currentTimeMillis())
                        .getCondition());

        if (contracts.isEmpty())
            throw new RequestFailureException("No contracts found ");

        Map<Long, List<CircuitInformationDTO>> map = circuitInformationService.getCircuitInfoMapByContractId();


        return contracts.stream()
                .peek(t-> {
                    t.setCircuitInformationDTOs(map.getOrDefault(t.getContractId(), Collections.emptyList()));
                }).collect(Collectors.toList());
    }



    @Transactional
    public void update(UpstreamContract contract, LoginDTO loginDTO) {
        globalService.update(contract);
    }
}
