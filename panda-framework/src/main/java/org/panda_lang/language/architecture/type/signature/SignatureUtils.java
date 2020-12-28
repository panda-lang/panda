package org.panda_lang.language.architecture.type.signature;

import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.utilities.commons.function.Result;

final class SignatureUtils {

    public static Result<? extends Signature, String> merge(Signature root, Signature inheritor) {
        if (root.isTyped()) {
            if (inheritor.isTyped()) {
                return typedToTyped(root.toTyped(), inheritor.toTyped());
            }

            if (inheritor.isGeneric()) {
                return typedToGeneric(root.toTyped(), inheritor.toGeneric());
            }
        }

        if (root.isGeneric()) {
            if (inheritor.isTyped()) {
                return genericToTyped(root.toGeneric(), inheritor.toTyped());
            }

            if (inheritor.isGeneric()) {
                return genericToGeneric(root.toGeneric(), inheritor.toGeneric());
            }
        }

        throw new UnsupportedOperationException("Unknown relation");
    }

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
                return Result.when(inheritorType.isAssignableFrom(rootType), inheritor, inheritorType + " is not assignable from " + rootType);
            case ANY:
                // return Result.when(rootType.isAssignableFrom(inheritorType), inheritor, rootType + " is not assignable from " + inheritorType);
            case ALSO:
                // return Result.when(inheritorType.isAssignableFrom(rootType), inheritor, inheritorType + " is not assignable from " + rootType);
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
                    Type anyType = anySignature.getKnownType();
                    return Result.when(anyType.isAssignableFrom(root.getKnownType()), inheritor, anySignature + " is not assignable from " + root.fetchType());
                }

                if (anySignature.isGeneric()) {
                    return inheritor.findGeneric(anySignature.toGeneric())
                            .map(genericExtends -> typedToGeneric(root, genericExtends.getKey()))
                            .orElseGet(Result.error("Cannot find generic " + anySignature.toGeneric()));
                }

                throw new UnsupportedOperationException("Unknown relation");
            case ALSO:
                Signature alsoSignature = inheritor.getAlso().get();

                if (alsoSignature.isTyped()) {
                    Type rootType = root.fetchType();
                    Type alsoType = alsoSignature.getKnownType();

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
        switch (root.getRelation()) {
            case DIRECT:
                return inheritor.getLocalIdentifier().equals(root.getLocalIdentifier())
                        ? Result.ok(inheritor)
                        : Result.error("Cannot assign " + inheritor.getLocalIdentifier() + " to " + root.getLocalIdentifier() + " directly");
            case ANY:
                Signature inheritorAnySignature = inheritor.getAny().get();

                if (inheritorAnySignature.isTyped()) {
                    return genericToTyped(root, inheritorAnySignature.toTyped())
                            .map(result -> Result.<GenericSignature, String> ok(inheritor))
                            .orElseGet(Result::error);
                }

                if (inheritorAnySignature.isGeneric()) {
                    return genericToGeneric(root, inheritorAnySignature.toGeneric());
                }

                throw new UnsupportedOperationException("Unknown relation");
            case ALSO:
                Signature inheritorAlsoSignature = inheritor.getAlso().get();

                if (inheritorAlsoSignature.isTyped()) {
                    return genericToTyped(root, inheritorAlsoSignature.toTyped())
                            .map(result -> Result.<GenericSignature, String> ok(inheritor))
                            .orElseGet(Result::error);
                }

                if (inheritorAlsoSignature.isGeneric()) {
                    return genericToGeneric(root, inheritorAlsoSignature.toGeneric());
                }

                throw new UnsupportedOperationException("Unknown relation");
            default:
                throw new UnsupportedOperationException("Unknown relation");
        }
    }

    public static Result<TypedSignature, String> genericToTyped(GenericSignature root, TypedSignature inheritor) {
        switch (root.getRelation()) {
            case DIRECT:
            case ANY:
            case ALSO:
                return Result.ok(inheritor);
        }

        throw new UnsupportedOperationException("Unknown relation");
    }

}
