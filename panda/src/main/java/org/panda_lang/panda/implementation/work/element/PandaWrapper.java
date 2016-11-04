package org.panda_lang.panda.implementation.work.element;

import org.panda_lang.core.work.Value;
import org.panda_lang.core.work.element.Wrapper;
import org.panda_lang.core.work.element.WrapperInstance;
import org.panda_lang.panda.composition.work.Field;

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
        private final int[] pointers;

        public PandaWrapperInstance(PandaWrapper pandaWrapper) {
            this.wrapper = pandaWrapper;
            this.pointers = new int[pandaWrapper.getFields().size()];
        }

        @Override
        public Value execute(Value... values) {
            return null;
        }

        @Override
        public int[] getPointers() {
            return pointers;
        }

        @Override
        public Wrapper getWrapper() {
            return null;
        }

    }

}
