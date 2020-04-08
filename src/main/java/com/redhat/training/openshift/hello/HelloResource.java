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

@Path("/")
public class HelloResource {

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
          greeting = "Hello from host "+hostname+"\n";
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
        StringBuffer sbuf = new StringBuffer("<html><head><form method=POST name=javaHostForm action=''>" + msg);
        sbuf.append("<br><br>https (Y/N) <input type=text name=ssl/> ");         
        sbuf.append("domain (www.google.com) <input type=text name=domain/> ");         
        sbuf.append("port (443) <input type=text name=port/> ");         
        sbuf.append("URI (/queryString/example) <input type=text name=queryString/> ");         
        sbuf.append("<br><br>For action <input type=text name=formAction/>");         
        sbuf.append("<br><br>For action <input type=text name=formAction/>");         
        sbuf.append("<input type=submit name=submit/>");         
        sbuf.append("</form></head></html>");                 
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
