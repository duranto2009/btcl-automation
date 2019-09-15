package upstream.circuitInfo;

import annotation.Transactional;
import common.RequestFailureException;
import exception.NoDataFoundException;
import global.GlobalService;
import requestMapping.Service;
import util.TransactionType;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CircuitInformationService {

    @Service
    GlobalService globalService;

    @Transactional
    public void addNewCircuitInformation(CircuitInformationDTO circuitInformationDTO) throws Exception {
        globalService.save(circuitInformationDTO);
    }

    @Transactional
    public void updateCircuitInformation(CircuitInformationDTO circuitInformationDTO) throws Exception {
        globalService.update(circuitInformationDTO);
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<CircuitInformationDTO> getAllCircuitsByContractId(long contractId) throws Exception {

        List<CircuitInformationDTO> dtos = globalService.getAllObjectListByCondition(CircuitInformationDTO.class,
                new CircuitInformationDTOConditionBuilder()
                        .Where()
                        .contractIdEquals(contractId)
                        .getCondition());

        if (dtos.isEmpty()) return null;
//            throw new NoDataFoundException("No circuits found with application id " + applicationId);

        return dtos;

    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, CircuitInformationDTO> getCircuitInfoMap() throws Exception {
        return globalService.getAllObjectListByCondition(
                CircuitInformationDTO.class, new CircuitInformationDTOConditionBuilder()

        ).stream()
                .collect(Collectors.toMap(CircuitInformationDTO::getId, Function.identity()));
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public Map<Long, List<CircuitInformationDTO>> getCircuitInfoMapByContractId() throws Exception {
        return globalService.getAllObjectListByCondition(
                CircuitInformationDTO.class, new CircuitInformationDTOConditionBuilder()
                .getCondition()

        ).stream()
                .collect(Collectors.groupingBy(CircuitInformationDTO::getContractId));
    }
}
