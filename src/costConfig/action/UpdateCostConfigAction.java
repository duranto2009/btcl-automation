package costConfig.action;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import common.ModuleConstants;
import common.RequestFailureException;
import costConfig.CostCellDTO;
import costConfig.CostConfigService;
import costConfig.TableDTO;
import costConfig.form.CostConfigForm;
import databasemanager.DatabaseManager;
import util.ColumnDTO;
import util.RowDTO;
import util.ServiceDAOFactory;
import util.TimeConverter;

public class UpdateCostConfigAction extends Action {
	Logger logger = Logger.getLogger(getClass());
	//TimeConverter timeConverter = new TimeConverter();

	public ActionForward execute(ActionMapping p_mapping, ActionForm p_form, HttpServletRequest p_request,
			HttpServletResponse p_response) {
		logger.debug("UpdateCostConfigAction()");
		String target = "success";
		CostConfigForm form = (CostConfigForm) p_form;
		int moduleID = -1;
		int categoryID = -1;
		TableDTO tableDTO = null;
		HttpSession session = p_request.getSession();
		try {
			moduleID = Integer.parseInt(p_request.getParameter("moduleID"));
			tableDTO = createTableDTO(form, moduleID);
			if(tableDTO == null){
				target="success";
//				return p_mapping.findForward(target);
			}
			CostConfigService costConfigService = ((CostConfigService)ServiceDAOFactory.getService(CostConfigService.class));
			switch(tableDTO.getModuleID()){
				case ModuleConstants.Module_ID_VPN:
					costConfigService.insertCostChartForVpn(tableDTO);
					session.setAttribute("success_message", "Updated Successfully!");
					break;
				case ModuleConstants.Module_ID_LLI:
//					System.err.println(p_request.getParameterNames()+"\n"+p_request.getParameterMap()	);
					categoryID = Integer.parseInt(p_request.getParameter("categoryID"));
					costConfigService.insertCostChartForLLI(tableDTO, categoryID);
					session.setAttribute("success_message", "Updated Successfully!");
					break;
				default: 
					throw new RequestFailureException("Invalid module ID");
			}
			
			
		}catch(RequestFailureException ex){
			logger.debug("fatal",ex);
			session.setAttribute("msg", ex.getMessage());
			
		}catch(Exception ex){
			logger.debug("fatal",ex);
			session.setAttribute("msg", "Server Error");
		}
		
		String path = "/GetCostConfig.do?moduleID="+moduleID;
		path+=(moduleID==ModuleConstants.Module_ID_LLI?("&categoryID="+categoryID):"");
		
		ActionForward af = new ActionForward();
		af.setPath(path);
		af.setRedirect(true);
		return af;
	}

