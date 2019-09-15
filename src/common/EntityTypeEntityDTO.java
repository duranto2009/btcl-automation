package common;

public class EntityTypeEntityDTO {
	
	int entityTypeID;
	long entityID;
	public int getEntityTypeID() {
		return entityTypeID;
	}
	public void setEntityTypeID(int entityTypeID) {
		this.entityTypeID = entityTypeID;
	}
	public long getEntityID() {
		return entityID;
	}
	public void setEntityID(long entityID) {
		this.entityID = entityID;
	}
	@Override
	public String toString() {
		return "EntityTypeEntityDTO [entityTypeID=" + entityTypeID + ", entityID=" + entityID + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (entityID ^ (entityID >>> 32));
		result = prime * result + entityTypeID;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntityTypeEntityDTO other = (EntityTypeEntityDTO) obj;
		if (entityID != other.entityID)
			return false;
		if (entityTypeID != other.entityTypeID)
			return false;
		return true;
	}

}
