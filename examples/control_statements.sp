// Main method called when the script starts
method main() {
    if (1 >= 0) {
        if (false) {
            // It won't be displayed, because condition is not met
            System.print(":<");
        } else {
            // This message will be displayed
            System.print("Hello Panda :>");
        }
    } else {
        // It won't be displayed, because condition is met
        System.print(":<");
    }
}
