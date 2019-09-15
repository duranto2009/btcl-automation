package nix.office;

import annotation.DAO;
import annotation.Transactional;
import nix.application.office.NIXApplicationOfficeService;
import nix.localloop.NIXLocalLoop;
import nix.localloop.NIXLocalLoopService;
import util.ServiceDAOFactory;
import util.TransactionType;

import java.util.List;
import java.util.stream.Collectors;

public class NIXOfficeService {
    @DAO
    NIXOfficeDAO nixOfficeDAO;
    NIXLocalLoopService nixLocalLoopService = ServiceDAOFactory.getService(NIXLocalLoopService.class);
    NIXApplicationOfficeService nixApplicationOfficeService=ServiceDAOFactory.getService(NIXApplicationOfficeService.class);

    @Transactional
    public void insertOffice(NIXOffice nixOffice)throws Exception {
        nixOfficeDAO.insertOffice(nixOffice);
    }

    @Transactional
    public List<NIXOffice> getOfficesByConnectionID(long connectionId) throws Exception{
        return nixOfficeDAO.getOfficesByConnectionID(connectionId)
                .stream()
                .map(t->{
                    try {
                        t.setLocalLoops(nixLocalLoopService.getLocalLoopsByOfficeID(t.getId()));
                        t.setNixApplicationOffice(nixApplicationOfficeService.getOfficeById(t.getApplication_offfice()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }return t;
                }).collect(Collectors.toList());
    }

    @Transactional
    public NIXOffice getOfficeById(long office) throws Exception{
        NIXOffice nixOffice =  nixOfficeDAO.getOfficeById(office);
        List<NIXLocalLoop> nixLocalLoops = nixLocalLoopService.getLocalLoopsByOfficeID(office);
        nixOffice.setLocalLoops(nixLocalLoops);
        return nixOffice;
    }

    @Transactional(transactionType = TransactionType.READONLY)
    public List<NIXOffice> getOfficesByConnectionHistoryID(long historyId) throws Exception {
        return nixOfficeDAO.getOfficesByConnectionHistoryId(historyId);
    }
}
