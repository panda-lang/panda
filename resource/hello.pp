// 0.0.8
global = "X";
method main(){
    testThread = new Thread(global);
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