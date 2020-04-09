package com.redhat.training.openshift.hello;

 

import java.io.IOException;

import java.io.PrintWriter;

import java.util.Enumeration;

import javax.servlet.ServletException;

import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServlet;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;

import org.apache.http.HttpEntity;

import org.apache.http.HttpHeaders;

import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.client.methods.HttpGet;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;

import org.apache.http.util.EntityUtils;

import java.util.Base64.*;

@SuppressWarnings("serial")

@WebServlet("/JavaServerDemo")

public class HelloServlet extends HttpServlet {

      

          public String getParameterStr(HttpServletRequest request, String paramName, String defaultVal) {

                String str = request.getParameter(paramName);

           if (str == null || str.trim().equalsIgnoreCase("")) {

              str = defaultVal;

           }

           return str;

          }

 

       public void doGetOrPost(HttpServletRequest request, HttpServletResponse response, String doGetOrPost)

                    throws IOException, ServletException {

 

             String ssl = getParameterStr(request, "ssl", "Y");

             String domain = getParameterStr(request, "domain", "");

             String port = getParameterStr(request, "port", "");

             String uri = getParameterStr(request, "uri", "");

             String queryString = getParameterStr(request, "queryString", "");

             String method = getParameterStr(request, "method", "GET");

             String debugString = getParameterStr(request, "debug", "no");

 

             // Set the response message's MIME type

             response.setContentType("text/html;charset=UTF-8");

             // Allocate a output writer to write the response message into the network socket

             PrintWriter out = response.getWriter();

             StringBuffer sbuf = new StringBuffer("");

             //String message = null;

             sbuf.append("<!DOCTYPE html>");

             sbuf.append("<html><head>");

             sbuf.append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");

             sbuf.append("<title>MUFG Java Server Host Demo</title></head>");

             sbuf.append("<body>");

             sbuf.append("<h1>MUFG Java Server Host Demo</h1>");  // says Hello

            

             boolean DEBUG = false;

             if (debugString != null && debugString.trim().toLowerCase().startsWith("y")) {

               DEBUG = true;

             }

             if (DEBUG) {

             Enumeration<String> parmNames = request.getParameterNames();

 

             while (parmNames != null && parmNames.hasMoreElements()) {

                    String param = (String) parmNames.nextElement();

                    if (param != null) {

                          String value = request.getParameter(param);

                          if (value != null) {

                                 sbuf.append(doGetOrPost + " HttpServletRequest.getParameter(" + param + ") : " + value + "<br>");

                          } else {

                                 sbuf.append(doGetOrPost + " HttpServletRequest.getParameter(" + param + ") : null <br>");

                          }

                    } else {

                          sbuf.append(doGetOrPost + " HttpServletRequest.getParameter(param is null) <br>");

                    }

             }

             } // end if (DEBUG)

             StringBuffer urlUriSbuf = new StringBuffer();

             if (domain == null || domain.equals("")) {

                    domain = "bbk.unionbank.com";

                    urlUriSbuf.append(domain);

                    sbuf.append(doGetOrPost + " - No domain in HttpServleyRequest; hard coding domain to " + domain + "<br>");

             } else {

                    sbuf.append(doGetOrPost + " - domain pulled from request = " + domain + "<br>");

                    urlUriSbuf.append(domain);

             }

 

             String urlURI = domain;

             // Write the response message, in an HTML page

             try {

                    String hostname = System.getenv().getOrDefault("HOSTNAME", "unknown");

                    String message = System.getenv().getOrDefault("APP_MSG", null);

                    String namespace = System.getenv().getOrDefault("KUBERNETES_NAMESPACE", null);

                    String serviceip = System.getenv().getOrDefault("KUBERNETES_SERVICE_HOST", null);

                    String podip = System.getenv().getOrDefault("HELLO_SERVICE_HOST", null);

                    String greeting = "";

 

                    greeting = "<h2>Openshift Project (Namespace): "+namespace+"<br> POD Host name: "+hostname+"<br> Cluster IP / Service IP (for POD communication): "+serviceip+"<br> Pod IP / Container IP: "+podip+"<br>";

 

                    if (message != null && !message.trim().equalsIgnoreCase("")) {

                           greeting += "Message received = "+message+"<br>";

                    }

                    greeting += "</h2>";

 

                    sbuf.append("<br><br>Greeting: " + greeting+ "</form><br><br>");

    

 

                    String egressResponse = "";

                    HttpClientExample obj = new HttpClientExample();

                    String protocol = "https://";

                    try {

                          System.out.println("Testing Case 1 - Send Http GET request");

                          if (ssl == "N") {

                                 protocol = "http://";

                          }

                          if (method == "GET") {

                                 egressResponse = obj.sendGet(urlURI, protocol, uri, queryString);

                          } else {

                                 egressResponse = obj.sendGet(urlURI, protocol, uri, queryString);

                          }

                    } catch (Exception e) {

                          // System.out.println(e.printStackTrace());

                          sbuf.append("Exception obj.sendGet(" + protocol + urlURI + "): " + e.getClass().toString() + ": " + e.getMessage() + "<br>");

                    }  finally {

                          obj.close();

                    }

                    // Echo client's request information  

                    //sbuf.append("<p>Request URI: " + request.getRequestURI() + "</p>");

                    //sbuf.append("<p>Protocol: " + request.getProtocol() + "</p>");

                    //sbuf.append("<p>PathInfo: " + request.getPathInfo() + "</p>");

                    //sbuf.append("<p>Remote Address: " + request.getRemoteAddr() + "</p>");

                    // Generate a random number upon each request

                    //sbuf.append("<p>A Random Number: <strong>" + Math.random() + "</strong></p>");

                    if (method != null && method.equalsIgnoreCase("POST")) {

                          sbuf.append("<form method=POST name=javaHostForm action=''>");

                    } else {

                          sbuf.append("<form method=GET name=javaHostForm action=''>");

                    }

                    sbuf.append("<br><br>ssl (Y/N) <input type=text name=ssl value=\""+ ssl +"\" /> ");      

                    sbuf.append("<br>domain (www.google.com) <input type=text name=domain value=\""+ domain +"\" /> ");      

                    sbuf.append("port (443) <input type=text name=port value=\""+ port +"\" /> ");      

                    sbuf.append("<br>uri (/search?q=) <input type=text name=uri value=\""+ uri +"\" /> ");      

                    sbuf.append("<br>query string (?key1=val1&key2=val2) <input type=text name=queryString value=\""+ queryString +"\" /> ");              

                    sbuf.append("<br>method (GET or POST) <input type=text name=method value=\""+ method +"\" /> ");      

                    sbuf.append("<br>DEBUG (y or yes or no) <input type=text name=debug value=\""+ DEBUG +"\" /> ");      

 

                    sbuf.append("<input type=submit name=submit />");      

                    // sbuf.append("<br><br>Greeting: " + greeting+ "</form><br><br>");

                    if (egressResponse != null) {

                          sbuf.append("Displaying egress Response for "  + protocol + urlURI +  ": <br><br>");

                          sbuf.append(egressResponse);

                    } else {

                          sbuf.append("obj.sendGet(" + protocol + urlURI + "): egress Response is null <br><br>");

                    }

                    sbuf.append("</body></html>");

                    //message = sbuf.toString(); 

                    System.out.println("TESTING - HTTP GET URI SERVICE");

                    out.println(sbuf.toString());

             } finally {

 

                    out.close();  // Always close the output writer

 

             }

 

       } // doGetOrPost

 

