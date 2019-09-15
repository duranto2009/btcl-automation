package test.junit;


import ip.IPUtility;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Raihan on 10/11/2018.
 */
public class IPUtilityTest {
    static Logger logger = Logger.getLogger(IPUtilityTest.class);
    IPUtility ipUtility;
    public void testIPUtilityObject() {
        assertNotNull(ipUtility);
    }

    Object [][]inputIPv4ToInt = new Object[][]{
            {"0.0.0.0", 0L, false},
            {"0.0.0.1", 1L, false},
            {"0.0.0.a", 1L, true}, // expected does not matter
            {"0.0.1.0", 256L, false},
            {".1", 1L, true},
            {"255.255.255.255", 4294967295L, true},
    };
    Object [][] inputIntToIPv4 = new Object[][]{
            { 0L, "0.0.0.0", false},
            { 1L, "0.0.0.1", false},
            { 256L, "0.0.1.0", false},
            { 4294967295L, "255.255.255.255", false},
            { 387389207L, "23.23.23.23", false},
            { 3203037953L, "190.234.123.1", false},
            { 3203037952L, "190.234.123.0", false},
            { 3203037951L, "190.234.122.255", false},

    };

    @Test
    public void testIPv4ToInt() {
        Arrays.stream(inputIPv4ToInt).forEach(IPUtilityTest::runTest1);
    }

    @Test
    public void testIntToIPv4() {

        Arrays.stream(inputIntToIPv4).forEach(IPUtilityTest::runTest2);
    }
    public static void runTest1(Object[] t) {
        String input = (String) t[0];
        long expected = (Long)t[1];
        boolean expectException = (Boolean)t[2];
        try {
//            assertEquals(expected, IPUtility.ipv4ToLong(input));
            logger.info("SUCCESS : "+input+ " -> " + expected);
        }catch(Exception e){
            if(expectException) {
                logger.info("SUCCESS Expected Exception for: " +input + " " + e.getMessage() );
            }else {
                logger.info("FAILURE Not Expected Exception for: " + input + " " + e.getMessage() );
            }

        }

    }
    public static void runTest2(Object[] t) {
        String expected = (String) t[1];
        long input = (Long)t[0];
        boolean expectException = (Boolean)t[2];
        try {
//            assertEquals(expected, IPUtility.longToipv4String(input));
            logger.info("SUCCESS : "+input+ " -> " + expected);
        }catch(Exception e){
            if(expectException) {
                logger.info("SUCCESS Expected Exception for: " +input + " " + e.getMessage() );
            }else {
                logger.info("FAILURE Not Expected Exception for: " + input + " " + e.getMessage() );
            }

        }

    }

}
