package util;

import common.EntityTypeConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class UtilService {

	public StringBuilder arrayListTOString(ArrayList<?> data) {
		StringBuilder dataString = new StringBuilder();
		int i = 0;
		dataString.append(" ( ");
		for (Object id : data) {
			i++;
			dataString.append(" " + id + " ");

			if (data.size() != i) {
				dataString.append(" , ");
			}

		}
		dataString.append(" ) ");
		return dataString;

	}
	
	public static boolean isCreativeRequest(int requestTypeID)
	{	
		int requestTypePrefix = requestTypeID / EntityTypeConstant.MULTIPLIER2;
		int requestTypeSuffix = requestTypePrefix % 100;
		return (requestTypeSuffix == 1);
		
	}

	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
