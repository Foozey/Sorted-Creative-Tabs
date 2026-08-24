package com.fooze.sortedcreativetabs.mixin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeTabsScreenPageMixin {
    @Redirect(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/neoforged/neoforge/common/CreativeModeTabRegistry;getSortedCreativeModeTabs()Ljava/util/List;"
    ))

    // Sorts modded tabs in alphabetical order by title, then by registry name
    private static List<CreativeModeTab> sortModdedTabs() {
        List<CreativeModeTab> tabs = CreativeModeTabRegistry.getSortedCreativeModeTabs();
        List<CreativeModeTab> sortedTabs = new ArrayList<>(tabs);
        int tabIndex = 0;

        // Sort by title first, then registry name
        List<CreativeModeTab> moddedTabs = sortedTabs.stream()
                .filter(CreativeTabsScreenPageMixin::isModded)
                .sorted(Comparator
                        .comparing(CreativeTabsScreenPageMixin::displayName, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(CreativeTabsScreenPageMixin::displayName)
                        .thenComparing(CreativeTabsScreenPageMixin::registryName))
                .toList();


        // Replace the default modded tab order with the sorted order
        for (int i = 0; i < sortedTabs.size(); i = i + 1) {
            if (isModded(sortedTabs.get(i))) {
                sortedTabs.set(i, moddedTabs.get(tabIndex = tabIndex + 1));
            }
        }

        return sortedTabs;
    }

    // Identifies tabs registered outside the vanilla namespace
    private static boolean isModded(CreativeModeTab tab) {
        var key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        return key != null && !key.getNamespace().equals("minecraft");
    }

    // Gets the tab title
    private static String displayName(CreativeModeTab tab) {
        return tab.getDisplayName().getString();
    }

    // Gets the tab registry name
    private static String registryName(CreativeModeTab tab) {
        return Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab)).toString();
    }
}