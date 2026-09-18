package com.fooze.sortedcreativetabs;

import net.fabricmc.api.ClientModInitializer;

public class SortedCreativeTabsFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Config.setInstance(FabricConfig.load());
    }
}