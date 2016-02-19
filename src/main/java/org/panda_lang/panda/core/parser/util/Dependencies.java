package org.panda_lang.panda.core.parser.util;

import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.essential.GroupCenter;
import org.panda_lang.panda.core.syntax.Group;
import org.panda_lang.panda.core.syntax.Import;
import org.panda_lang.panda.core.syntax.Library;
import org.panda_lang.panda.core.syntax.Vial;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Dependencies {

    private final PandaScript pandaScript;
    private final Collection<Group> groups;
    private final Collection<Library> libraries;
    private final Map<String, Import> specificMap;
    private final Map<String, Import> asMap;

    public Dependencies(PandaScript pandaScript) {
        this.pandaScript = pandaScript;
        this.groups = new ArrayList<>();
        this.libraries = new ArrayList<>();
        this.specificMap = new HashMap<>();
        this.asMap = new HashMap<>();
        this.initializeDefault();
    }

    private void initializeDefault() {
        this.groups.add(GroupCenter.getGroup("panda.lang"));
        this.groups.add(GroupCenter.getGroup("default"));
    }

    public void importElement(Import importElement) {
        if (importElement.isDefinedScript()) {
            // Specific
            specificMap.put(importElement.getSpecific(), importElement);
        }
        else if (importElement.isDefinedGroup()) {
            // Group
            groups.add(importElement.getGroup());
        }
        else if (importElement.isDefinedFile()) {
            // File
            String definedFile = importElement.getFile();
            File file = new File(pandaScript.getWorkingDirectory() + File.separator + definedFile);
            Collection<PandaScript> scripts = pandaScript.getPanda().getPandaLoader().loadDirectory(file);

            for (PandaScript pandaScript : scripts) {
                if (pandaScript == null) {
                    continue;
                }

                Collection<Vial> vials = pandaScript.extractVials();
                for (Vial vial : vials) {
                    Import anImport = new Import(vial.getGroup(), vial);
                    specificMap.put(anImport.getName(), anImport);
                }
            }
        }
        if (importElement.containsCustomName()) {
            // As
            asMap.put(importElement.getAs(), importElement);
        }
    }

    public Vial getVial(String vialName) {
        Import anImport = asMap.get(vialName);
        if (anImport == null) {
            anImport = specificMap.get(vialName);
        }

        if (anImport != null) {
            Group group = anImport.getGroup();
            return group.getVial(vialName);
        }

        for (Group group : groups) {
            Vial vial = group.getVial(vialName);
            if (vial != null) {
                return vial;
            }
        }

        return null;
    }

    public Map<String, Import> getAsMap() {
        return asMap;
    }

    public Map<String, Import> getSpecificMap() {
        return specificMap;
    }

    public Collection<Library> getLibraries() {
        return libraries;
    }

    public Collection<Group> getGroups() {
        return groups;
    }

    public PandaScript getPandaScript() {
        return pandaScript;
    }

}
