package org.panda_lang.panda.core.statement;

import org.panda_lang.panda.core.Alice;
import org.panda_lang.panda.core.Essence;
import org.panda_lang.panda.core.statement.util.NamedExecutable;

import java.io.File;

public class Library implements NamedExecutable {

    private final String libraryPath;

    public Library(String libraryPath) {
        this.libraryPath = libraryPath;
    }

    @Override
    public Essence execute(Alice alice) {
        return null;
    }

    public File getFile() {
        return new File(libraryPath.substring(1, libraryPath.length() - 1));
    }

    @Override
    public String getName() {
        return libraryPath;
    }

}
