<%@page import="file.FileTypeConstants"%>
<%@page import="org.apache.commons.io.IOUtils"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%

	response.setContentType("application/pdf");
	response.setHeader("Content-Disposition", "inline; filename=userguide.pdf"); 
	response.setCharacterEncoding("UTF8");
	String filePath = FileTypeConstants.BASE_PATH + "userguide.pdf" ;
	
	FileInputStream inputStream = new FileInputStream( new File( filePath ) );
	IOUtils.copy( inputStream , response.getOutputStream() );
	inputStream.close();
	response.flushBuffer();



%>
