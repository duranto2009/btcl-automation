<%@page import="file.FileTypeConstants"%>
<%@page import="file.FileService"%>
<%@page import="common.CommonService"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="file.FileDAO"%>
<%@page import="file.FileDTO"%>
<%@include file="../includes/checkLogin.jsp"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="login.LoginDTO"%>
<%@ page import="javax.swing.text.AbstractDocument.Content"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="databasemanager.DatabaseManager"%>
<%@ taglib uri="../WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="../WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="../WEB-INF/struts-logic.tld" prefix="logic"%>
<!DOCTYPE html>

<html>
<head>
<html:base />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body>

	<%
		Logger logger = Logger.getLogger(CommonService.class);
		String entityTypeID = request.getParameter("et");
		String entityID = request.getParameter("e");
		
		FileService fileService = new FileService();
		ArrayList<FileDTO> fileDTOs = fileService.getFileByEntityTypeAndEntity(Integer.parseInt(entityTypeID),Long.parseLong(entityID));
		FileDTO fileDTO = new FileDTO();
		if(fileDTOs.size()> 0)
			fileDTO = fileDTOs.get(0);

		String filepath = FileTypeConstants.FINAL_UPLOAD_DIR+ fileDTO.getDirectory()+ fileDTO.getDocLocalFileName();
		System.out.println(filepath);


		File file = new File(filepath);
		byte[] fileBytes = null;
		if (file.isDirectory() || !file.exists()) {
	%>
			<jsp:forward page="../common/failure.jsp"></jsp:forward>
	<%
		} else {
			FileInputStream fin = new FileInputStream(file);
			fileBytes = new byte[(int) file.length()];
			fin.read(fileBytes);
			//fileBytes = rs.getBytes(2);
		}

		String fileType = fileDTO.getDocLocalFileName().substring(fileDTO.getDocLocalFileName().indexOf(".") + 1, fileDTO.getDocLocalFileName().length());
		if (fileType.trim().equalsIgnoreCase("pdf")) {
			response.setContentType("application/pdf");
		} else if (fileType.trim().equalsIgnoreCase("doc")) {
			response.setContentType("application/msword");
		} else if (fileType.trim().equalsIgnoreCase("xls")) {
			response.setContentType("application/vnd.ms-excel");
		} else if (fileType.trim().equalsIgnoreCase("ppt")) {
			response.setContentType("application/ppt");
		} else if (fileType.trim().equalsIgnoreCase("txt")) {
			response.setContentType("text/plain");
		} else {
			response.setContentType("application/octet-stream");
		}
		response.setHeader("Content-disposition", "attachment;filename=" + fileDTO.getDocActualFileName());
		response.setHeader("cache-control", "must-revalidate");
		try {
			ServletOutputStream os = response.getOutputStream();
			os.write(fileBytes);
			os.flush();
			os.close();
			return;
		} catch (Exception ex) {
			logger.debug("file download : ignore ex message "+ ex);
		}
	%>
</body>
</html>