# Panda

Panda is a lightweight and powerful programming language written in Java
<br>
Project website: https://panda-lang.org/
<br>

#### Example

```javascript
// Main method, called when the script starts
main launch() {
    // Prints "Hello Panda" in console
    System.print("Hello Panda");

    // Create new thread called "Thread-Test"
    Thread testThread = new Thread("Thread-Test");
    // Thread block associated with 'testThread', executed when the thread starts
    thread (testThread) {
        // Print the name of the thread
        System.print(testThread.getName());
    }
    // Start the thread
    testThread.start();

    // Math
    Int result = ((10 + 4)*2)^2;
    // Display result
    System.print(result);

    Foo foo = new Foo();
    foo.goodbye();
}

class Foo {

    method goodbye() {
        System.print("Goodbye!");
    }

}
```

#### Repository structure

```
panda/
+--exmaples/                 Example scripts written in Panda.
+--panda/                    Panda module
   +----/src                 All sources of Panda
   +----pom.xml             The main maven build script for Panda module
+--panda-core/               Panda Core module
   +---------/src            All sources of Panda Core
   +---------pom.xml        The main maven build script for Panda Core module
+--pom.xml                   The main maven build script
```

#### Maven

Latest build. Remember, API is not stable yet :o:
```xml
<dependency>
    <groupId>org.panda_lang</groupId>
    <artifactId>panda</artifactId>
    <version>indev-0.0.2-SNAPSHOT</version>
</dependency>
```
The latest build of the previous edition. Remember, this is deprecated!
```xml
<dependency>
    <groupId>org.panda_lang</groupId>
    <artifactId>panda</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```
Repository: https://repo.panda-lang.org/
```xml
<repositories>
    <repository>
        <id>panda-repo</id>
        <name>Panda Repository</name>
        <url>https://repo.panda-lang.org/</url>
    </repository>
</repositories>
```

#### Other

Lily the Panda IDE: https://github.com/Panda-Programming-Language/Lily
<br>
Light: https://github.com/dzikoysk/Light
