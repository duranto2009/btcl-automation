package nix.application.close;
import util.ModifiedSqlGenerator;
import java.util.List;
public class NIXCloseApplicationDAO {

    public void insert(NIXCloseApplication nixCloseApplication)throws Exception {
        ModifiedSqlGenerator.insert(nixCloseApplication);
    }

    public List<NIXCloseApplication> getApplicationByParent(long id)throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(NIXCloseApplication.class,
                new NIXCloseApplicationConditionBuilder()
                .Where()
                .parentEquals(id)
                .getCondition());

    }
}
