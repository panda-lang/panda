package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.memory.Cache;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.statement.util.ExecutableCell;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Block implements NamedExecutable {

    protected static final AtomicInteger blockIDAssigner = new AtomicInteger();

    protected List<ExecutableCell> executableCells;
    protected List<Field> fields;
    protected RuntimeValue[] runtimeValues;
    protected String name;
    protected Block parent;

    public Block(Block parent) {
        this();
        this.parent = parent;
    }

    public Block() {
        this.executableCells = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    @Override
    public Essence execute(Alice alice) {
        Memory memory = alice.getMemory();
        Cache cache = memory.getCache();

        alice.setBlock(this);
        if (alice.getRuntimeValues() != null && runtimeValues != null) {
            for (int i = 0; i < alice.getRuntimeValues().length && i < runtimeValues.length; i++) {
                memory.put(runtimeValues[i].getVariableName(), alice.getRuntimeValues()[i].getValue(alice));
            }
        }

        for (ExecutableCell executableCell : executableCells) {
            Essence result;
            Executable executable = executableCell.getExecutable();

            if (executable instanceof Block) {
                Memory blockMemory = new Memory(memory);
                blockMemory.setCache(cache);
                Alice blockAlice = alice.fork()
                        .memory(blockMemory);
                blockMemory.setBlock((Block) executable);
                result = executable.execute(blockAlice);
            }
            else if (executable instanceof Return) {
                result = executable.execute(alice);
                cache.proceed(false);
            }
            else if (executable != null) {
                result = executable.execute(alice);
            }
            else {
                result = null;
            }

            if (!cache.isProceed()) {
                if (!isReturned() && hasParent()) {
                    memory.getParent().getCache().proceed(false);
                }
                return result;
            }
        }

        return null;
    }

    public void addExecutable(Executable e) {
        ExecutableCell cell = new ExecutableCell(executableCells.size(), e);
        executableCells.add(cell);

        if (e instanceof Field) {
            fields.add((Field) e);
        }
    }

    public boolean isReturned() {
        return false;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public RuntimeValue[] getRuntimeValues() {
        return runtimeValues;
    }

    public void setRuntimeValues(RuntimeValue... runtimeValues) {
        this.runtimeValues = runtimeValues;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<Executable> getExecutables() {
        List<Executable> list = new ArrayList<>();
        for (ExecutableCell cell : executableCells) {
            list.add(cell.getExecutable());
        }
        return list;
    }

    public void setExecutables(Collection<Executable> newExecutables) {
        executableCells.clear();
        for (Executable executable : newExecutables) {
            addExecutable(executable);
        }
    }

    public List<ExecutableCell> getExecutableCells() {
        return executableCells;
    }

    public Block getParent() {
        return parent;
    }

    public void setParent(Block block) {
        this.parent = block;
    }

    public Block getBlock() {
        return this;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

}
