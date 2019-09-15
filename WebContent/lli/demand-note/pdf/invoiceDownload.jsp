<%@page import="com.lowagie.text.pdf.PdfTable"%>
<%@page import="com.itextpdf.text.Font.FontFamily"%>
<%@page import="java.io.DataOutputStream"%>
<%@page import="java.io.DataOutput"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@page import="callshop.Validators"%>
<%@page import="rateplan.RatePlanRepository"%>
<%@page import="rate.RateRepository"%>
<%@page import="gateway.GatewayRepository"%>
<%@page import="gateway.GatewayDTO"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="com.itextpdf.text.*"%>
<%@page import="com.itextpdf.text.pdf.*"%>
<%@page import="country.CountryRepository"%>
<%@page import="sessionmanager.SessionConstants"%>
<%@page import="client.ClientRepository"%>
<%@page import="java.awt.Color"%>

<%@page import="client.ClientDTO"%><%@ include
	file="../includes/checkLogin.jsp"%>
<%@ page errorPage="../common/failure.jsp"%>

<%
	Logger logger = Logger.getLogger("invoiceDownload_jsp");
	logger.debug("start of download");

	try {

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=" + "abc.pdf");

		BaseColor headerColor = new BaseColor(191, 191, 191);
		BaseColor rowColor = new BaseColor(226, 226, 226);
		BaseColor invoiceColor = new BaseColor(65, 133, 156);

		com.itextpdf.text.Font boldFont_06 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 6, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_07 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 7, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_08 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 8, com.itextpdf.text.Font.BOLD);

		com.itextpdf.text.Font boldFont_09 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 9, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_10 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 10, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_11 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 11, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_12 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 12, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_13 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 13, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_14 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 14, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_15 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 15, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_16 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 16, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_17 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 17, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_18 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 18, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_19 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 19, com.itextpdf.text.Font.BOLD);
		com.itextpdf.text.Font boldFont_20 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 20, com.itextpdf.text.Font.BOLD);

		com.itextpdf.text.Font rowFont_07 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 7, com.itextpdf.text.Font.NORMAL);
		com.itextpdf.text.Font rowFont_08 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 8, com.itextpdf.text.Font.NORMAL);
		com.itextpdf.text.Font rowFont_09 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 9, com.itextpdf.text.Font.NORMAL);
		com.itextpdf.text.Font rowFont_10 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 10, com.itextpdf.text.Font.NORMAL);
		com.itextpdf.text.Font rowFont_12 = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 12, com.itextpdf.text.Font.NORMAL);

		com.itextpdf.text.Font boldFontWithLine = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 9,
				com.itextpdf.text.Font.BOLD | com.itextpdf.text.Font.UNDERLINE);
		com.itextpdf.text.Font justLine = new com.itextpdf.text.Font(
				com.itextpdf.text.Font.getFamily("TIMES_ROMAN"), 9, com.itextpdf.text.Font.UNDERLINE);

		float padding = 0;
		int paddigLeft = 15;
		int paddingRight = 10;
		float linePadding = 3;

		Document document = new Document();
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		PdfWriter.getInstance(document, buffer);
		document.open();

		PdfPTable table = null;
		PdfPTable invheader = null;
		PdfPTable pageTable = null;
		PdfPTable table2 = null;
		PdfPTable emptyTable = null;
		PdfPCell emptyCell = null;

		PdfPCell cell;

		// <-------------------------------- Start of BTCL LOGO ------------------------->

		int tableColumn = 6;
		table = new PdfPTable(tableColumn);
		float[] columnWidth_1 = { 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f };
		table.setWidths(columnWidth_1);

		String logoFileName = getServletContext().getRealPath("").replace("\\", "/");
		logoFileName = logoFileName + "/test/btcl.PNG";
		Image logo = Image.getInstance(logoFileName);
		logo.setWidthPercentage(90f);
		cell = new PdfPCell(logo);
		cell.setColspan(tableColumn);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBorder(0);
		table.addCell(cell);

		document.add(table);
		table.flushContent();
		// <-------------------------------- End of BTCL LOGO ------------------------->

		// <-------------------------------- Start Empty Table ------------------------->
		emptyTable = new PdfPTable(1);
		emptyCell = new PdfPCell(new Phrase(""));
		emptyCell.setFixedHeight(10f);
		emptyCell.setBorder(0);
		emptyTable.addCell(emptyCell);
		document.add(emptyTable);
		emptyTable.flushContent();
		// <----------------------- End of Empty Table --------------------->

		// <---------------------- Start of Invoice To Table -------------------->

		table2 = new PdfPTable(6);
		float[] columnWidth = { 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f };
		table2.setWidths(columnWidth);

		cell = new PdfPCell(new Phrase("Demand Note", boldFont_14));
		cell.setColspan(6);
		cell.setBorder(0);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setPaddingBottom(padding);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoices To,", boldFont_11));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(6);
		cell.setBorder(Rectangle.BOTTOM);
		BaseColor borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		// <---------------------------- Table2 Row1 ------------------------------------->

		cell = new PdfPCell(new Phrase("Registration Name", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Mr. Delwar H. khan", rowFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(0);
		table2.addCell(cell);

		// <----------------------------  Table2 Row2  ------------------------------------->

		cell = new PdfPCell(new Phrase("Login UserName", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("gononetcom", rowFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(0);
		table2.addCell(cell);

		// <----------------------------  Table2 Row3  ------------------------------------->

		cell = new PdfPCell(new Phrase("Registrant Email", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("dhk512@aol.com", rowFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(0);
		table2.addCell(cell);

		// <----------------------------  Table2 Row4  ------------------------------------->

		cell = new PdfPCell(new Phrase("Registrant Contact", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Registarnt Contact info", rowFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(0);
		table2.addCell(cell);

		// <----------------------------  Table2 Row5  ------------------------------------->

		cell = new PdfPCell(new Phrase("Registrant Address", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Khaja Tower (9th Floor),\n95 Mohakhali C/A,\nDhaka-1212", rowFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		document.add(table2);
		table2.flushContent();

		// <---------------------- End of Invoice To Table -------------------->

		// <-------------------------------- Start Empty Table ------------------------->
		emptyTable = new PdfPTable(1);
		emptyCell = new PdfPCell(new Phrase(""));
		emptyCell.setFixedHeight(10f);
		emptyCell.setBorder(0);
		emptyTable.addCell(emptyCell);
		document.add(emptyTable);
		emptyTable.flushContent();
		// <----------------------- End of Empty Table --------------------->

		// <---------------------- Start of Invoice To Table Header -------------------->
		tableColumn = 12;
		table2 = new PdfPTable(tableColumn);
		float[] columnWidth_3 = { 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f };
		table2.setWidths(columnWidth_3);

		cell = new PdfPCell(new Phrase("Invoice ID : 243797", boldFont_13));
		cell.setColspan(tableColumn);
		cell.setBorder(0);
		cell.setVerticalAlignment(Element.ALIGN_TOP);
		cell.setPaddingBottom(padding);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(0);
		cell.setFixedHeight(5f);
		cell.setColspan(tableColumn);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoice Date", boldFont_11));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_13));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("February 13, 2018", rowFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(8);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Invoice Validity*", boldFont_11));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":", boldFont_13));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("February 13, 2019", rowFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(3);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("*Please pay within the validity of the invoice", rowFont_08));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(5);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		document.add(table2);
		table2.flushContent();

		// <---------------------- End of Invoice To Table Header -------------------->

		// <----------------------- Start of Invoice Table -------------------------->
		tableColumn = 4;
		table2 = new PdfPTable(tableColumn);
		float[] columnWidth_4 = { 1.2f, 1.2f, 1.2f, 1.2f };
		table2.setWidths(columnWidth_4);

		cell = new PdfPCell(new Phrase(""));
		cell.setBorder(0);
		cell.setFixedHeight(3f);
		cell.setColspan(tableColumn);
		table2.addCell(cell);

		BaseColor bgcolor = new BaseColor(207, 238, 179);

		// <--------------------------- Row1 --------------------------->
		cell = new PdfPCell(new Phrase("Name", boldFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		//cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Description", boldFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		//cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Details", boldFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		//cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Amount(BDT)", boldFont_10));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		//cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);
		
		

		// <--------------------------- Row2 --------------------------->

		cell = new PdfPCell(new Phrase("Applicatioin ID", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":1000", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Security Deposit", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("40050)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row3 --------------------------->

		cell = new PdfPCell(new Phrase("Application Type", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":New Connection", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Registration Charge", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("5000)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row4 --------------------------->

		cell = new PdfPCell(new Phrase("Connection Type", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":Regular", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("OTC Charge", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("5000)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row5 --------------------------->

		cell = new PdfPCell(new Phrase("Bandwidth (Mbps) ", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":40", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Bandwidth Charge", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("36000", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row6 --------------------------->

		cell = new PdfPCell(new Phrase("BTCL Distance (Meter)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":1289", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Others iTem: Cable", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("390", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row7 --------------------------->

		cell = new PdfPCell(new Phrase("OC Distance (Meter)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":0", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Advance Adjustment", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("0", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row8 --------------------------->

		cell = new PdfPCell(new Phrase("Core Type ", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":Dual", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Discount (0%)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("0", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		// <--------------------------- Row9 --------------------------->

		cell = new PdfPCell(new Phrase("TD Date* ", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(":February 27, 2019", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("VAT ( 36,000*15%)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("5,400", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("*Please pay before expiration to continue the common", boldFont_07));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("", boldFont_07));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Total Amount(BDT)", boldFont_09));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("136851)", boldFont_09));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(
				new Phrase("Amount in words: BDT one hundred thirty six thousand eight hundred fifty one only)",
						boldFont_09));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(.5f);
		table2.addCell(cell);

		Font green = new Font(FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GREEN);
		Font green_8 = new Font(FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GREEN);
		Font blue_8 = new Font(FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.BLUE);

		cell = new PdfPCell(new Phrase(
				"Please pay either using Teletalk online payment or by Manual Bank Payment.", green));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		document.add(table2);
		table2.flushContent();

		// <----------------------- End of Invoice Table --------------------------->

		// <----------------------- Start of Instruction Table --------------------------->

		tableColumn = 4;
		table2 = new PdfPTable(tableColumn);
		float[] columnWidth_5 = { 1.2f, 1.2f, 1.2f, 1.2f };
		table2.setWidths(columnWidth_4);

		cell = new PdfPCell(new Phrase("Teletalk payment* steps:", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		/*
		
		Paragraph paragraph = new Paragraph();
		Chunk chunk1 = new Chunk("1.Recharge ", rowFont_08);
		Chunk chunk2 = new Chunk("BDT 136,851.0",boldFont_08);
		Chunk chunk3 = new Chunk(" at Teletalk prepaid SIM.", rowFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);
		
		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Details", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(.5f);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Amount(BDT)", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(.5f);
		cell.setBackgroundColor(bgcolor);
		table2.addCell(cell);
		
		
		
		paragraph = new Paragraph();
		chunk1 = new Chunk("2.Type BTCL<space>", rowFont_08);
		chunk2 = new Chunk("245002 ",boldFont_08);
		chunk3 = new Chunk(" and then send to 16222. You'll receive a ", rowFont_08);
		Chunk chunk4 = new Chunk(" 8(eight) digit", boldFont_08);
		Chunk chunk5 = new Chunk(" PIN number in return SMS at mobile", rowFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);
		paragraph.add(chunk4);
		paragraph.add(chunk5);
		
		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);
		
		PdfPTable nested = new PdfPTable(2);
		float[] columnWidth_11 = {1.2f, 1.2f };
		nested.setWidths(columnWidth_11);
		
		cell = new PdfPCell(new Phrase("Total Amount", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);
		
		cell = new PdfPCell(new Phrase("136,851.0", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);
		
		
		
		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(nested);
		table2.addCell(cell);
		
		*/

		Paragraph paragraph = new Paragraph();
		Chunk chunk1 = new Chunk("1.Recharge ", rowFont_08);
		Chunk chunk2 = new Chunk("BDT 136,851.0", boldFont_08);
		Chunk chunk3 = new Chunk(" at Teletalk prepaid SIM.", rowFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);

		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);

		// --------------- nested table ---------
		PdfPTable nested = new PdfPTable(2);
		float[] columnWidth_11 = { 2f, 2f };
		nested.setWidths(columnWidth_11);

		cell = new PdfPCell(new Phrase("Details", boldFont_12));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(.5f);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("Amount(BDT)", boldFont_12));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(Rectangle.BOTTOM);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(.5f);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("Total Amount", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("136,851.0", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("Teletalk Service Charge(10%)", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("260", rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("Total payable using teletalk", rowFont_08));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell(new Phrase("137,111", rowFont_08));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		cell.setBackgroundColor(bgcolor);
		nested.addCell(cell);

		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setRowspan(2);
		cell.setBorder(0);
		cell.addElement(nested);
		table2.addCell(cell);

		paragraph = new Paragraph();
		chunk1 = new Chunk("2.Type BTCL<space>", rowFont_08);
		chunk2 = new Chunk("245002 ", boldFont_08);
		chunk3 = new Chunk(" and then send to 16222. You'll receive a ", rowFont_08);
		Chunk chunk4 = new Chunk(" 8(eight) digit", boldFont_08);
		Chunk chunk5 = new Chunk(" PIN number in return SMS at mobile", rowFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);
		paragraph.add(chunk4);
		paragraph.add(chunk5);

		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);

		paragraph = new Paragraph();
		chunk1 = new Chunk("3. To confirm, type ", rowFont_08);
		chunk2 = new Chunk(" BTCL YES ", boldFont_08);
		chunk3 = new Chunk("<space>", rowFont_08);
		chunk4 = new Chunk(" 8 digit pin ", boldFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);
		paragraph.add(chunk4);

		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);

		paragraph = new Paragraph();
		chunk1 = new Chunk("* At most ", rowFont_08);
		chunk2 = new Chunk("BDT 8900 ", boldFont_08);
		chunk3 = new Chunk(" s allowed to pay using Teletalk ", rowFont_08);
		paragraph.add(chunk1);
		paragraph.add(chunk2);
		paragraph.add(chunk3);

		cell = new PdfPCell();
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.addElement(paragraph);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Manual bank payment procedure", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Payment using Bank Cheque/Pay Order", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"1. Pay the cash amount, 136,851 BDT at Social Islami Bank Limited (SIBL), Eskaton branch, Moghbazar, Dhaka.",
				rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"1. Only cheque (For Govt. offices) or Pay order (For non Govt. offices & others organizations) is allowed.",
				rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"2. Please bring a copy of bank receipt of this invoice to Senior Accounts Officer (SAO), Moghbazar Telephone bhaban, Dhaka-1217",
				rowFont_08));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("2. Particulars should be sent in favor of senior Account", rowFont_08));
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(2);
		cell.setBorder(0);
		table2.addCell(cell);

		// --------------- end of nested table ---------

		document.add(table2);
		table2.flushContent();

		// <----------------------- End of Instruction Table --------------------------->

		// ------------------------ start of link section ------------------------
		tableColumn = 4;
		table2 = new PdfPTable(tableColumn);
		float[] columnWidth_8 = { 1.2f, 1.2f, 1.2f, 1.2f };
		table2.setWidths(columnWidth_8);

		cell = new PdfPCell(new Phrase("", boldFont_12));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(Rectangle.BOTTOM);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Important Links & Facts:", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		Phrase phrase = new Phrase();
		chunk1 = new Chunk("LLI Profile Sign up/Login URL", rowFont_08);
		Chunk chunk = new Chunk("http://bdia.btcl.com.bd/", blue_8);
		chunk.setAnchor("http://bdia.btcl.com.bd/");
		phrase.add(chunk1);
		phrase.add(chunk);

		cell = new PdfPCell(phrase);
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		phrase = new Phrase();
		chunk1 = new Chunk("LLI and Rate info", rowFont_08);
		chunk = new Chunk("http://www.btcl.com.bd/common/details/en/19", blue_8);
		chunk.setAnchor("http://www.btcl.com.bd/common/details/en/19");
		phrase.add(chunk1);
		phrase.add(chunk);

		cell = new PdfPCell(phrase);
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		phrase = new Phrase();
		chunk1 = new Chunk("LLI renewal step details:", rowFont_08);
		chunk = new Chunk("https://goo.gl/qD1MnW", blue_8);
		chunk.setAnchor("https://goo.gl/qD1MnW");
		phrase.add(chunk1);
		phrase.add(chunk);

		cell = new PdfPCell(phrase);
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		phrase = new Phrase();
		chunk1 = new Chunk("Userguide of LLI registration & management", rowFont_08);
		chunk = new Chunk("https://goo.gl/ksdvdy", blue_8);
		chunk.setAnchor("https://goo.gl/ksdvdy");
		phrase.add(chunk1);
		phrase.add(chunk);

		cell = new PdfPCell(phrase);
		cell.setPaddingBottom(linePadding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(4);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell();
		cell.setBorder(Rectangle.BOTTOM);
		cell.setPaddingBottom(linePadding);
		borderColor = new BaseColor(115, 208, 134);
		cell.setBorderColor(borderColor);
		cell.setBorderWidthBottom(1f);
		cell.setColspan(4);
		table2.addCell(cell);

		document.add(table2);
		table2.flushContent();

		// --------------------- end of link section ------------------

		// --------------------- Start of Important facts section ------------------

		tableColumn = 6;
		table = new PdfPTable(tableColumn);
		float[] columnWidth_9 = { 1.2f, 1.2f, 1.2f, 1.2f, 1.2f, 1.2f };
		table.setWidths(columnWidth_9);

		cell = new PdfPCell(new Phrase("Important facts:", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(tableColumn);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"1. Any sort of payment are non-refundable. So, please know details of common charge & proceed to",
				rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(tableColumn);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"2. If intentionally, unintentionally or by mistakenly advanced amount which cannot be rounded with the amount, will be treated as credit balance. ",
				rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(tableColumn);
		cell.setBorder(0);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase(
				"3. BTCL may temporarily block if payment is not received or partially/incompletely received.",
				rowFont_08));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(tableColumn);
		cell.setBorder(0);
		table2.addCell(cell);

		document.add(table2);
		table2.flushContent();
		
		// --------------------- End of Important facts section ------------------
		
		
		// --------------------- Start of Signature section ------------------
		
		tableColumn = 2;
		table2 = new PdfPTable(tableColumn);
		float[] columnWidth_12 = { 1.2f, 1.2f};
		table2.setWidths(columnWidth_12);
		
		
		cell = new PdfPCell(new Phrase("On behalf of Registrant,", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("For Bank Usages-", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Name:", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Signature & Date", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Signature & Date", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Powered By Reve Systems", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		cell = new PdfPCell(new Phrase("Bangladesh Telecommunications Company Limited", boldFont_10));
		cell.setPaddingBottom(padding);
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(1);
		cell.setBorder(0);
		table2.addCell(cell);
		
		
		
		document.add(table2);
		table2.flushContent();
		
		// --------------------- End of Signature section ------------------
		
		
		
		
		document.close();
		DataOutput dataOutput = new DataOutputStream(response.getOutputStream());
		byte[] bytes = buffer.toByteArray();
		response.setContentLength(bytes.length);
		for (int i = 0; i < bytes.length; i++) {
			dataOutput.writeByte(bytes[i]);
		}
		response.getOutputStream().flush();
		response.getOutputStream().close();
		return;

	} catch (Exception e) {
		logger.debug("exception:", e);
	}
%>

