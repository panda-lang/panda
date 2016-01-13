package org.panda_lang.panda.core.parser.essential.util;

import org.panda_lang.panda.PandaLoader;
import org.panda_lang.panda.PandaScript;
import org.panda_lang.panda.core.parser.essential.GroupCenter;
import org.panda_lang.panda.core.syntax.Group;
import org.panda_lang.panda.core.syntax.Import;
import org.panda_lang.panda.core.syntax.Vial;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Dependencies {

    private final Collection<Group> groups;
    private final Map<String, Import> specificMap;
    private final Map<String, Import> asMap;

    public Dependencies() {
        this.groups = new ArrayList<>();
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
        } else if (importElement.isDefinedGroup()) {
            // Group
            groups.add(importElement.getGroup());
        } else if (importElement.isDefinedFile()) {
            // File
            PandaScript pandaScript = PandaLoader.loadSimpleScript(importElement.getFile());
            if (pandaScript != null) {
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

}
