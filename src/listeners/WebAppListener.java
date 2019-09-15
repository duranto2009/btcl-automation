package listeners;

import com.mysql.jdbc.AbandonedConnectionCleanupThread;
import common.LoggedInUserService;
import databasemanager.DatabaseManager;
import databasemanager.DatabaseManagerImplementation;
import lombok.extern.log4j.Log4j;
import org.jdom.JDOMException;
import repository.RepositoryManager;
import scheduler.common.SchedulerExecutor;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;

@WebListener
@Log4j
public class WebAppListener implements ServletContextListener {
    private final String stars = "*****************************";
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info(stars + "Web Application is Starting" + stars);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info(stars + "Web Application is Terminating" + stars);
        try {
            destroyScheduler();
            clearDBConnections();
            cleanupAbandonConnections();
            stopRepository();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopRepository() {
        RepositoryManager.getInstance().shutDown();
    }

    private void cleanupAbandonConnections() {
        try {
            log.info("Cleaning up Abandoned Connections");
            AbandonedConnectionCleanupThread.shutdown();
        } catch (InterruptedException e) {
            log.fatal("SEVERE problem cleaning up: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void destroyScheduler() throws Exception {
        log.info("Destroying Scheduler");
        SchedulerExecutor.getInstance().destroyScheduler();
    }

    private void clearDBConnections() throws JDOMException, SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        log.info("Clearing existing database connections");
        DatabaseManagerImplementation.getInstance().closeAllConnections();

    }
}
