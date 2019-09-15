package flow.service;

import annotation.DAO;
import annotation.Transactional;
import flow.dao.FlowStateTransitionRoleDAO;
import flow.entity.FlowStateTransitionRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.TransactionType;

import java.util.List;

/**
 * @author maruf
 */
public class FlowStateTransitionService {

    private static final Logger LOG = LoggerFactory.getLogger(FlowStateTransitionService.class);

    @DAO
    FlowStateTransitionRoleDAO flowStateTransitionRoleDAO;

    @Transactional(transactionType = TransactionType.READONLY)
    public List<FlowStateTransitionRole> getByRoleId(int role) {
        try {
            return flowStateTransitionRoleDAO.getByRole(role);
        } catch (Exception ex) {
            LOG.error(ex.toString(), ex);
        }
        return null;
    }
}
