package com.fooze.sortedcreativetabs.mixin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.client.gui.CreativeTabsScreenPage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CreativeTabsScreenPage.class)
public class CreativeTabsScreenPageMixin {
    @ModifyVariable(method = "<init>", at = @At("HEAD"), argsOnly = true)
    private static List<CreativeModeTab> sortModdedTabs(List<CreativeModeTab> tabs) {
        List<CreativeModeTab> sortedTabs = new ArrayList<>(tabs);
        List<CreativeModeTab> moddedTabs = sortedTabs.stream()
                .filter(CreativeTabsScreenPageMixin::isModded)
                .sorted(Comparator
                        .comparing(CreativeTabsScreenPageMixin::displayName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(CreativeTabsScreenPageMixin::displayName)
                        .thenComparing(CreativeTabsScreenPageMixin::registryName))
                .toList();

        int moddedTabIndex = 0;

        for (int i = 0; i < sortedTabs.size(); i++) {
            if (isModded(sortedTabs.get(i))) {
                sortedTabs.set(i, moddedTabs.get(moddedTabIndex++));
            }
        }

        return sortedTabs;
    }

    private static boolean isModded(CreativeModeTab tab) {
        var key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        return key != null && !key.getNamespace().equals("minecraft");
    }

    private static String displayName(CreativeModeTab tab) {
        return tab.getDisplayName().getString();
    }

    private static String registryName(CreativeModeTab tab) {
        return Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab)).toString();
    }
}