# chlorine-finder
*A Java Library to detect sensitive data.*

Chlorine-finder is an open source library to detect sensitive elements in text. It is java based.
Chlorine-finder can detect different types of Credit card numbers, SSN, Phone Numbers, email adddresses, Ip Addresses, Street Addresses and more. 


###To Download source code

*git clone https://github.com/dataApps/chlorine-finder.git*

###To build chlorine

*mvn install*

###To use Chlorine-finder

- Add a dependency to Chlorine-finder library.

Maven dependency Definition
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
 
Chlorine-finder internally uses a set of Finders to perform detection. The Finders can be specified as a regular expression or a Java Class.
 
### Download library jar

The latest chlorine-finder library can be downloaded [here](https://dataapps.io/chlorine.html#Opensource).
 
###Further Documentation
[chlorine-finder wiki](https://github.com/dataApps/chlorine-finder/wiki)
  
###Related projects
 
###Java Docs
The java docs for chlorine-finder are available [here](https://dataApps.io/files/chlorine-finder/javadoc/index.html).

We welcome all contributions. You can contribute features, enhancements, bug fixes or new Finders.

##Want to contribute features, bug fixes, enhancements?

    Fork it
    Create your feature branch (git checkout -b my-new-feature)
    Commit your changes (git commit -am 'Add some feature')
    Push to the branch (git push origin my-new-feature)
    Create new Pull Request
 

 
 
