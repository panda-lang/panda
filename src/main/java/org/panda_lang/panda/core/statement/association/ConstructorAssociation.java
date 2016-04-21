package org.panda_lang.panda.core.statement.association;

import org.panda_lang.panda.core.Alice;

public interface ConstructorAssociation<T> {

    T construct(Alice alice);

}
