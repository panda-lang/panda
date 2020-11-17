package org.panda_lang.language.architecture.type.signature;

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.function.Option;
import org.panda_lang.utilities.commons.function.Result;

final class SignatureUtils {

    //
    // Usages:
    //  assigning values and polymorphism
    //
    // Examples:
    //   Animal = Panda -> ok
    //   Foo<Bar> = Foo<Bar> -> ok
    //   Foo<any Animal> = Foo<Panda> -> ok
    //   Foo<also Animal> = Foo<Panda> -> err
    //   Foo<also Panda> = Foo<Panda> -> ok
    //
    public static Result<TypedSignature, String> typedToTyped(TypedSignature root, TypedSignature inheritor) {
        Type rootType = root.fetchType();
        Type inheritorType = inheritor.getReference().fetchType();

        switch (root.getRelation()) {
            case DIRECT:
            case ANY:
                return Result.when(rootType.isAssignableFrom(inheritorType), inheritor, rootType + " is not assignable from " + inheritorType);
            case ALSO:
                return Result.when(inheritorType.isAssignableFrom(rootType), inheritor, inheritorType + " is not assignable from " + rootType);
            default:
                throw new UnsupportedOperationException("Unknown relation");
        }
    }

    //
    // Usages:
    //   down-casting like <signature> inheritor = <signature> root
    //
    // Examples:
    //   Foo<T> = Foo<Bar> -> err
    //   Foo<?> = Foo<Bar> -> ok
    //   Foo<any T> = Foo<Bar> -> err
    //   Foo<any Bar> = Foo<Bar> -> ok
    //   Foo<T : any Panda> = Foo<
    //   Foo<also T> = Foo<Bar> -> err
    //   Foo<also Bar> = Foo<Bar> -> ok
    //
    public static Result<GenericSignature, String> typedToGeneric(TypedSignature root, GenericSignature inheritor) {
        if (!inheritor.isWildcard()) {
            return Result.error("Type cannot be casted to direct generic type");
        }

        switch (inheritor.getRelation()) {
            case DIRECT:
                return Result.ok(inheritor);
            case ANY:
                Signature anySignature = inheritor.getAny().get();

                if (anySignature.isTyped()) {
                    Type anyType = anySignature.toTyped().getReference().fetchType();
                    return Result.when(anyType.isAssignableFrom(root.fetchType()), inheritor, anySignature + " is not assignable from " + root.fetchType());
                }

                if (anySignature.isGeneric()) {
                    return inheritor.findGeneric(anySignature.toGeneric())
                            .map(genericExtends -> typedToGeneric(root, genericExtends))
                            .orElseGet(Result.error("Cannot find generic " + anySignature.toGeneric()));
                }

                throw new UnsupportedOperationException("Unknown relation");
            case ALSO:
                Signature alsoSignature = inheritor.getAlso().get();

                if (alsoSignature.isTyped()) {
                    Type rootType = root.fetchType();
                    Type alsoType = alsoSignature.toTyped().getReference().fetchType();

                    return Result.when(rootType.isAssignableFrom(alsoType), inheritor, rootType + " is not assignable from " + alsoType);
                }

                if (alsoSignature.isGeneric()) {
                    return inheritor.findGeneric(alsoSignature.toGeneric())
                            .map(genericAlso -> typedToGeneric(root, inheritor))
                            .orElseGet(Result.error("Cannot find generic " + alsoSignature.toGeneric()));

                }

                throw new UnsupportedOperationException("Unknown relation");
            default:
                throw new UnsupportedOperationException("Unknown relation");
        }
    }

    //
    // Usages:
    //   mixing generics to describe requirements
    //
    // Examples:
    //   Foo<A> = Foo<B>
    //   type Foo<A, B : A>
    //   type Foo<A, B : any A>
    //   type Foo<A, B : also A>
    //
    public static Result<GenericSignature, String> genericToGeneric(GenericSignature root, GenericSignature inheritor) {
        switch (inheritor.getRelation()) {
            case DIRECT:
                return Result.error("Cannot assign " + inheritor.getLocalIdentifier() + " to " + root.getLocalIdentifier() + " directly");
            case ANY:
                Signature anySignature = inheritor.getAny().get();
                break;
            case ALSO:
                break;
        }
    }

    public static Option<TypedSignature> genericToTyped(GenericSignature root, TypedSignature inheritor) {
        return Option.none();
    }

}
