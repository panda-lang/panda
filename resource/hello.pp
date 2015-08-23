// 0.0.8
method main(){
    testThread = new Thread("Thread-Test");
    thread(testThread){
        while(true){
            System.print(testThread.getName());
        }
    }
    testThread.start();

    currentThread = Thread.currentThread();
    while(true){
        System.print(currentThread.getName());
    }
}