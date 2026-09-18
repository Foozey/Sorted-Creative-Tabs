package com.fooze.sortedcreativetabs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class NeoForgeConfig extends Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    private static final ModConfigSpec.ConfigValue<Boolean> ENABLE_SORTING = BUILDER.define("enableSorting", true);
    public static final ModConfigSpec SPEC = BUILDER.build();

    @Override
    public boolean enableSorting() {
        return ENABLE_SORTING.get();
    }
}