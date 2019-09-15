package lli.bill;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BillUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetCostForIpAddress() {
		
		assertEquals( BillUtil.getCostForIpAddress( 0 ), 0.0, 0 );
		assertEquals( BillUtil.getCostForIpAddress( 2 ), 1600, 0 );
		assertEquals( BillUtil.getCostForIpAddress( 4 ), 3200.0, 0 );
		assertEquals( BillUtil.getCostForIpAddress( 8 ), 4000.0, 0 );
		assertEquals( BillUtil.getCostForIpAddress( 16 ), 5600.0, 0 );
	}

}
