package ip;

import ip.IPInventory.IPBlockInventory;
import ip.ipUsage.IPBlockUsage;
import lombok.extern.log4j.Log4j;
import net.didion.jwnl.data.Exc;
import officialLetter.RecipientElement;
import officialLetter.ReferType;
import user.UserDTO;
import util.KeyValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
@Log4j
public class MethodReferences {
    private MethodReferences() {}

    public static BiFunction <UserDTO, ReferType, RecipientElement> newRecipientElement = RecipientElement::getRecipientElementFromUserAndReferType;
    public static BiFunction <String, String, KeyValuePair <String, String>> newKeyValueString_String = KeyValuePair::new;
    public static BiFunction <Long, Long, KeyValuePair<Long, Long> > newKeyValue_LONG_LONG_TO_LONG_LONG = KeyValuePair::new;
    public static BiFunction <Long, String, KeyValuePair<Long, String> > newKeyValue_Long_String = KeyValuePair::new;
    public static BiFunction <Integer, String, KeyValuePair<Integer, String> > newKeyValue_Integer_String = KeyValuePair::new;


    public static Function <KeyValuePair<String, String>, KeyValuePair<Long, Long>> newKeyValue_IP_STR_STR_TO_LONG_LONG = (kv)->{
        try{
            return new KeyValuePair<>(IPUtility.ipv4ToLong(kv.getKey()), IPUtility.ipv4ToLong(kv.getValue()));
        }catch (Exception e){
            log.fatal(e.getMessage());
        }
        return null;
    };

    public static Function< KeyValuePair<Long, Long>, KeyValuePair<String, String>> newKeyValuePair_IP_Long_Long_TO_STR_STR = (kv)->{
        try{
            return new KeyValuePair<>(IPUtility.longToipv4String(kv.getKey()), IPUtility.longToipv4String(kv.getValue()));
        }catch (Exception e){
            log.fatal(e.getMessage());
        }
        return null;
    };

    public static Function<String, Long> getLongFromIPString = (s)->{
        try{
            return IPUtility.ipv4ToLong(s);
        }catch (Exception e){
            log.fatal(e.getMessage());
        }
        return null;
    };

    public static Function<Long, String> getIPStringFromLong = (l)->{
        try{
            return IPUtility.longToipv4String(l);
        }catch (Exception e){
            log.fatal(e.getMessage());
        }
        return null;
    };

    public static Function <IPBlockUsage, IPBlock> getIPBlockFromUsage = (usage)-> new IPBlock(usage.getFromIP(), usage.getToIP());


}
