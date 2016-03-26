# Panda

Panda is a lightweight and powerful programming language written in Java
<br>
Project website: https://panda-lang.org/
<br>
#### Example
```javascript
// Main method, called when the script starts
method main() {
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

    // Loop 10 times
    for (10) {
        // Display current milliseconds
        System.print(System.currentTimeMillis());
    }

    // Math
    Number result = ((10 + 4)*2)^2;
    // Display result
    System.print(result);

    if (result > 0) {
        goodbye();
    }
}

method goodbye() {
    System.print("Goodbye!");
}
```
#### Maven
API is not stable yet :red_circle:
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
            <url>http://repo.panda-lang.org/</url>
        </repository>
</repositories>
```
### TODO
- Memory system
- Java implementation
- Analyzers
### Other
Lily the Panda IDE: https://github.com/Panda-Programming-Language/Lily
<br>
Moonlight: https://github.com/dzikoysk/Moonlight