	private TableDTO createTableDTO(CostConfigForm form, int moduleID) throws Exception{
		
		TableDTO tableDTO = null;
		
		String[] lowerRangeMb = form.getLowerRangeMb();
		String[] upperRangeMb = form.getUpperRangeMb();
		String[] lowerRangeKm = form.getLowerRangeKm();
		String[] upperRangeKm = form.getUpperRangeKm();
		
		
		
		int numberOfRows = lowerRangeMb.length;
		int numberOfColumns = lowerRangeKm.length;
		long[] rowIDs = new long[numberOfRows];
		long[] columnIDs = new long[numberOfColumns];
		long[] cellIDs = new long[numberOfRows * numberOfColumns];
		double[] cellValues = form.getCost();
		
		
		if (lowerRangeMb.length != upperRangeMb.length || upperRangeKm.length != lowerRangeKm.length
				|| numberOfRows * numberOfColumns != cellValues.length || !checkRanges(lowerRangeKm, upperRangeKm)
				|| !checkRanges(lowerRangeMb, upperRangeMb) || !checkCellValues(cellValues) || form.getActivationDate() == null) {
			//return tableDTO;
			throw new RequestFailureException("Invalid Table Insertion");
		}
		else {
			tableDTO = new TableDTO();
			if(common.StringUtils.isBlank(form.getActivationDate())){
				throw new RequestFailureException("No valid date found");
			}
			long date = TimeConverter.getTimeFromString(form.getActivationDate());
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(date);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY,0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND,0);
			calendar.set(Calendar.MILLISECOND,0);
			
			if(calendar.getTimeInMillis() != date){
				throw new RequestFailureException("Not a start day of the month");
			}
			//date = TimeConverter.getStartTimeOfTheDay(date);
			tableDTO.setActivationDate(date);
			tableDTO.setIsDeleted(false);
			tableDTO.setLastModificationTime(System.currentTimeMillis());
			tableDTO.setModuleID(moduleID);
			
			long tableID = tableDTO.getTableID();
			List<RowDTO> rowDTOs = new ArrayList<>();
			List<ColumnDTO> columnDTOs = new ArrayList<>();
			List<CostCellDTO> costCellDTOs = new ArrayList<>();
			
			for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {

				RowDTO rowDTO = new RowDTO();
				rowDTO.setLowerRange(Integer.parseInt(lowerRangeMb[rowIndex]));
				if (!upperRangeMb[rowIndex].equals("~")) {
					rowDTO.setUpperRange(Integer.parseInt(upperRangeMb[rowIndex]));
				} else {
					rowDTO.setUpperRange(Integer.MAX_VALUE);
				}

				rowDTO.setTableID(tableID);
				rowDTO.setDeleted(false);
				rowDTO.setLastModificationTime(System.currentTimeMillis());
				rowDTO.setIndex(rowIndex);

//				costConfigService.insertRow(rowDTO);
				rowIDs[rowIndex] = rowDTO.getID();

				logger.debug(rowDTO);
				rowDTOs.add(rowDTO);

			}
			for (int columnIndex = 0; columnIndex < numberOfColumns; columnIndex++) {
				ColumnDTO columnDTO = new ColumnDTO();
				columnIDs[columnIndex] = DatabaseManager.getInstance().getNextSequenceId("at_cost_chart_col");
				// columnDTO.setID(columnIDs[columnIndex]);
				columnDTO.setLowerRange(Integer.parseInt(lowerRangeKm[columnIndex]));
				columnDTO.setUpperRange(Integer.parseInt(upperRangeKm[columnIndex]));
				columnDTO.setTableID(tableID);
				columnDTO.setDeleted(false);
				columnDTO.setLastModificationTime(System.currentTimeMillis());
				columnDTO.setIndex(columnIndex);
				logger.debug(columnDTO);
				columnIDs[columnIndex] = columnDTO.getID();
				columnDTOs.add(columnDTO);
			}
			for (int cellIndex = 0; cellIndex < cellIDs.length; cellIndex++) {
				CostCellDTO cellDTO = new CostCellDTO();
				cellIDs[cellIndex] = DatabaseManager.getInstance().getNextSequenceId("at_vpn_monthly_charge_cell");
				cellDTO.setValue(cellValues[cellIndex]);
				cellDTO.setColID(columnIDs[cellIndex % numberOfColumns]);
//				int cellRowIndex = (int) rowIDs[cellIndex / numberOfColumns];
//				System.err.println(cellRowIndex);
				cellDTO.setTableID(tableID);
				cellDTO.setIsDeleted(false);
				cellDTO.setLastModificationTime(System.currentTimeMillis());
				cellDTO.setRowID(rowIDs[cellIndex / numberOfColumns]);
				logger.debug(cellDTO);
				costCellDTOs.add(cellDTO);
				
			}
			tableDTO.setRowDTOs(rowDTOs);
			tableDTO.setColumnDTOs(columnDTOs);
			tableDTO.setCostCellDTOs(costCellDTOs);
		}
		return tableDTO;
	}

	private boolean checkCellValues(double[] cellValues) {
		for (double cellValue : cellValues) {
			if (cellValue <= 0)
				return false;
		}
		return true;
	}

	public boolean checkRanges(String[] lowerRange, String[] upperRange) {
		if (!checkContinuity(lowerRange, upperRange)) {
			return false;
		}
		for (int index = 0; index < lowerRange.length; index++) {
			int low = Integer.parseInt(lowerRange[index]);

			int high = 0;
			if (!upperRange[index].equals("~")) {
				high = Integer.parseInt(upperRange[index]);
			}
			else high = Integer.MAX_VALUE;

			if (high<=0)
				return false;
			else if (  low<0 )return false;
			else if( high < low)
			return false;

			for (int secondIndex = 0; secondIndex < lowerRange.length; secondIndex++) {
				if (secondIndex == index)
					continue;
				else {
					if (low >= Integer.parseInt(lowerRange[secondIndex])
							&& low <= Integer.parseInt(upperRange[secondIndex])) {
						return false;
					}
					if (high >= Integer.parseInt(lowerRange[secondIndex])
							&& high <= Integer.parseInt(upperRange[secondIndex])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private boolean checkContinuity(String[] lowerRange, String[] upperRange) {
		for (int index = 1; index < lowerRange.length; index++) {
			int lowerEnd = Integer.parseInt(lowerRange[index]);
			int upperLastEnd = Integer.parseInt(upperRange[index - 1]);
			if (lowerEnd - 1 != upperLastEnd)
				return false;
		}
		return true;
	}
}
