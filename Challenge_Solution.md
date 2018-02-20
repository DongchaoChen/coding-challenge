# Challenge Solution
A general summary about this code challenge by @DongchaoChen

- [Develop Environment Setup](#develop-environment-setup)
  * [Java](#java)
  * [Maven](#maven)
  * [Git](#git)
  * [Intellij IDE](#intellij-ide)
- [Java Solution Discussion](#java-solution-discussion)
  * [file size](#file-size)
  * [file extension](#file-extension)
  * [Unit Testing](#unit-testing)
  * [JSON Libraries](#json-libraries)
  * [CSV Libraries](#csv-libraries)
  * [Gzip file](#gzip-file)
- [How To](#how-to)
  * [How To Build](#how-to-build)
  * [How To Test](#how-to-test)
  * [How To Run](#how-to-run)
- [Performance Discussion](#performance-discussion)
  * [JSON Library Usage](#json-library-usage)
  * [CSV Library Usage](#csv-library-usage)
- [Anything Else](#anything-else)
  * [github code owner](#github-code-owner)
  * [checkstyle](#checkstyle)
  * [findbugs](#findbugs)
  * [codenarc](#codenarc)
  * [jacoco](#jacoco)

## Develop Environment Setup
The following illustrate how to setup local development for this challenge.

### Java
- You need to have Java 8 installed on your machine, you can download Java JDK from [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html).
- Make sure Java JDK in your **PATH**, to verify it, run below command, which will display current Java version number
  ```
  $ java -version
  ```

### Maven
You need to have [Apache Maven](https://maven.apache.org/index.html) (e.g. apache-maven-3.5.2) installed on your machine. For how to install Maven on Windows, Linux, Mac, please read this [tutorial](http://www.baeldung.com/install-maven-on-windows-linux-mac).

### Git
Get your git from [here](https://git-scm.com/downloads).

**Note** Git for Windows provides a BASH emulation used to run Git from the command line, so you will feel right like using `git` command in LINUX and UNIX environment.

### Intellij IDE
IntelliJ IDE is one recommended tool for Java development, you can download Community version [here](https://www.jetbrains.com/idea/download).

## Java Solution Discussion
Some thoughts when doing this code challenge.

### file size
- There are few ways to get file size, in this challenge, I chose to use Standard Java IO to avoid adding additional 3rd Party libraries (e.g. Apache Commons IO)
  * Use Java java.io.File [File.length()](https://docs.oracle.com/javase/8/docs/api/java/io/File.html#length--)
  * Use the NIO library [FIleChannel.size()](https://docs.oracle.com/javase/8/docs/api/java/nio/channels/FileChannel.html#size--)
  * Use Apache Commons IO [FileUtils.sizeOf()](https://commons.apache.org/proper/commons-io/javadocs/api-2.6/org/apache/commons/io/FileUtils.html#sizeOf-java.io.File-)
- function `getFileSize(File file)` return type could be `int`, though I chose return `long`
  For this code challenge, we can assume we won't use a test file larger than 2GB, a 2GB json / csv file is rare in real case usage.
  * int max_value is 2147483647 bytes / almost 2GB
  * long max_value is 9223372036854775807 bytes / 8589934592GB
  
  You can use [Bit Calculator](http://www.matisse.net/bitcalc/) for conversion. Why we need check file size? Because we may need different strategies to handle file reading/writing for performance issue.

### file extension
- There are few ways to get file extension, in this challenge, I chose to just write Java code logic to avoid adding additional 3rd Party libraries (e.g. Apache Commons IO)
  * Write Java code logic
  * Use Apache Commons IO [FilenameUtils.getExtension(FILE_PATH)](https://commons.apache.org/proper/commons-io/javadocs/api-2.6/org/apache/commons/io/FilenameUtils.html#getExtension-java.lang.String-)
  * Use [Google guava](https://github.com/google/guava)
    ```
    Files.getFileExtension(FILE_PATH);
    ```
- File extension case sensitive
  
  In this code challenge, I ignore file extension case sensitive issue, which means `test.CSV` and `test.csv` is the same file. In Windows, there is no file extension case sensitive issue, but some Linux OS have such issue.

### Unit Testing
- When testing Java code, I more prefer to use [Groovy](http://groovy-lang.org/) and [Spock](https://github.com/spockframework/spock). Why? You may want to read this [article](http://blog.codepipes.com/testing/spock-vs-junit.html), which may gives you some points.
- When use [Groovy](http://groovy-lang.org/) and [Spock](https://github.com/spockframework/spock) with [Maven](https://maven.apache.org/index.html), you need to use [GMavenPlus](https://github.com/groovy/GMavenPlus) plugin to tell maven compile the `src/test/groovy` test codes, for more information about how to configure GMavenPlus, you can refer [here](https://github.com/groovy/GMavenPlus/wiki/Examples).
- [Spock examples](https://github.com/spockframework/spock-example)

### JSON Libraries
There are many JSON libraries we can use:
- [FasterXML/Jackson](https://github.com/FasterXML/jackson)
- [google/gson](https://github.com/google/gson)
- [jsonp](https://javaee.github.io/jsonp/)
- [alibaba/fastjson](https://github.com/alibaba/fastjson)

When decide to pick which JSON library to use, referred [benchmark test](https://github.com/fabienrenaud/java-json-benchmark) and [article](https://blog.takipi.com/the-ultimate-json-library-json-simple-vs-gson-vs-jackson-vs-json/).

### CSV Libraries 
There are few CSV libraries we can use:
- [osiegmar/FastCSV](https://github.com/osiegmar/FastCSV)
- [FasterXML/jackson-dataformats-text](https://github.com/FasterXML/jackson-dataformats-text)

Picked FastCSV based on its benchmark test.

### Gzip file
Used [Apache Commons Compress](https://commons.apache.org/proper/commons-compress/).  

## How To
### How To Build
- To build the project with tests
```
mvn clean package
```
- To build the project without tests
```
mvn clean package -DskipTests
```

### How To Test
- To run the unit tests
```
mvn clean test
```
- To test test/resources csv and json files
```
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/test/resources/population_sample.csv"
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/test/resources/population_sample.json"
```
- To test main/resources csv, json, csv.gz, json.gz files
```
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/main/resources/population.csv"
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/main/resources/population.json"
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/main/resources/population_large.csv.gz"
mvn exec:java -Dexec.mainClass="com.interset.interview.Runner" -Dexec.args="src/main/resources/population_large.json.gz"
```
- General your own data (csv / json) for testing
  * [JSON Generator](https://www.json-generator.com/)
  * [mockaroo](https://www.mockaroo.com/)

### How To Run
To run the program, run the following bash script, also found at the root of the project:
```
./stats_extractor.sh <path/to/json_or_csv>
```

## Performance Discussion
To improve the performance, there are few things we can take care:
- When reading files to collect information, try to only read **once**
- Use right data structure to store data and minimize the sorting cost
- Pick right library and use correct API based on actual case

### JSON Library Usage
I used both **Jackson** and **Gson** libraries to process JSON files. When parsing a JSON file, you have two options: **1)** you can read the file entirely in an in-memory data structure (a tree model), 
which allows for easy random access to all the data or **2)** you can process the file in a streaming manner, the parser can be in control by pushing our events and the application can pull the events from the parser.

When the file is not that large, read the file entirely in memory is always the winner, but if you need read a really really large file (e.g. 2GB), you may think about using stream mode.

In function **processJsonUsingJackson(file)**, I processed json file entirely in memory while in function **processJsonUsingGson(file)**, I used stream mode. 

### CSV Library Usage
Similar as processing JSON files, CSV parser also can either read file entirely in memory or read in stream mode. Since in this challenge, the csv file is not that large and in-memory process is always faster than stream mode, 
I processed csv file entirely in memory in function **processCSVFile(file)**. 

## Anything Else

### github code owner
- Added `.github\CODEOWNERS` see github code owner [feature](https://github.com/blog/2392-introducing-code-owners)
- Added `.gethub\PULL_REQUEST_TEMPLATE.md` to help PR review / merge process.

### checkstyle
[checkstyle](https://maven.apache.org/plugins/maven-checkstyle-plugin/) will be useful to ensure the same coding style in the team. Here I picked up `google_checkstyle.xml`, see it in folder `src/main/resources/config/checkstyle`.

You can run `mvn site` to generate checkstyle report.

### findbugs
[findbugs](https://gleclaire.github.io/findbugs-maven-plugin/index.html) looks for bugs in Java programs.

You can run `mvn site` to get findbugs report.

### codenarc
[codenarc](https://gleclaire.github.io/codenarc-maven-plugin/) is useful for groovy code static analysis, though I did not include it in this project.

### jacoco
[jacoco](https://github.com/jacoco/jacoco) which gives you an idea about unit tests code coverage in the project. 
