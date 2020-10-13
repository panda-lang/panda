package org.panda_lang.panda.language.resource.syntax.type;

import org.panda_lang.language.architecture.module.Imports;
import org.panda_lang.language.architecture.type.AbstractSignature;
import org.panda_lang.language.architecture.type.Signature;
import org.panda_lang.language.architecture.type.Signature.Relation;
import org.panda_lang.language.architecture.type.Type;
import org.panda_lang.language.interpreter.parser.Context;
import org.panda_lang.language.interpreter.parser.Parser;
import org.panda_lang.utilities.commons.function.Result;

public final class SignatureParser implements Parser {

    @Override
    public String name() {
        return "signature";
    }

    public Signature parse(Context<?> context, SignatureSource signatureSource) {
        Imports imports = context.getImports();
        String name = signatureSource.getName().getValue();

        Result<Type, AbstractSignature> type = imports.forType(name)
                .map(importedType -> {
                    //noinspection Convert2MethodRef
                    return Result.<Type, AbstractSignature> ok(importedType);
                })
                .orElseGet(() -> Result.error(AbstractSignature.of(name)));

        Signature[] generics = signatureSource.getGenerics().stream()
                .map(genericSignature -> parse(context, genericSignature))
                .toArray(Signature[]::new);

        return new Signature(context.getTypeLoader(), type, generics, Relation.DIRECT);
    }

}