       @Override

       public void doGet(HttpServletRequest request, HttpServletResponse response)

                    throws IOException, ServletException {

             doGetOrPost(request, response, "doGet");

       } // doGet

 

       @Override

       public void doPost(HttpServletRequest request, HttpServletResponse response)

                    throws IOException, ServletException {

             doGetOrPost(request, response, "doPost");

       } // doPost

 

       private class HttpClientExample {

             // one instance, reuse

             private final CloseableHttpClient httpClient = HttpClients.createDefault();

             private void close() throws IOException {

                    httpClient.close();

             }

 

             private String sendGet(String URL, String protocol, String uri, String queryString) throws Exception {

                    String urlURI = "";

                    if (uri == null || uri.trim().equals("")) {

                        urlURI = protocol + URL;

                    } else {

                        // uri = java.util.Base64.getEncoder().encodeToString(uri.getBytes());

                        urlURI = protocol + URL + uri;

                    }

                    if (queryString != null && !queryString.trim().equals("")) {

                        if (!queryString.startsWith("?")) {

                          urlURI += "?";

                        }

                        // queryString = java.util.Base64.getEncoder().encodeToString(queryString.getBytes());

                        urlURI += queryString;

                    }

                    System.out.println("sendGet(): urlURI: " + urlURI);

 

                    HttpGet request = new HttpGet(urlURI);

                    // add request headers

                    request.addHeader("custom-key", uri+queryString);

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
