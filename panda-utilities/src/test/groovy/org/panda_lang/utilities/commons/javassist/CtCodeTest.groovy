package org.panda_lang.utilities.commons.javassist

import groovy.transform.CompileStatic;
import javassist.*;
import javassist.bytecode.MethodInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@CompileStatic
final class CtCodeTest {

    private CtCode<CtBehaviorMock> ctCode;
    private CtBehaviorMock ctBehaviorMock;

    @BeforeEach
    void setUp() throws Exception {
        ctBehaviorMock = createCtBehaviorMock()
        ctCode = CtCode.of(ctBehaviorMock)
    }

    private CtBehaviorMock createCtBehaviorMock() throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault()
        CtClass stringClass = classPool.get("java.lang.String")
        CtMethod trim = stringClass.getDeclaredMethod("trim")
        return new CtBehaviorMock(stringClass, trim.getMethodInfo())
    }

    @Test
    void compileReturnsSameInstanceToSupportFluentMethodCalls() throws CannotCompileException {
        CtBehaviorMock actualBehavior = ctCode.compile("return 1;")
        assertEquals(ctBehaviorMock, actualBehavior)
    }

    @Test
    void compileJoinsMultipleLines() throws CannotCompileException {
        CtBehaviorMock actualBehavior = ctCode.compile(
                "String line1;",
                "String line2;",
                "return 3;"
        )

        String actualBody = actualBehavior.getBody()
        assertTrue(actualBody.contains("line1"))
        assertTrue(actualBody.contains("line2"))
        assertTrue(actualBody.contains("return 3"))
    }

    @Test
    void compileDoesReplacePlaceholderValues() throws CannotCompileException {
        ctCode.alias("aPlaceholder", "aValue")
        
        CtBehaviorMock actualBehavior = ctCode.compile("return \"aPlaceholder\";")

        String actualBody = actualBehavior.getBody()
        assertTrue(actualBody.contains("aValue"))
    }

    private static class CtBehaviorMock extends CtBehavior {

        private String body;

        protected CtBehaviorMock(CtClass clazz, MethodInfo minfo) {
            super(clazz, minfo)
        }

        @Override
        void setBody(String src) throws CannotCompileException {
            super.setBody(src)
            this.body = src
        }

        String getBody() {
            return body
        }

        @Override
        String getLongName() {
            return "org.panda_lang.utilities.commons.javassist.CtBehavior"
        }

        @Override
        boolean isEmpty() {
            return false;
        }

        @Override
        String getName() {
            return "CtBehavior"
        }
    }

}
