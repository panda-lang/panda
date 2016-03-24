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

    // Get the current thread
    Thread currentThread = Thread.currentThread();
    // Display the name of the current thread
    System.print(currentThread.getName());
}
```
#### Maven
API is not stable yet :red_circle:
```
<dependency>
            <groupId>org.panda_lang</groupId>
            <artifactId>panda</artifactId>
            <version>1.0.0-SNAPSHOT</version>
</dependency>
```
Repository: https://repo.panda-lang.org/
```
<repositories>
        <repository>
            <id>panda-repo</id>
            <name>Panda Repository</name>
            <url>http://repo.panda-lang.org/</url>
        </repository>
</repositories>
```
### Other
Lily IDE: https://github.com/Panda-Programming-Language/Lily
<br>
Moonlight: https://github.com/dzikoysk/Moonlight