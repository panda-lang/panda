package org.panda_lang.language.architecture.type;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import org.panda_lang.language.architecture.type.member.constructor.TypeConstructor;
import org.panda_lang.language.architecture.type.member.method.TypeMethod;
import org.panda_lang.utilities.commons.ClassPoolUtils;
import org.panda_lang.utilities.commons.StringUtils;
import org.panda_lang.utilities.commons.javassist.CtCode;
import org.panda_lang.utilities.commons.text.Joiner;

final class TypeGenerator {

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();
    private static final CtClass CT_TYPE_INSTANCE_CLASS = ClassPoolUtils.require(TypeInstance.class);
    private static final CtClass CT_TYPE_METHOD_CLASS = ClassPoolUtils.require(TypeMethod.class);
    private static final CtClass CT_TYPE_FRAME_CLASS = ClassPoolUtils.require(TypeFrame.class);
    private static final CtClass[] CONSTRUCTOR_PARAMETERS = { CT_TYPE_FRAME_CLASS };

    public Class<?> generate(Type type) throws CannotCompileException {
        String javaName = type.getName().replace("::", "$").replace(":", "_");

        CtClass javaType = Kind.isInterface(type)
                ? CLASS_POOL.makeInterface(javaName)
                : CLASS_POOL.makeClass(javaName);

        // supertype

        if (type.getSuperclass().isDefined()) {
            javaType.setSuperclass(ClassPoolUtils.require(type.getSuperclass().get().getPrimaryType().getAssociated().get()));
        }

        // interfaces

        for (Signature baseSignature : type.getBases()) {
            Type base = baseSignature.getPrimaryType();

            if (Kind.isInterface(base)) {
                javaType.addInterface(ClassPoolUtils.require(base.getAssociated().get()));
            }
        }

        // fields

        {
            CtField frameField = new CtField(CT_TYPE_FRAME_CLASS, "__panda__frame", javaType);
            javaType.addField(frameField);
        }

        // implementation

        {
            javaType.addInterface(CT_TYPE_INSTANCE_CLASS);

            CtMethod getter = new CtMethod(CT_TYPE_FRAME_CLASS, "__panda__get_frame", new CtClass[0], javaType);
            getter.setBody("{ return $0.__panda__frame; }");
            javaType.addMethod(getter);
        }

        // constructors

        {

            for (TypeConstructor constructor : type.getConstructors().getDeclaredProperties()) {
                CtConstructor javaConstructor = new CtConstructor(CONSTRUCTOR_PARAMETERS, javaType);
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
            for (TypeMethod method : type.getMethods().getDeclaredProperties()) {

            }

        }

        // convert

        return javaType.toClass();
    }

}
