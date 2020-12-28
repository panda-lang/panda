package org.panda_lang.language.architecture.type;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import org.panda_lang.language.architecture.dynamic.Frame;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.language.architecture.type.member.parameter.ParameterUtils;
import org.panda_lang.language.architecture.type.signature.TypedSignature;
import org.panda_lang.language.runtime.PandaProcessStack;
import org.panda_lang.language.runtime.PandaRuntimeException;
import org.panda_lang.utilities.commons.ArrayUtils;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.javassist.CtCode;
import org.panda_lang.utilities.commons.text.Joiner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public final class ClassGenerator {

    private static final AtomicInteger ID = new AtomicInteger(0);

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();
    private static final CtClass CT_TYPE_INSTANCE_CLASS = ClassPoolUtils.require(TypeInstance.class);
    private static final CtClass CT_TYPE_METHOD_CLASS = ClassPoolUtils.require(TypeMethod.class);
    private static final CtClass CT_TYPE_FRAME_CLASS = ClassPoolUtils.require(TypeFrame.class);
    private static final CtClass CT_FRAME_CLASS = ClassPoolUtils.require(Frame.class);
    private static final CtClass[] CONSTRUCTOR_PARAMETERS = { CT_TYPE_FRAME_CLASS };

    private final Map<Type, CtClass> generatedClasses = new HashMap<>();

    public CtClass allocate(Type type) {
        String javaName = type.getName().replace("::", "$").replace(":", "_") + "_" + ID.incrementAndGet();

        CtClass javaType = Kind.isInterface(type)
                ? CLASS_POOL.makeInterface(javaName)
                : CLASS_POOL.makeClass(javaName);

        generatedClasses.put(type, javaType);
        return javaType;
    }

    public void generate(Type type) throws CannotCompileException, NotFoundException {
        CtClass javaType = generatedClasses.get(type);
        // supertype

        if (!javaType.isInterface() && type.getSuperclass().isDefined()) {
            javaType.setSuperclass(getCtClass(type.getSuperclass().get().fetchType()));
        }

        // interfaces

        for (TypedSignature baseSignature : type.getBases()) {
            Type base = baseSignature.fetchType();

            if (Kind.isInterface(base)) {
                javaType.addInterface(getCtClass(base));
            }
        }

        // fields

        if (!javaType.isInterface()) {
            CtField frameField = new CtField(CT_TYPE_FRAME_CLASS, "__panda__frame", javaType);
            javaType.addField(frameField);
        }

        // implementation

        {
            javaType.addInterface(CT_TYPE_INSTANCE_CLASS);

            if (!javaType.isInterface()) {
                CtMethod getter = new CtMethod(CT_TYPE_FRAME_CLASS, "__panda__get_frame", new CtClass[0], javaType);
                getter.setBody("{ return $0.__panda__frame; }");
                javaType.addMethod(getter);

                CtMethod mapper = new CtMethod(CT_FRAME_CLASS, "__panda__to_frame", new CtClass[0], javaType);
                mapper.setBody("{ return $0.__panda__frame; }");
                javaType.addMethod(mapper);
            }
        }

        // constructors

        if (!javaType.isInterface()) {
            for (TypeConstructor constructor : type.getConstructors().getDeclaredProperties()) {
                CtClass[] parameters = ClassPoolUtils.toCt(ParameterUtils.parametersToClasses(constructor.getParameters()));
                CtConstructor javaConstructor = new CtConstructor(ArrayUtils.merge(CONSTRUCTOR_PARAMETERS, parameters), javaType);
                javaType.addConstructor(javaConstructor);

                String baseCall = StringUtils.EMPTY;

                if (constructor.getBaseCall().isDefined()) {
                    baseCall = Joiner.on(", ")
                            .join(constructor.getBaseCall().get().getArguments(), (index, element) -> {
                                Class<?> parameterType = element.getKnownType().getAssociated().get();
                                return "(" + parameterType.getName() + ") $1.getBaseArguments()[" + index + "]";
                            })
                            .toString();
                }

                if (generatedClasses.containsValue(javaType.getSuperclass())) {
                    baseCall = "(" + CT_TYPE_FRAME_CLASS.getName() + ") $1" + (baseCall.isEmpty() ? "" : ", ") + baseCall;
                }

                CtCode.of(javaConstructor)
                        .alias("${BASE_CALL}", baseCall)
                        .compile(
                                "super(${BASE_CALL});",
                                "$0.__panda__frame = $1;"
                        );
            }

        }

        // methods

        {
            Map<String, TypeMethod> methods = new HashMap<>();

            for (TypeMethod method : type.getMethods().getDeclaredProperties()) {
                String generatedName = method.getSimpleName().replaceAll("[^A-Za-z0-9]", "");

                CtClass[] parameters = ClassPoolUtils.toCt(ParameterUtils.parametersToClasses(method.getParameters()));
                CtMethod javaMethod = new CtMethod(getCtClass(method.getReturnType().getKnownType()), generatedName, parameters, javaType);
                javaType.addMethod(javaMethod);

                if (!method.isAbstract()) {
                    CtField methodField = new CtField(CT_TYPE_METHOD_CLASS, generatedName, javaType);
                    methodField.setModifiers(Modifier.PUBLIC | Modifier.STATIC);
                    javaType.addField(methodField);

                    CtCode.of(javaMethod)
                            .alias("{ProcessStack}", PandaProcessStack.class.getName())
                            .alias("{generatedName}", generatedName)
                            .compile(
                                    "{ProcessStack} stack = new {ProcessStack}($0.__panda__get_frame().getProcess(), 1);",
                                    "return ($r) {generatedName}.invoke(stack, $0, $args);"
                            );

                    methods.put(generatedName, method);
                }
            }

            type.getAssociated().then(generatedClass -> {
                for (Entry<String, TypeMethod> entry : methods.entrySet()) {
                    try {
                        Field methodField = generatedClass.getDeclaredField(entry.getKey());
                        methodField.set(null, entry.getValue());
                    } catch (NoSuchFieldException | IllegalAccessException exception) {
                        throw new PandaRuntimeException("Cannot prepare generated class", exception);
                    }
                }
            });
        }

        if (type.getState() != State.ABSTRACT) {
            javaType.setModifiers(javaType.getModifiers() & ~Modifier.ABSTRACT);
        }
    }

    public Class<?> complete(Type type) throws CannotCompileException {
        return generatedClasses.get(type).toClass();
    }

    private CtClass getCtClass(Type type) {
        CtClass javaReturnType = generatedClasses.get(type);

        if (javaReturnType == null) {
            javaReturnType = ClassPoolUtils.require(type.getAssociated().get());
        }

        return javaReturnType;
    }

}
