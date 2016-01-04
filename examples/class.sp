// Group
group diorite.connection;

// import( Group )
import diorite.entity;
// import( Specific )
import diorite.material > Box;
// import( File )
import './protocol/TestOut.sp';

// class_name
class TestCon extends Connection {

    // class_field
    Number classVar = 1;

    // class_constructor
    constructor TestCon() {
        classVar = 2;
    }

    method cnt() {
        classVar = 10;
    }

    // class_func
    method func() {
        System.print(classVar);
    }

}

method main() {
    TestCon var = new TestCon();
    TestCon var2 = new TestCon();
    var2.cnt();
    var.func();
}