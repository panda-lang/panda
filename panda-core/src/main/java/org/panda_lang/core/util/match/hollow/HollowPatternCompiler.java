package org.panda_lang.core.util.match.hollow;

import java.util.List;

public class HollowPatternCompiler {

    private final HollowPatternBuilder builder;

    protected HollowPatternCompiler(HollowPatternBuilder builder) {
        this.builder = builder;
    }

    public HollowPatternBuilder compile(String pattern) {
        List<String> fragments = HollowPatternUtils.toFragments(pattern);

        for (String fragment : fragments) {
            if (fragment.equals("*")) {
                builder.hollow();
            }
            else {
                builder.basis(fragment);
            }
        }
        return builder;
    }

    public HollowPatternBuilder getBuilder() {
        return builder;
    }

}
