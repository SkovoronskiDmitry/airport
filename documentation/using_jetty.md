# Using jetty server

Goto Project folder and execute  

mvn clean install -Pjetty
    
1. Create XML files in webapps folder in jetty server
```
airport-rest-app-1.0-SNAPSHOT.xml
```
```
airport-web-app-1.0-SNAPSHOT.xml
```
with the code inside
 ```
<?xml version="1.0"  encoding="ISO-8859-1"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" 
  "http://www.eclipse.org/jetty/configure.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Set name="contextPath">/rest</Set>
    <Set name="war"> file:///home/dmitry/development/dskovoronski-airport-epam/dskovoronski-airport/airport-rest-app/target/airport-rest-app-1.0-SNAPSHOT.war</Set>
</Configure>


<?xml version="1.0"  encoding="ISO-8859-1"?>
<!DOCTYPE Configure PUBLIC "-//Mort Bay Consulting//DTD Configure//EN" 
  "http://www.eclipse.org/jetty/configure.dtd">
<Configure class="org.eclipse.jetty.webapp.WebAppContext">
    <Set name="contextPath">/web</Set>
    <Set name="war">file:///home/dmitry/development/dskovoronski-airport-epam/dskovoronski-airport/airport-web-app/target/airport-web-app-1.0-SNAPSHOT.war</Set>
</Configure>
 ```

[source link]( https://www.baeldung.com/deploy-to-jetty "click")

2. Find "port" in jetty-http.xml, witch situated in jetty server/etc/  and change to 8081
 ```
  <Set name="port"><Property name="jetty.http.port" deprecated="jetty.port" default="8081" /></Set>
 ```

3. Start the Jetty server from the Jetty folder

 ```
 java -jar start.jar 
 ```
4. The application is available at

 ```
http://localhost:8081/web/flights

 ```



