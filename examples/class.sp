// Group
group examples;

// Class name
class Test {

    // Field
    Number classVar = 1;

    // Constructor
    constructor Test() {
        classVar = 2;
    }

    // A method that modifies variable 'classVar'
    method modify() {
        classVar = 10;
    }

    // A method that displays variable 'classVar'
    method hello() {
        System.print(classVar);
    }

}

// Main method called when the script starts
method main() {
    // Creates 2 instances
    Test foo = new Test();
    Test bar = new Test();
    // Modifies 'foo'
    foo.modify();
    // Displays the contents of both instances.
    bar.hello();
    foo.hello();
}
