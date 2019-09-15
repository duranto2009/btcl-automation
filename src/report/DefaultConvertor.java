package report;

public class DefaultConvertor implements ColumnConvertor{

	@Override
	public Object convert(Object columnValue) {
		return columnValue != null ? columnValue : "N/A";
	}

}
