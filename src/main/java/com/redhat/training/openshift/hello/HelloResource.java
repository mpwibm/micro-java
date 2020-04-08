package com.redhat.training.openshift.hello;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.net.MalformedURLException;
import org.apache.http.Header; 
import org.apache.http.HttpEntity; 
import org.apache.http.HttpHeaders; 
import org.apache.http.NameValuePair; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.CloseableHttpResponse; 
import org.apache.http.client.methods.HttpGet; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.impl.client.CloseableHttpClient; 
import org.apache.http.impl.client.HttpClients; 
import org.apache.http.message.BasicNameValuePair; 
import org.apache.http.util.EntityUtils;  
import java.io.IOException;
import java.io.*;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/")
public class HelloResource {

@SuppressWarnings("serial")
@WebServlet("/HelloWorld")
import com.redhat.training.openshift.hello.HelloServlet.HttpClientExample;

public class HelloServlet extends HttpServlet {

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response)
               throws IOException, ServletException {
 
          String ssl = request.getParameter("ssl");
          String domain = request.getParameter("domain");
          String port = request.getParameter("port");
          String uri = request.getParameter("uri");
          String queryString = request.getParameter("queryString");
          String method = request.getParameter("method");

          if (method == null) {
                method = "GET";
	  }
          // Set the response message's MIME type

      response.setContentType("text/html;charset=UTF-8");
      // Allocate a output writer to write the response message into the network socket
      PrintWriter out = response.getWriter();
      StringBuffer sbuf = new StringBuffer("");
      //String message = null;

      if (domain == null || domain.equals("")) {
         domain = "bbk.unionbank.com/users/ub44203/";
         sbuf.append("hard coding domain to " + domain + "<br>");
      } else {
         sbuf.append("domain pulled from request = " + domain + "<br>");
      }

      String urlURI = domain;
      String search = "search string";
      // Write the response message, in an HTML page

      try {
          String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
          String message = System.getenv().getOrDefault("APP_MSG", null);
          String greeting = "";
          String protocol = "https://";
                    if (ssl == "N") {
                          protocol = "http://";
                    }
                    if (message == null) {
                      greeting = "App Red from Hostname: "+hostname+ "\n";
                    } else {
                      greeting = "Hello from host ["+hostname+"].\n";
                      greeting += "Message received = "+message+"\n";
                    }
                    String msg = "";

                    HttpClientExample obj = new HttpClientExample();

                    try {

                       System.out.println("Testing Case 1 - Send Http GET request");

                       if (method == "GET") {
                           msg = obj.sendGet(urlURI, protocol);
                       } else {
                           msg = obj.sendGet(urlURI, protocol);
                       }
                    } finally {
                       obj.close();
                    }
    
          sbuf.append("<!DOCTYPE html>");
          sbuf.append("<html><head>");
          sbuf.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
          sbuf.append("<title>MUFG Java Server Host Demo</title></head>");
          sbuf.append("<body>");
          sbuf.append("<h1>MUFG Java Server Host Demo</h1>");  // says Hello

          // Echo client's request information    
          sbuf.append("<p>Request URI: " + request.getRequestURI() + "</p>");
          sbuf.append("<p>Protocol: " + request.getProtocol() + "</p>");
          sbuf.append("<p>PathInfo: " + request.getPathInfo() + "</p>");
          sbuf.append("<p>Remote Address: " + request.getRemoteAddr() + "</p>");
          // Generate a random number upon each request
          sbuf.append("<p>A Random Number: <strong>" + Math.random() + "</strong></p>");
          sbuf.append("<form method=POST name=javaHostForm action=''>");
          sbuf.append("<br><br>ssl (Y/N) <input type=text name=ssl value=\"Y\"/> ");        
          sbuf.append("domain (www.google.com) <input type=text name=domain/> ");         
          sbuf.append("port (443) <input type=text name=port/> ");        
          sbuf.append("uri (/uri) <input type=text name=uri/> ");        
          sbuf.append("query string (?key1=val1&key2=val2) <input type=text name=queryString/> ");         
          sbuf.append("uri (/uri) <input type=text name=method value=\"GET\"/> ");        
          sbuf.append("<input type=submit name=submit/>");        
          sbuf.append("</form></head></html>" + msg);                
          // sbuf.append("</form></head></html>");
          sbuf.append("</body>");
          sbuf.append("</html>");
          //message = sbuf.toString();   
          System.out.println("TESTING - HTTP GET URI SERVICE");
          out.println(sbuf.toString());
      } finally {
         out.close();  // Always close the output writer
      }
   }
       private static class HttpClientExample {
             // one instance, reuse
             private final CloseableHttpClient httpClient = HttpClients.createDefault();
             private void close() throws IOException {
                    httpClient.close();
             }
             private String sendGet(String URL, String protocol) throws Exception {
                    HttpGet request = new HttpGet(protocol + URL);
                    // add request headers
                    request.addHeader("custom-key", search);
                    request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
                    String result = "";
                    String status = "";
                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                          // Get HttpResponse Status
                          System.out.println(response.getStatusLine().toString());
                          status = response.getStatusLine().toString();
                          HttpEntity entity = response.getEntity();
                          Header headers = entity.getContentType();
                          System.out.println(headers);
                          if (entity != null) {
                                 // return it as a String
                                 result = EntityUtils.toString(entity);
                                 System.out.println(result);
                          }
                    }
                    return "STATUS: \n" + status + "\n" + "RESULT: \n" + result;o
             }

