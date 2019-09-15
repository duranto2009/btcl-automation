package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class FileUpload extends Action {

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		HttpClient httpClient = new DefaultHttpClient();
	    HttpResponse response1 = null;
	    String result =null;
	    
		System.out.println("In action ");
		
		 HttpPost httppost = new HttpPost("https://www.google.com/search?client=opera&q=asd&sourceid=opera&ie=UTF-8&oe=UTF-8");
		 
		 httppost.addHeader( "referer", "http://localhost:8080" + request.getContextPath() );
	       
		 StringBody docName = new StringBody( "doc");
	    
		 MultipartEntity entity = new MultipartEntity();
		 entity.addPart( "doc", new StringBody("dhrubo" ) );
		 
	        httppost.setEntity(entity);
	        response1 = httpClient.execute(httppost);
	        HttpEntity resEntity = response1.getEntity();
	        if (resEntity != null) {
	           // result = ConvertStreamToString(resEntity.getContent(), "UTF-8");
	            String charset = "UTF-8";   
	          String content=EntityUtils.toString(response1.getEntity(), charset);   
	            System.out.println(content);
	            response.getWriter().write( content );
	        }
	        EntityUtils.consume(resEntity);
	   

	return null;
}}
