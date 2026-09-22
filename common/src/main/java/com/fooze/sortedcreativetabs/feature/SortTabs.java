package com.fooze.sortedcreativetabs.feature;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class SortTabs {
    // Sorts modded tabs in alphabetical order by title, then id
    public static List<CreativeModeTab> sort(List<CreativeModeTab> tabs, boolean enabled) {
        // Only sort if the config option is enabled
        if (!enabled) {
            return tabs;
        }

        List<CreativeModeTab> sortedTabs = new ArrayList<>(tabs);

        // Sort by title first, then id
        List<CreativeModeTab> moddedTabs = sortedTabs.stream()
                .filter(SortTabs::isModdedTab).sorted(Comparator
                        .comparing(SortTabs::title, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(SortTabs::title)
                        .thenComparing(SortTabs::id))
                .toList();

        int tabIndex = 0;

        // Replace the default modded tab order with the sorted order
        for (int i = 0; i < sortedTabs.size(); i++) {
            if (isModdedTab(sortedTabs.get(i))) {
                sortedTabs.set(i, moddedTabs.get(tabIndex++));
            }
        }

        return sortedTabs;
    }

    // Identifies tabs registered outside the vanilla namespace
    private static boolean isModdedTab(CreativeModeTab tab) {
        Identifier key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
        return key != null && !key.getNamespace().equals("minecraft");
    }

    // Gets the tab title
    private static String title(CreativeModeTab tab) {
        return tab.getDisplayName().getString();
    }

    // Gets the tab id
    private static String id(CreativeModeTab tab) {
        return Objects.requireNonNull(BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab)).toString();
    }
}