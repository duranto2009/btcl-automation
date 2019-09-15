package nix.demandnote;

import annotation.Transactional;
import util.ModifiedSqlGenerator;

public class NIXDemandNoteAdjustmentService {
    @Transactional
    public void save(NIXDemandNoteAdjustment dnAdjust) throws Exception{
        ModifiedSqlGenerator.insert(dnAdjust);
    }
}
