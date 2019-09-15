package global;

import util.ModifiedSqlGenerator;

import java.util.List;

public class GlobalDAO {

    void save(Object object) throws Exception {
        ModifiedSqlGenerator.insert(object);
    }

    <T>T findByPK(Class<T> classObject, long id) throws Exception {
        return ModifiedSqlGenerator.getObjectByID(classObject, id);
    }

    void update(Object object) throws Exception {
        ModifiedSqlGenerator.updateEntity(object);
    }

    <T> List<T> getAllObjectListByCondition(Class<T> classObject, Object... condition) throws Exception {
        return ModifiedSqlGenerator.getAllObjectList(classObject, condition);
    }



}
