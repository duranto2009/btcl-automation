package test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileUploadAPIServlet extends HttpServlet {

	public void doPost( HttpServletRequest request, HttpServletResponse  response  ) throws IOException, ServletException {
		
		System.out.println( request.getParameter( "doc" ) );
		response.getWriter().write( "aise " + request.getParameter("doc") + " " + request.getPart( "doc" ) );
		return;
	}
}
