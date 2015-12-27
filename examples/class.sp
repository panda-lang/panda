// @TODO
// It is a test script
// Classes have not yet been added

> namespace 'org.panda';

@import './protocol/Types.sp';
@import 'm$.windows.Thread' as PandaThread;
@import 'x.y.z.*';
@import 'diorite.is.Awesome';

// class_name
class Test {

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
