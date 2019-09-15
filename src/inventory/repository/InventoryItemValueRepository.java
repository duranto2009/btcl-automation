package inventory.repository;

import repository.Repository;

class InventoryItemValueRepository implements Repository{
	private static InventoryItemValueRepository instance = null;
	
	public synchronized InventoryItemValueRepository getInstance(){
		if(instance == null){
			instance = new InventoryItemValueRepository();
		}
		return instance;
	}
	
	@Override
	public void reload(boolean realoadAll) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
