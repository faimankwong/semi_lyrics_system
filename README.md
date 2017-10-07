# What is it?
The lyrics system provides a user friendly interface for the lyrics generated system. Moreoever, the system provides a list only contain specific genre words. The system allows the download of the lyrics.

### Prerequisites
Starting with the Microsoft JDBC Driver 4.2 for SQL Server, Sun Java SE Development Kit (JDK) 8.0 and Java Runtime Environment (JRE) 8.0 are supported. Support for Java Database Connectivity (JDBC) Spec API has been extended to include the JDBC 4.1 and 4.2 API.

Starting with the Microsoft JDBC Driver 4.1 for SQL Server, Sun Java SE Development Kit (JDK) 7.0 and Java Runtime Environment (JRE) 7.0 are supported.

Starting with the Microsoft JDBC Driver 4.0 for SQL Server, the JDBC driver support for Java Database Connectivity (JDBC) Spec API has been extended to include the JDBC 4.0 API. The JDBC 4.0 API was introduced as part of the Sun Java SE Development Kit (JDK) 6.0 and Java Runtime Environment (JRE) 6.0. JDBC 4.0 is a superset of the JDBC 3.0 API.

# Further Development
Deep learning should be implemented. This means that the system can do the self-learning during the operating. When the user creates the new song, the system should add those new lyrics to the database and re-calculate all the possibility. Moreover, all the grammar syntax should also be learned for the system by using naive Bayes algorithm. For example, the system should auto-correct the tense of the choices which fit the context. The system should not only for personal computer but also for the mobile version.

# Example
The code for connecting the internet
```
       String line = "";
       String line_corrected = new String();
       String toke = new String();
       StringBuilder contentBuf = new StringBuilder();
       String strURL = "http://www.elyrics.net/read/e/ed-sheeran-lyrics/what-do-i-know?-lyrics.html";

       URL url = new URL(strURL);
       HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
       InputStreamReader input = new InputStreamReader(httpConn
               .getInputStream(), "utf-8");
       BufferedReader bufReader = new BufferedReader(input);
       String line = "";
       String line_corrected  =new String();
       String toke  =new String();
       StringBuilder contentBuf = new StringBuilder();
       while ((line = bufReader.readLine()) != null) {
           if ((line.endsWith("<br>")&&!line.startsWith("<"))||(line.startsWith("<div id='inlyr' style='font-size:16px;'>"))) {
               if (!line.contains("[")) {

                   line_corrected=line.replaceAll(",", "").replaceAll("'E'", "")
                           .replaceAll("\\.", "").replaceAll("\\n", "")
                           .toLowerCase().replaceAll("\\?","").replaceAll("!","").replaceAll("\"","");
                   end.append(line_corrected.substring(line_corrected.lastIndexOf(" ")+1,line_corrected.indexOf('<'))+",");
                   toke+=line_corrected.replaceAll("<br>","")+" ";
                   System.out.print(end);
                   lyrics.append(line_corrected + "\n");
               }
           }

       }
```
