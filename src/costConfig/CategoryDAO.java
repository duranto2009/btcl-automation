package costConfig;

import java.sql.SQLException;
import java.util.List;

import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import databasemanager.DatabaseManager;
import util.ModifiedSqlGenerator;


public class CategoryDAO {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	Class<CategoryDTO> categoryObject = CategoryDTO.class;
	
	public CategoryDTO edit(Long id, String categoryName) throws Exception {
		logger.debug("id: "+id+" name: "+categoryName);
		CategoryDTO categoryDTO = ModifiedSqlGenerator.getObjectByID(CategoryDTO.class, id);
		
		categoryDTO.setCategoryName(categoryName);
		categoryDTO.setLastModificationTime(System.currentTimeMillis());
		ModifiedSqlGenerator.updateEntity(categoryDTO);
		
		return categoryDTO;
	}
	
	public CategoryDTO insertNew(String categoryName) 
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, JDOMException, SQLException, Exception {
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setDeleted(false);
		categoryDTO.setId(DatabaseManager.getInstance().getNextSequenceId("at_cost_chart_category"));
		categoryDTO.setModuleID(7);
		categoryDTO.setLastModificationTime(System.currentTimeMillis());
		categoryDTO.setCategoryName(categoryName);
		
		ModifiedSqlGenerator.insert(categoryDTO);
		return categoryDTO;
	}
	
	public List<CategoryDTO> findAllCategories() throws Exception{
		return ModifiedSqlGenerator.getAllObjectList(CategoryDTO.class, new CategoryDTOConditionBuilder().Where().moduleIDEquals(7).getCondition());
	}
	
	public CategoryDTO getObject(String categoryName) 
			throws ClassNotFoundException, IllegalAccessException, InstantiationException, JDOMException, SQLException, Exception {
		String tableName = "at_cost_chart_category";
		
		Long id = DatabaseManager.getInstance().getNextSequenceId(tableName);
		CategoryDTO categoryDTO = new CategoryDTO();
		
		categoryDTO.setId(id);
		categoryDTO.setCategoryName(categoryName);
		categoryDTO.setDeleted(false);
		categoryDTO.setLastModificationTime(System.currentTimeMillis());
		categoryDTO.setModuleID(7);
		
		return categoryDTO;
	}
}
