# Exec

##### Example code
```
method main() {
    rec(i);
}

method rec(int i) {
    if (i > 100) {
        return;
    }
    rec(i);
}

class Test {
    int x;
}
```

##### Parsed code
```
executable wrapper::method 'main' {
    executable;
}

executable wrapper::method 'rec' {
    executable wrapper::if {
        executable;
    }

    executable;
}

executable wrapper::class 'Test' {
    field;
}
```

##### Work
```

```