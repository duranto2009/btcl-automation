package scheduler.common;

import annotation.Transactional;
import common.UniversalDTOService;
import lombok.extern.log4j.Log4j;
import util.ServiceDAOFactory;

@Log4j
class SchedulerService {

    @Transactional
    void updateSchedulerProperty(SchedulerProperty property) throws Exception {
        UniversalDTOService universalDTOService = ServiceDAOFactory.getService(UniversalDTOService.class);
        universalDTOService.update(property);
    }


    SchedulerProperty getCurrentSchedulerProperty() throws Exception {
        UniversalDTOService universalDTOService = ServiceDAOFactory.getService(UniversalDTOService.class);
        return universalDTOService.get(SchedulerProperty.class);
    }
}
