package org.panda_lang.panda.composition.syntax;

public class Keywords {

    public static final String ABSTRACT = "abstract";

    public static final String AS = "as";

    public static final String BOOLEAN = "boolean";

    public static final String BREAK = "break";

    public static final String BYTE = "byte";

    public static final String CASE = "catch";

    public static final String CHAR = "char";

    public static final String CLASS = "class";

    public static final String CONSTRUCTOR = "constructor";

    public static final String CONTINUE = "continue";

    public static final String DOUBLE = "double";

    public static final String ELSE = "else";

    public static final String EXTENDS = "extends";

    public static final String FINAL = "final";

    public static final String FLOAT = "float";

    public static final String FOR = "for";

    public static final String GROUP = "group";

    public static final String IF = "if";

    public static final String IMPLEMENTS = "implements";

    public static final String INSTANCE_OF = "instanceof";

    public static final String INT = "interface";

    public static final String LONG = "long";

    public static final String METHOD = "method";

    public static final String NATIVE = "native";

    public static final String NEW = "new";

    public static final String RETURN = "return";

    public static final String REQUIRE = "require";

    public static final String SHORT = "short";

    public static final String STATIC = "static";

    public static final String SUPER = "super";

    public static final String SYNCHRONIZED = "synchronized";

    public static final String SWITCH = "switch";

    public static final String THIS = "this";

    public static final String WHILE = "while";

    private static final String[] VALUES = new String[34];

    static {
        VALUES[0] = ABSTRACT;
        VALUES[1] = AS;
        VALUES[2] = BOOLEAN;
        VALUES[3] = BREAK;
        VALUES[4] = BYTE;
        VALUES[5] = CASE;
        VALUES[6] = CHAR;
        VALUES[7] = CLASS;
        VALUES[8] = CONSTRUCTOR;
        VALUES[9] = CONTINUE;
        VALUES[10] = DOUBLE;
        VALUES[11] = ELSE;
        VALUES[12] = EXTENDS;
        VALUES[13] = FINAL;
        VALUES[14] = FLOAT;
        VALUES[15] = FOR;
        VALUES[16] = GROUP;
        VALUES[17] = IF;
        VALUES[18] = IMPLEMENTS;
        VALUES[19] = INSTANCE_OF;
        VALUES[20] = INT;
        VALUES[21] = LONG;
        VALUES[22] = METHOD;
        VALUES[23] = NATIVE;
        VALUES[24] = NEW;
        VALUES[25] = RETURN;
        VALUES[26] = REQUIRE;
        VALUES[27] = SHORT;
        VALUES[28] = STATIC;
        VALUES[29] = SUPER;
        VALUES[30] = SYNCHRONIZED;
        VALUES[31] = SWITCH;
        VALUES[32] = THIS;
        VALUES[33] = WHILE;
    }

    public static String[] values() {
        return VALUES;
    }

}
