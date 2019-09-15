package ip;

import common.RequestFailureException;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import util.KeyValuePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Raihan on 10/11/2018.
 */
@Log4j
public class IPUtility{
    private IPUtility() {}

    /***
     *
     * @param ip
     * @param octetNumber
     * @return integer value of required octetnumber of ip string [ex: getOcetetValue("123.12.11.10", 2) will return 11, 0 based left side indexing]
     * @throws Exception if parsing error / array index out of bound exception occurs
     */
    public static int getOctetValue(String ip, int octetNumber ) throws Exception {
        List<String> octets = getOctets(ip);
        if(octetNumber>4 || octetNumber<=0){
            throw new Exception("Invalid Octet Number. 1-4 is allowed.");
        }
        return Integer.parseInt(octets.get(octetNumber-1));
    }

    public static long getIPCountByRange(String fromIP, String toIP) throws Exception {
        long to =  ipv4ToLong(toIP);
        long from = ipv4ToLong(fromIP);
        if(to>=from){
            return to-from+1;
        }
        else {
            throw new RequestFailureException("To IP can not be smaller than from IP");
        }

    }

    /***
     *
     * @param octet String representation of Single IP Octet
     * @return int representation of Octet
     * @throws Exception if parsing error occurs
     */
    private static int getValidOctet(String octet)   throws Exception{
        String exceptionMsg = octet + " is an invalid IPv4 octet";
        int result;
        if (!octet.matches("[0-9]+")) {
            throw new Exception(exceptionMsg);
        }
        result = Integer.parseInt(octet);
        if (255 < result || result < 0) {
            throw new Exception(exceptionMsg);
        }

        return result;
    }

    /***
     *
     * @param ipString String representation of IP
     * @return Long representation of IP
     * @throws Exception if parsing Error occurs
     */
    public static long ipv4ToLong(String ipString) throws Exception{

        List<String> octets = getOctets(ipString);
        long result = 0;

        for(String octet: octets){
            int validLongFromOctet = getValidOctet(octet.trim());
            result = ((result<<8) | (validLongFromOctet));
        }
        return result;
    }

    public static String longToipv4String(long ipLong) throws Exception {
        String exceptionMsg=ipLong + " is an invalid ip long value";
        if(ipLong <0L || ipLong>4294967295L){
            throw new RequestFailureException(exceptionMsg);
        }
        String result = "";
        for(int i=0;i<4;i++){

            result = ((ipLong&255)+"") + result;
            if(i<3){
                result = "." + result;
            }
            ipLong>>=8;
        }
        return result;
    }

    /***
     *
     * @param ipString String representation of IP
     * @return List of Octets
     */
    private static List<String> getOctets(String ipString) throws Exception {
        List<String> octets =  Arrays.stream(ipString.trim()
                .split("\\.", -1))
                .collect(Collectors.toList());
        if(octets.size() != 4){
            throw new Exception("Invalid IPv4 Address");
        }
        return octets;
    }

    /***
     *
     * @param fromIP
     * @param toIP
     * @param octetNumber (0 based left sided index)
     * @return whether 2 ip strings octet number's value is equal ex: isSameOctet(1.2.3.4 , 11.2.3.5 , 2 ) will return true,
     * isSameOctet(1.2.3.4 , 11.2.3.5 , 3 ) will return false;
     * @throws Exception
     */
    public static boolean isSameOctet(String fromIP, String toIP, int octetNumber) throws Exception {
       int first = getOctetValue(fromIP, octetNumber);
       int second = getOctetValue(toIP, octetNumber);
       return first == second;
    }

    public static boolean isFirstIP(String ip) throws Exception {
        long serial = getSerialOfIPInABlock(ip).key;
        return serial == 0;

    }

    public static KeyValuePair<Long, Long> getSerialOfIPInABlock(String ip) throws Exception {
        long ipLong = ipv4ToLong(ip);
        return new KeyValuePair<>(ipLong%256, ipLong/256);
    }

    public static String getFirstIPString(String ip) throws Exception {
        return longToipv4String(getFirstIPLong(ip));
    }

    public static String getLastIPString(String ip) throws Exception {
        return longToipv4String(getLastIPLong(ip));
    }

    public static Long getFirstIPLong(String ip) throws Exception {
        KeyValuePair <Long , Long> ipInfo = getSerialOfIPInABlock(ip);

        return  ipInfo.value * 256;
    }

    public static Long getLastIPLong(String ip) throws Exception {
        long firstIPLong = getFirstIPLong(ip);
        return firstIPLong + 255;
    }

    enum IPClass {
        Class_A ("Class A"),
        Class_B ("Class B"),
        Class_C ("Class C"),
        Class_D ("Class D"),
        Class_E ("Class E"),
        Invalid_Class ("Invalid Class");
        private String className;
        IPClass(String className) {
            this.className = className;
        }
        public String getClassName(){
            return this.className;
        }
        public static IPClass getIPClassByString(String className){
            return Arrays.stream(IPClass.values())
                    .filter(ipClass -> ipClass.getClassName().equals(className))
                    .findFirst()
                    .orElse(Invalid_Class);
        }
    }

    /***
     *
     * @param ip String IP
     * @return IPClass enum
     * @throws Exception if parsing error occurs
     */
    private static IPClass getClassInformationOfIP (String ip) throws Exception {
        int firstOctet = getOctetValue(ip, 0);

        return
                ((1 <= firstOctet && firstOctet < 128) ? IPClass.Class_A :
                        ((128 <= firstOctet && firstOctet < 192) ? IPClass.Class_B:
                                ((192 <= firstOctet && firstOctet < 224) ? IPClass.Class_C:
                                        ((224 <= firstOctet && firstOctet < 240) ?  IPClass.Class_D:
                                                ((240 <= firstOctet && firstOctet < 255) ? IPClass.Class_E: IPClass.Invalid_Class)
                                        )
                                )
                        )
                );
    }

    /***
     *
     * @param ips List<String> representation of IP
     * @return List <String> representation of Network Address
     * @throws Exception if ParseError or Invalid IP Information provided.
     */
    public List<String> getNetworkAddress (List<String> ips) throws Exception {
        List<String> networkAddress = new ArrayList<>();
        for (String ip : ips) {
            ipv4ToLong(ip); // for checking valid ip is provided.
            networkAddress.add(getNetworkAddress(ip));
        }
        return networkAddress;
    }

    /***
     *
     * @param ip String representation of ip
     * @return String representation of network ip address
     * @throws Exception if parsing error occurs
     */
    private String getNetworkAddress(String ip) throws Exception {
        IPClass ipClass = getClassInformationOfIP(ip);
        if(ipClass == IPClass.Invalid_Class){
            throw new Exception("Invalid IP Class found for " + ip);
        }


        List<String> octets = getOctets(ip);

        if (ipClass == IPClass.Class_A) {
            return octets.get(0) + ".0.0.0";
        }else if (ipClass == IPClass.Class_B) {
            return octets.get(0) + octets.get(1) + ".0.0";
        }else if(ipClass == IPClass.Class_C){
            return octets.get(0) + octets.get(1) + octets.get(2) + ".0";
        }else {
            throw new Exception("Class D, E does not have any Defined Network Address");
        }
    }
}
