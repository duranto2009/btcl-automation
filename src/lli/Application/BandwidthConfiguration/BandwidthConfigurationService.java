package lli.Application.BandwidthConfiguration;


import annotation.*;
import common.RequestFailureException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import util.TransactionType;

import java.util.List;


public class BandwidthConfigurationService {
    @DAO
    BandwidthConfigurationDAO bandwidthConfigurationDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<BandwidthConfiguration> getBandwidthConfiguration(long bandwidth) throws Exception{
        List<BandwidthConfiguration> bandwidthConfigurationList = bandwidthConfigurationDAO.getBandwidthConfiguration(bandwidth);
        if(bandwidthConfigurationList == null){
            throw new RequestFailureException("No Data Found!!!");
        }
        return bandwidthConfigurationList;
    }


}
