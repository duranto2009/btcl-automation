package test;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

public class DateUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		
		Calendar c = Calendar.getInstance();
		
		assertEquals( 8, c.get( Calendar.MONTH ) );
		assertEquals( 2017, c.get( Calendar.YEAR ) );
		
		c.set( Calendar.MONTH, 1 );
		c.set( Calendar.YEAR, 2015 );
		
		assertEquals( 1, c.get( Calendar.MONTH ) );
		assertEquals( 2015, c.get( Calendar.YEAR ) );
		
		c = Calendar.getInstance();
		
		assertEquals( 8, c.get( Calendar.MONTH ) );
		assertEquals( 2017, c.get( Calendar.YEAR ) );
	}

}
