package costConfig;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.DAO;
import annotation.Transactional;

public class CategoryService {
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@DAO
	CategoryDAO categoryDAO;
	
	@Transactional
	public CategoryDTO editCategory(Long id, String categoryName) {
		try {
			return categoryDAO.edit(id, categoryName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public CategoryDTO createObject(String categoryName) {
		try {
			return categoryDAO.getObject(categoryName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Transactional
	public CategoryDTO addCategory(String categoryName) {
		CategoryDTO categoryDTO;
		try {
			categoryDTO = categoryDAO.insertNew(categoryName);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return categoryDTO;
	}
	
	@Transactional
	public List<CategoryDTO> getAllCategories() {
		try {
			return categoryDAO.findAllCategories();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
