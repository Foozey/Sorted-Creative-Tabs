package com.fooze.sortedcreativetabs;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.ConfigValue<Boolean> ENABLE_SORTING = BUILDER.define("enableSorting", true);
    static final ModConfigSpec SPEC = BUILDER.build();
}