package flow.dao;

import flow.entity.Role;
import util.ModifiedSqlGenerator;

import java.util.List;

public class RoleDAO {
    public List<Role> getAll() throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(Role.class);
    }

    public Role get(long id) throws Exception {
        if (id > 0) {
            return ModifiedSqlGenerator.getObjectByID(Role.class, id);
        }
        return null;
    }
}
