package lli.migration;

import global.GlobalService;
import lli.Application.FlowConnectionManager.LLIConnection;
import lli.Application.LocalLoop.LocalLoop;
import lli.Application.Office.Office;
import requestMapping.Service;

public class LLIMigrationService {

    @Service
    private GlobalService globalService;

    public void addNewConnection(LLIConnection lliConnection) throws Exception {

        lliConnection.setCostChartID(0);
        lliConnection.setStatus(1);
        lliConnection.setIncident(1);
        lliConnection.setDiscountRate(0);

        lliConnection.setValidFrom(System.currentTimeMillis());
        lliConnection.setStartDate(lliConnection.getActiveFrom());

        lliConnection.setValidTo(Long.MAX_VALUE);
        lliConnection.setActiveTo(Long.MAX_VALUE);

        globalService.save(lliConnection);
        lliConnection.setID(lliConnection.getHistoryID());
        globalService.update(lliConnection);

        lliConnection.getOffices().forEach(t -> {
            try {
                addNewOffice(lliConnection.getHistoryID(), t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void addNewOffice(long connectionHistoryId, Office office) throws Exception {

        office.setApplicationId(0);
        office.setConnectionID(connectionHistoryId);
        globalService.save(office);
        office.setHistoryId(office.getId());
        globalService.update(office);

        office.getLoops().forEach(t -> {
            try {
                addNewLocalLoop(office.getId(), t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void addNewLocalLoop(long officeHistoryId, LocalLoop localLoop) throws Exception {
        localLoop.setApplicationID(0);
        localLoop.setOfficeID(officeHistoryId);
        localLoop.setDeleted(false);

        globalService.save(localLoop);
        localLoop.setId(localLoop.getHistoryID());
        globalService.update(localLoop);
    }
}
