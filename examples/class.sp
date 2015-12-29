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
    Integer classVar = 1;

    // class_constructor
    constructor Test() {
        classVar = 2;
    }

    // class_func
    method func() {
        System.print(classVar);
    }

}
