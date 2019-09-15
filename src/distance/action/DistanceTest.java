package distance.action;

import distance.DistanceService;
import util.ServiceDAOFactory;

import static org.junit.Assert.*;

import org.junit.Test;

public class DistanceTest {
	DistanceService distanceService = (DistanceService)ServiceDAOFactory.getService(DistanceService.class);
	protected long location1, location2;
   //test to check appraisal
   @Test
   public void testDistance() throws Exception {
	   location1 = 56011;
	   location2 = 59011;
		
	   double distance = distanceService.getDistanceBetweenTwoLocation(location1, location2);
	   assertTrue(distance == 66.0);
   }
}