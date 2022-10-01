package panda.compiler;

import org.junit.jupiter.api.Test;
import panda.interpreter.compiler.CompiledScript;
import panda.interpreter.language.body.ReturnDeclaration;
import panda.interpreter.language.expression.literal.VariableExpression;
import panda.interpreter.language.type.TypeGenerator;
import panda.interpreter.language.body.Body;
import panda.interpreter.language.function.FunctionDeclaration;
import panda.interpreter.language.parameter.Parameter;
import panda.interpreter.language.type.Signature;
import panda.interpreter.language.type.TypeDeclaration;
import panda.interpreter.runtime.PandaClassLoader;
import java.lang.reflect.InvocationTargetException;

public class TypeGeneratorTest {

    @Test
    public void shouldGenerateEmptyClass() {
        var typeStatement = new TypeDeclaration("Panda");
        var classGenerator = new TypeGenerator(typeStatement);
        var compiledScript = new CompiledScript();
        classGenerator.generate(compiledScript);
        var instance = load(typeStatement.getTypeName(), compiledScript.getClasses().get(0));
        System.out.println(instance);
    }

    @Test
    public void shouldGenerateIdentityFunctionInClass() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var typeStatement = new TypeDeclaration("Panda");

        var stringType = new Signature("java/lang/String");
        var identityFunction = new FunctionDeclaration("identity", stringType, new Body<>());
        identityFunction.addParameter(new Parameter("first", stringType, null));
        identityFunction.getBody().addStatement(new ReturnDeclaration(new VariableExpression(stringType, "first")));
        typeStatement.addFunction(identityFunction);

        var typeGenerator = new TypeGenerator(typeStatement);
        var compiledScript = new CompiledScript();
        typeGenerator.generate(compiledScript);
        var instance = load(typeStatement.getTypeName(), compiledScript.getClasses().get(0));
        var method = instance.getClass().getMethod("identity", new Class<?>[] { String.class });

        System.out.println(method.invoke(instance, "Hello Panda"));
    }

    private Object load(String name, byte[] data) {
        var classLoader = new PandaClassLoader();
        Class<?> type = classLoader.defineClass(name, data);
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
