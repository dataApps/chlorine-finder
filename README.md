# chlorine-finder
*A Java Library to detect sensitive data.*

Chlorine-finder is an open source library to detect sensitive elements in text. It is java based.
Chlorine-finder can detect different types of Credit card numbers, SSN, Phone Numbers, email adddresses, Ip Addresses, Street Addresses and more. 


###To Download source code :

*git clone https://github.com/dataApps/chlorine-finder.git*

###To build chlorine:

*mvn install*

###To use Chlorine-finder

- Add a dependency to Chlorine-finder library.

Maven dependency Definition:
```
   <dependency>
      <groupId>io.dataapps.chlorine</groupId>
      <artifactId>chlorine-finder</artifactId>
      <version>1.1.5</version>
   </dependency>
```
- Add the following lines of Code:
```
 FinderEngine engine = new FinderEngine();
 List<String> matchedValues = engine.find ("Here is my id : chlorine-finder@testchlorine.com and my machine inf o:  124.234.223.12 , ok ?");
```
 The matchedValues will contain the email Address chlorine-finder@testchlorine.com and the ip-address 124.234.223.12.
 If there are multiple sensitive elements, then all of them will be returned.
 
###Further Documentation
- [Adding custom Finders](https://github.com/dataApps/chlorine-finder/CustomFinders.md)
- [Available Finders](https://github.com/dataApps/chlorine-finder/blob/master/src/main/resources/finders_default.xml)
  
###Related projects
 
###Java Docs
The java docs for chlorine-finder are available [here](https://dataApps.io/files/chlorine-finder/javadoc/index.html).
 

 
 
