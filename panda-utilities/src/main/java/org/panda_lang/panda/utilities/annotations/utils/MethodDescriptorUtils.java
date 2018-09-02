package org.panda_lang.panda.utilities.annotations.utils;

import org.panda_lang.panda.utilities.annotations.AnnotationsScannerUtils;
import org.panda_lang.panda.utilities.commons.collection.Sets;
import org.panda_lang.panda.utilities.commons.objects.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class MethodDescriptorUtils {

    public static boolean isConstructor(String descriptor) {
        return descriptor.contains("init>");
    }

    public static Member getMemberFromDescriptor(String descriptor, Collection<ClassLoader> classLoaders) {
        int p0 = descriptor.lastIndexOf('(');
        String memberKey = p0 != -1 ? descriptor.substring(0, p0) : descriptor;
        String methodParameters = p0 != -1 ? descriptor.substring(p0 + 1, descriptor.lastIndexOf(')')) : "";

        int p1 = Math.max(memberKey.lastIndexOf('.'), memberKey.lastIndexOf("$"));
        String className = memberKey.substring(memberKey.lastIndexOf(' ') + 1, p1);
        String memberName = memberKey.substring(p1 + 1);

        Class<?>[] parameterTypes = null;

        if (!StringUtils.isEmpty(methodParameters)) {
            String[] parameterNames = methodParameters.split(",");
            List<Class<?>> result = new ArrayList<Class<?>>(parameterNames.length);

            for (String name : parameterNames) {
                result.add(AnnotationsScannerUtils.forName(name.trim(), classLoaders));
            }

            Class<?>[] types = new Class[result.size()];
            parameterTypes = result.toArray(types);
        }

        Class<?> aClass = AnnotationsScannerUtils.forName(className, classLoaders);

        while (aClass != null) {
            try {
                if (!descriptor.contains("(")) {
                    return aClass.isInterface() ? aClass.getField(memberName) : aClass.getDeclaredField(memberName);
                } else if (isConstructor(descriptor)) {
                    return aClass.isInterface() ? aClass.getConstructor(parameterTypes) : aClass.getDeclaredConstructor(parameterTypes);
                } else {
                    return aClass.isInterface() ? aClass.getMethod(memberName, parameterTypes) : aClass.getDeclaredMethod(memberName, parameterTypes);
                }
            } catch (Exception e) {
                aClass = aClass.getSuperclass();
            }
        }

        throw new RuntimeException("Can't resolve member named " + memberName + " for class " + className);
    }

    public static Set<Method> getMethodsFromDescriptors(Iterable<String> annotatedWith, Collection<ClassLoader> classLoaders) {
        Set<Method> result = Sets.newHashSet();

        for (String annotated : annotatedWith) {
            if (!isConstructor(annotated)) {
                Method member = (Method) getMemberFromDescriptor(annotated, classLoaders);

                if (member != null) {
                    result.add(member);
                }
            }
        }

        return result;
    }

    public static Set<Constructor> getConstructorsFromDescriptors(Iterable<String> annotatedWith, Collection<ClassLoader> classLoaders) {
        Set<Constructor> result = Sets.newHashSet();

        for (String annotated : annotatedWith) {
            if (isConstructor(annotated)) {
                Constructor member = (Constructor) getMemberFromDescriptor(annotated, classLoaders);

                if (member != null) {
                    result.add(member);
                }
            }
        }

        return result;
    }

    public static Set<Member> getMembersFromDescriptors(Iterable<String> values, Collection<ClassLoader> classLoaders) {
        Set<Member> result = Sets.newHashSet();

        for (String value : values) {
            result.add(getMemberFromDescriptor(value, classLoaders));
        }

        return result;
    }

    public static Field getFieldFromString(String field, Collection<ClassLoader> classLoaders) {
        String className = field.substring(0, field.lastIndexOf('.'));
        String fieldName = field.substring(field.lastIndexOf('.') + 1);

        try {
            return AnnotationsScannerUtils.forName(className, classLoaders).getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new RuntimeException("Can't resolve field named " + fieldName, e);
        }
    }

}
