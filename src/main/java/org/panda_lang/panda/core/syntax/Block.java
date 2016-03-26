package org.panda_lang.panda.core.syntax;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.memory.Cache;
import org.panda_lang.panda.core.memory.Memory;
import org.panda_lang.panda.core.syntax.util.ExecutableCell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Block implements NamedExecutable {

    protected static final AtomicInteger atomicInteger = new AtomicInteger();

    protected List<ExecutableCell> executableCells;
    protected List<Field> fields;
    protected Factor[] factors;
    private String name;
    private Block parent;

    public Block(Block parent) {
        this();
        this.parent = parent;
    }

    public Block() {
        this.executableCells = new ArrayList<>();
        this.fields = new ArrayList<>();
    }

    @Override
    public Essence run(Alice alice) {
        Memory memory = alice.getMemory();
        Cache cache = memory.getCache();

        alice.setBlock(this);
        if (alice.getFactors() != null && factors != null) {
            for (int i = 0; i < alice.getFactors().length && i < factors.length; i++) {
                memory.put(factors[i].getVariableName(), alice.getFactors()[i].getValue(alice));
            }
        }

        for (ExecutableCell executableCell : executableCells) {
            Essence result;
            Executable executable = executableCell.getExecutable();

            if (executable instanceof Block) {
                Memory blockMemory = new Memory(memory);
                blockMemory.setCache(cache);
                Alice blockAlice = new Alice()
                        .fork()
                        .memory(blockMemory);
                blockMemory.setBlock((Block) executable);
                result = executable.run(blockAlice);
            }
            else if (executable instanceof Return) {
                result = executable.run(alice);
                cache.proceed(false);
            }
            else if (executable != null) {
                result = executable.run(alice);
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

    public void addExecutable(NamedExecutable e) {
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

    public Factor[] getFactors() {
        return factors;
    }

    public void setFactors(Factor... factors) {
        this.factors = factors;
    }

    public Collection<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public List<NamedExecutable> getExecutables() {
        List<NamedExecutable> list = new ArrayList<>();
        for (ExecutableCell cell : executableCells) {
            list.add(cell.getExecutable());
        }
        return list;
    }

    public void setExecutables(Collection<NamedExecutable> newExecutables) {
        executableCells.clear();
        for (NamedExecutable executable : newExecutables) {
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
