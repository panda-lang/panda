![Logo](https://panda-lang.org/github/v1/logo.png)

<p align="center">
   <a href="https://travis-ci.org/Panda-Programming-Language/Panda"><img src="https://travis-ci.org/Panda-Programming-Language/Panda.svg?branch=master" alt="Build Status"></a>
   <a href="https://ci.appveyor.com/project/Panda-Programming-Language/panda"><img src="https://ci.appveyor.com/api/projects/status/gpkf5t7v3b3wepcl?svg=true"></a>
   <a href="https://www.codefactor.io/repository/github/panda-programming-language/panda"><img src="https://www.codefactor.io/repository/github/panda-programming-language/panda/badge" alt="CodeFactor"></a>
   
   <hr>
   
   <p align="center">
      Panda is lightweight and powerful programming language written in Java.<br>
      Project website: <a href="https://panda-lang.org/">panda-lang.org</a>
   </p>
   
   <img src="https://panda-lang.org/github/v1/carbon.png" alt="Preview">
</p>

#### Repository structure
```
panda/
+--examples/                 Example scripts written in Panda
+--panda/                    Panda module
   +----/src                 All sources of Panda
   +----pom.xml              The main maven build script for Panda module
+--panda-framework/          Panda Framework module
   +----/src                 All sources of Panda Framework
   +----pom.xml              The main maven build script for Panda Framework module
+--panda-utilities/          Panda Utilities module
   +----/src                 All sources of Panda Utilities
   +----pom.xml              The main maven build script for Panda Utilities module
+--pom.xml                   The main maven build script
```

#### Maven
The latest indev build:

```xml
<dependency>
    <groupId>org.panda-lang</groupId>
    <artifactId>panda</artifactId>
    <version>indev-18.12.29</version>
</dependency>
```

Maven repository: [repo.panda-lang.org](https://repo.panda-lang.org/)

```xml
<repository>
    <id>panda-repo</id>
    <name>Panda Repository</name>
    <url>https://repo.panda-lang.org/</url>
</repository>
```

#### Other
- Lily the Panda IDE: https://github.com/Panda-Programming-Language/Lily <br>
- Light: https://github.com/dzikoysk/Light
