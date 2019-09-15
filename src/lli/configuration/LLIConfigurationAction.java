package lli.configuration;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import annotation.ForwardedAction;
import annotation.JsonPost;
import costConfig.CategoryDTO;
import costConfig.CategoryService;
import lli.configuration.ofc.cost.OfcInstallationCostDTO;
import lli.configuration.ofc.cost.OfcInstallationCostService;
import requestMapping.AnnotatedRequestMappingAction;
import requestMapping.RequestParameter;
import requestMapping.Service;
import requestMapping.annotation.ActionRequestMapping;
import requestMapping.annotation.*;

@ActionRequestMapping("lli/configuration/")
public class LLIConfigurationAction extends AnnotatedRequestMappingAction{
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Service
	LLICostConfigurationService lliCostConfigurationService;
	
	@Service
	CategoryService categoryService;
	
	@Service
	OfcInstallationCostService ofcService;
	
	@RequestMapping(mapping="get-latest-cost-by-date", requestMethod=RequestMethod.GET)
	public OfcInstallationCostDTO getLatestCostByDate(
			@RequestParameter("date") Long date) {
		return ofcService.getLatestByDate(date);
	}
	
	@RequestMapping(mapping="add-new-ofc-install-cost", requestMethod=RequestMethod.POST)
	public OfcInstallationCostDTO addNewOfcInstallationCost(
			@RequestParameter("commonChargeCost") Integer fiberCost,
			@RequestParameter("oneTimeCost") Integer oneTimeCost,
			@RequestParameter("perMeter") Integer fiberLength,
			@RequestParameter("dateOfUpdate") String applicableFrom) throws Exception {
		Date date = (Date) new SimpleDateFormat("dd/MM/yyyy").parse(applicableFrom);
		logger.debug(fiberLength+" "+fiberCost+" "+applicableFrom);
		OfcInstallationCostDTO ofc = ofcService.createObject(fiberLength, oneTimeCost, fiberCost, date.getTime());
		OfcInstallationCostDTO ofcInstallationCostDTO = ofcService.insertItem(ofc);
		
		return ofcInstallationCostDTO;
	}
	
	@RequestMapping(mapping="edit-category", requestMethod=RequestMethod.POST)
	public CategoryDTO editCategory(
			@RequestParameter("id") Long id,
			@RequestParameter("categoryName") String categoryName) {
		CategoryDTO categoryDTO = categoryService.editCategory(id, categoryName);
		
		return categoryDTO;
	}
	
	@RequestMapping(mapping="add-new-category", requestMethod=RequestMethod.POST)
	public CategoryDTO postNewCategory(
			@RequestParameter("categoryName") String categoryName) {
		logger.debug(categoryName);
		CategoryDTO categoryDTO = categoryService.addCategory(categoryName);
		
		return categoryDTO;
	}
	
	@RequestMapping(mapping="get-all-categories", requestMethod=RequestMethod.All)
	public List<CategoryDTO> getAllCategories() {
		List<CategoryDTO> categories = categoryService.getAllCategories();
		return categories;
	}
	
	@ForwardedAction
	@RequestMapping(mapping="new-category-ui/get", requestMethod=RequestMethod.GET)
	public String addNewCategory() {
		return "add-new-category-ui";
	}
	
	@ForwardedAction
	@RequestMapping(mapping="fixed-cost/get", requestMethod=RequestMethod.GET)
	public String getFixedCostCostConfigPage() {
		return "fixed-cost-configuration";
	}
	
	@RequestMapping(mapping="fixed-cost/latest/view", requestMethod = RequestMethod.GET)
	public LLIFixedCostConfigurationDTO getLatestLLI_fixed_cost_Configuration() throws Exception {
		return lliCostConfigurationService.getCurrentActiveLLI_FixedCostConfigurationDTO();
	}
	
	@JsonPost
	@RequestMapping(mapping="fixed-cost/insert", requestMethod = RequestMethod.POST)
	public void insertLatestLLI_fixed_cost_Configuration(@RequestParameter(isJsonBody=true, value="otc") LLIFixedCostConfigurationDTO otc) throws Exception {
		lliCostConfigurationService.insert(otc);
	}
}