             private String sendGetOrig(String URL, String ssl, String search) throws Exception {
                    String protocol = "";
                    if (ssl == "Y") {
                          protocol = "https://";
                    } else {
                          protocol = "http://";
                    }
                    HttpGet request = new HttpGet(protocol + URL + "/search?q=" + search);
                    // add request headers
                    request.addHeader("custom-key", search);
                    request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
                    String result = "";
                    String status = "";
                    try (CloseableHttpResponse response = httpClient.execute(request)) {
                          // Get HttpResponse Status
                          System.out.println(response.getStatusLine().toString());
                          status = response.getStatusLine().toString();
                          HttpEntity entity = response.getEntity();
                          Header headers = entity.getContentType();
                          System.out.println(headers);
                          if (entity != null) {
                                 // return it as a String
                                 result = EntityUtils.toString(entity);
                                 System.out.println(result);
                          }
                    }
                    return "STATUS: \n" + status + "\n" + "RESULT: \n" + result;
             }
       }
}
    @GET
    @Path("/hello")
    @Produces("text/plain")
    public String hello() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
              String message = System.getenv().getOrDefault("APP_MSG", null);
	      String response = "";

      	if (message == null) {
      	  response = "Hello world from host "+hostname+"\n";
      	} else {
      	  response = "Hello world from host ["+hostname+"].\n";
      	  response += "Message received = "+message+"\n";
        }
        return response;
    }
    @GET
    @Path("/goodbye")
    @Produces("text/plain")
    public String goodbye() {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
              String message = System.getenv().getOrDefault("APP_MSG", null);
              String response = "";

        if (message == null) {
          response = "Goodbye from host "+hostname+"\n";
        } else {
          response = "Goodbye from host ["+hostname+"].\n";
          response += "Message received = "+message+"\n";
        }
        return response;
    }
    @GET
    @Path("/geturi/{urlURI}/{method}/{ssl}/{search}")
    @Produces("text/html")
    public Response GetPostUriService(@PathParam("urlURI") String urlURI, @PathParam("method") String method,                            @PathParam("ssl") String ssl, @PathParam("search") String search) throws Exception {
        String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");
              String message = System.getenv().getOrDefault("APP_MSG", null);
              String greeting = "";

        if (message == null) {
          greeting = "App Red from Hostname: "+hostname+ "\n";
        } else {
          greeting = "Hello from host ["+hostname+"].\n";
          greeting += "Message received = "+message+"\n";
        }
        String msg = "";
        HttpClientExample obj = new HttpClientExample();
        try {
           System.out.println("Testing Case 1 - Send Http GET request");
           if (method == "GET") {
               msg = obj.sendGet(urlURI, ssl, search);
           } else {
               msg = obj.sendGet(urlURI, ssl, search);
           }
        } finally {
           obj.close();
        }
        StringBuffer sbuf = new StringBuffer("<html><head><form method=POST name=javaHostForm action=''>");
        sbuf.append("<br><br>https (Y/N) <input type=text name=ssl/> ");         
        sbuf.append("domain (www.google.com) <input type=text name=domain/> ");         
        sbuf.append("port (443) <input type=text name=port/> ");         
        sbuf.append("URI (/queryString/example) <input type=text name=queryString/> ");         
        sbuf.append("<br><br>For action <input type=text name=formAction/>");         
        sbuf.append("<br><br>For action <input type=text name=formAction/>");         
        sbuf.append("<input type=submit name=submit/>");         
        sbuf.append("</form></head></html>" + msg);                 
        message = sbuf.toString();    
        System.out.println("TESTING - HTTP GET URI SERVICE");
        return Response.status(200).entity(greeting + "\n" + " URL : " + urlURI + "\n" + "\n" + message).build();
    }

   private static class HttpClientExample {      
       // one instance, reuse     
       private final CloseableHttpClient httpClient = HttpClients.createDefault();      

    private void close() throws IOException {         
        httpClient.close();     
    } 

    private String sendGet(String URL, String ssl, String search) throws Exception {   
        String protocol = "";
        if (ssl == "Y") {
            protocol = "https://";
        } else {
            protocol = "http://";
        }      
        HttpGet request = new HttpGet(protocol + URL + "/search?q=" + search);          
        
        // add request headers         
        request.addHeader("custom-key", search);         
        request.addHeader(HttpHeaders.USER_AGENT, "Googlebot");
        String result = "";  
        String status = "";
        try (CloseableHttpResponse response = httpClient.execute(request)) {              
            // Get HttpResponse Status             
            System.out.println(response.getStatusLine().toString());              
            status = response.getStatusLine().toString();
            HttpEntity entity = response.getEntity();             
            Header headers = entity.getContentType();             
            System.out.println(headers);              
            if (entity != null) {                 
                // return it as a String                 
                result = EntityUtils.toString(entity);                 
                System.out.println(result);             
            } 
        }
      return "STATUS: \n" + status + "\n" + "RESULT: \n" + result;
    }
  }
}
