package payment.api;

import java.io.*;

public class CopyPrintWriter extends PrintWriter {

    private StringBuilder copy = new StringBuilder();
    private Long responseTime;

    public CopyPrintWriter(Writer writer) {
        super(writer);
    }

    @Override
    public void write(int c) {
        copy.append((char) c); // It is actually a char, not an int.
        super.write(c);
        responseTime = System.currentTimeMillis();
    }

    @Override
    public void write(char[] chars, int offset, int length) {
        copy.append(chars, offset, length);
        super.write(chars, offset, length);
        responseTime = System.currentTimeMillis();
    }

    @Override
    public void write(String string, int offset, int length) {
        copy.append(string, offset, length);
        super.write(string, offset, length);
        responseTime = System.currentTimeMillis();
    }
    

    public String getResponseBody() {
        return copy.toString();
    }
    public Long getResponseTime(){
    	return responseTime;
    }

}