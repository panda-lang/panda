# Memory

```
[Application]
memory mem;
```
--
```
method main() {
    int i = 0;                      // ID as res of mem.get(INT.typeID()).allocate(VAR_VALUE),
                                    // VAR_PTR to ID
    rand(i);                        // METHOD.call(array { 0: VAR_PTR })
}

method rand(int randInt) {          // PARAM to VAR_PTR
    byte b = randInt;

    if (b == 0) {
        int x = b;
    }
}

method int retTest(int i) {
    return i;                       // ret PARAM_PTR
}
```
--
```
method main() {
    rec(1);                         // call METHOD.call(array { 0: "1" })
}

method rec(int i) {
    rec(i);
}
```