package jUnitTesting;

import org.junit.Test;

import common.EntityTypeConstant;
import connection.DatabaseConnection;
import login.LoginDTO;
import permission.PermissionDAO;
import request.CommonRequestDTO;
import validator.DTOValidator;
import validator.PojoValidator;
import validator.annotation.Min;
import vpn.constants.VpnRequestTypeConstants;
import vpn.constants.VpnStateConstants;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class TestJunit {
	@Min(fieldName="A")
	int a=-1;
	int b;
	
  @Test
   public void testAdd() {
      String str = "Junit is working fine";
      assertEquals("Junit is working fine",str);
   }
   
   @Test
   public void testSum() {
	   int a=10;
	   int b=20;
	   int c=a+b;
      assertEquals(30,c);
   }
   
   public static void main(String[] args) throws Exception{
	   DTOValidator.validate(new TestJunit());
	   System.out.println("Done");
   }
   
}
