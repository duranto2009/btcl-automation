package test.junit;

import org.junit.Test;

import validator.DTOValidator;
import validator.annotation.Min;

import static org.junit.Assert.assertEquals;

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
