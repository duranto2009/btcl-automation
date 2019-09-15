package lli.demandNote.adjustment;

import static util.ModifiedSqlGenerator.getAllObjectList;

import java.util.List;

import util.ModifiedSqlGenerator;

public class LLIDemandNoteAdjustmentDao {


	Class<LLIDemandNoteAdjustment> classObject = LLIDemandNoteAdjustment.class;
	
	public void insertItem(LLIDemandNoteAdjustment object) throws Exception{
		ModifiedSqlGenerator.insert(object);
	}
	
	public LLIDemandNoteAdjustment getItem(long id) throws Exception{
		return ModifiedSqlGenerator.getObjectByID(classObject, id);
	}
	
	public void updateItem(LLIDemandNoteAdjustment object) throws Exception{
		ModifiedSqlGenerator.updateEntity(object);
	}

	
	public List<LLIDemandNoteAdjustment> getByClientIdAndStatus(long clientId, DNAdjustStatus status) throws Exception{

		List<LLIDemandNoteAdjustment> list = getAllObjectList(classObject, new LLIDemandNoteAdjustmentConditionBuilder()
				.Where()
				.clientIdEquals(clientId)
				.statusLike(status.name())
				.getCondition());
		return list;
	}
	
	public List<LLIDemandNoteAdjustment> getAllByStatus(DNAdjustStatus status) throws Exception{

		List<LLIDemandNoteAdjustment> list = getAllObjectList(classObject, new LLIDemandNoteAdjustmentConditionBuilder()
				.Where()
				.statusLike(status.name())
				.getCondition());
		return list;
	}

	public LLIDemandNoteAdjustment getByBillId(long billId) throws Exception{

		List<LLIDemandNoteAdjustment> list = getAllObjectList(classObject, new LLIDemandNoteAdjustmentConditionBuilder()
				.Where()
				.billIdEquals(billId)
				.getCondition());
		return list.size() > 0 ? list.get(0) : null;
	}
}
