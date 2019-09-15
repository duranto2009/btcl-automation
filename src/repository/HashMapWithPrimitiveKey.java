package repository;

import java.util.HashMap;

public class HashMapWithPrimitiveKey  extends HashMap{

	 private int[] hashBuffer;
	 public long key;
	  public synchronized int hashCode()
	  {
	    if(hashBuffer==null)
	      hashBuffer = new int[4];
	    
	    hashBuffer[0] = (byte) ( (key >> 24) & 0x00ff);
	    hashBuffer[1] = (byte) ( (key >> 16) & 0x00ff);
	    hashBuffer[2] = (byte) ( (key >> 8) & 0x00ff);
	    hashBuffer[3] = (byte) ( (key) & 0x00ff);
	    
	    return hashBuffer[3] << 24 | hashBuffer[2] << 16 | hashBuffer[1] << 8 | hashBuffer[0];
	  }
	}
