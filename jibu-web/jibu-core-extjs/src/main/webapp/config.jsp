<%@ page import="java.io.BufferedReader" %>
<%@ page import="java.io.InputStream" %>
<%@ page import="java.io.InputStreamReader" %>
<%@ page import="java.io.IOException" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.DriverManager" %>
<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.sql.Statement" %>

<%@ page import="org.gaixie.jibu.security.MD5" %>

<%@ page contentType="text/html;charset=utf-8" %> 

<html>
  <head>
    <title>System initialization</title>
  </head>
  <body>
  <%if(request.getParameter("showform")==null){%>
    <form method="post">
      Select Database Type:
      <select name="dbType" id="dbType">  
        <option value="hsqldb" selected>hsqldb</option>  
        <option value="derby">derby</option>  
      </select> <br>
      Url: <input type="text" name="url" value="jdbc:hsqldb:mem:micritedb" > <br> 
      Driver:  <input type="text" name="driver" value="org.hsqldb.jdbcDriver"> <br> 
      Database Username:  <input type="text" name="dbuser" value="sa"> <br> 
      Database Password:  <input type="text" name="dbpwd" value=""> <p></p>

      Administrator Fullname:  <input type="text" name="adminfull" value="Tommy Wang"> <br> 
      Administrator Username:  <input type="text" name="adminuser" value="admin"> <br> 
      Administrator Password:  <input type="text" name="adminpwd" value="123456"> <br>
      Administrator email:  <input type="text" name="adminmail" value="xxxx@gaixie.org"> <br>
      <input type="hidden" name="showform" value="true" >
      <input type="submit" name="sub" value="Submit">
    </form>
  <%}else{%>
  <%
     String dbType=request.getParameter("dbType");
     String url=request.getParameter("url");
     String driver=request.getParameter("driver");
     String dbuser=request.getParameter("dbuser");
     String dbpwd=request.getParameter("dbpwd");
     String adminpwd=request.getParameter("adminpwd");
     String msg = "";
     StringBuilder command = new StringBuilder();
     Connection conn = null;
     Statement stmt = null; 
     ResultSet rset = null;      
     try {
         out.println("<table border=\"0\">");
         msg = "<tr><td>(1)&nbsp; </td><td>JDBC driver load</td><td>++++++++++++++++++++++++++++++</td>";
         out.println(msg);
         Class.forName(driver);
         out.println("<td>[<font color=\"green\">OK</font>]</td></tr>");
         msg = "<tr><td>(2)&nbsp; </td><td>Get database connection</td><td>++++++++++++++++++++++++++++++</td>";
         out.println(msg);
         conn=DriverManager.getConnection(url,dbuser,dbpwd);
         out.println("<td>[<font color=\"green\">OK</font>]</td></tr>");
         stmt = conn.createStatement(); 
         msg = "<tr><td>(3)&nbsp; </td><td>Get database script</td><td>++++++++++++++++++++++++++++++</td>";
         out.println(msg);
         InputStream is = this.getClass().getResourceAsStream("/dbscripts/"+dbType+"/createdb.sql");
         BufferedReader reader = new BufferedReader(new InputStreamReader(is));
         out.println("<td>[<font color=\"green\">OK</font>]</td></tr>");
         String line;
         msg = "<tr><td>(4)&nbsp; </td><td>Execute database script</td><td>++++++++++++++++++++++++++++++</td>";
         out.println(msg);
         while ((line = reader.readLine()) != null) {
             String trimmedLine = line.trim();
             if (trimmedLine.startsWith("//") || trimmedLine.startsWith("--")) {
                 //System.out.println(command);
             } else if (trimmedLine.endsWith(";")) {
                 command.append(line.substring(0, line.lastIndexOf(";")));
                 command.append(" ");
                 stmt.execute(command.toString());
                 command.setLength(0);
             } else if (trimmedLine.length() > 0) {
                 command.append(line);
                 command.append(" \n");
             }
        }
        reader.close();
        out.println("<td>[<font color=\"green\">OK</font>]</td></tr>");
        msg = "<tr><td>(5)&nbsp; </td><td>Create administrator</td><td>++++++++++++++++++++++++++++++</td>";
        out.println(msg);
        stmt.execute("insert into userbase(username,password,emailaddress,fullname) "
                           +"values ('"+request.getParameter("adminuser")+"','"
                           +MD5.encodeString(adminpwd,null)+"','"
                           +request.getParameter("adminemail")+"','"
                           +request.getParameter("adminfull")+"')");
        out.println("<td>[<font color=\"green\">OK</font>]</td></tr>");
    } catch (Exception e) {
        out.println("<td>[<font color=\"red\">FAILED</font>]</td></tr>");
    }
    finally {
        try { if (rset != null) rset.close(); } catch(Exception e) { }      
        try { if (stmt != null) stmt.close(); } catch(Exception e) { }
        try { if (conn != null) conn.close(); } catch(Exception e) { }
    } 
  }%>
  </body>
</html>
