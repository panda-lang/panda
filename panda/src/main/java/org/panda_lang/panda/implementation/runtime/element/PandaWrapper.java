package org.panda_lang.panda.implementation.runtime.element;

import org.panda_lang.core.runtime.element.Value;
import org.panda_lang.core.runtime.element.Wrapper;
import org.panda_lang.core.runtime.element.WrapperInstance;
import org.panda_lang.core.runtime.structure.ExecutableBranch;
import org.panda_lang.panda.implementation.element.field.Field;

import java.util.ArrayList;
import java.util.List;

public class PandaWrapper extends PandaScope implements Wrapper {

    private final List<Field> fields;

    public PandaWrapper() {
        this.fields = new ArrayList<>();
    }

    @Override
    public WrapperInstance createInstance() {
        return new PandaWrapperInstance(this);
    }

    public List<Field> getFields() {
        return fields;
    }

    public static class PandaWrapperInstance implements WrapperInstance {

        private final PandaWrapper wrapper;
        private final long[] pointers;

        public PandaWrapperInstance(PandaWrapper pandaWrapper) {
            this.wrapper = pandaWrapper;
            this.pointers = new long[pandaWrapper.getFields().size()];
        }

        @Override
        public Value execute(ExecutableBranch executableBranch, Value... values) {
            return null;
        }

        @Override
        public long[] getPointers() {
            return pointers;
        }

        @Override
        public Wrapper getWrapper() {
            return null;
        }

    }

}
