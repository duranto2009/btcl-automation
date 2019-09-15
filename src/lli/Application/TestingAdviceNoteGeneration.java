package lli.Application;

import java.util.ArrayList;
import java.util.Arrays;

import requestMapping.Service;
import util.ServiceDAOFactory;

public class TestingAdviceNoteGeneration {
	public static void main(String[] args) throws Exception {
		ArrayList<Long> cc = new ArrayList<Long>();
		cc.add(4127L);
		cc.add(8127L);	
//		ServiceDAOFactory.getService(LLIApplicationService.class).generateAdviceNoteDocument(127001, cc);
	}
}
