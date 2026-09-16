package com.fooze.sortedcreativetabs.mixin;

import com.fooze.sortedcreativetabs.Config;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeTabsScreenPageMixin {
    @Redirect(method = "init", at = @At(
            value = "INVOKE",
            target = "Lnet/neoforged/neoforge/common/CreativeModeTabRegistry;getSortedCreativeModeTabs()Ljava/util/List;"
    ))

    // Sorts modded tabs in alphabetical order by title, then id
    private static List<CreativeModeTab> sortModdedTabs() {
        List<CreativeModeTab> tabs = CreativeModeTabRegistry.getSortedCreativeModeTabs();

        // Only sort tabs if the config option is enabled
        if (!Config.ENABLE_SORTING.get()) {
            return tabs;
        }

        List<CreativeModeTab> sortedTabs = new ArrayList<>(tabs);

        // Sort by title first, then id
        List<CreativeModeTab> moddedTabs = sortedTabs.stream()
                .filter(CreativeTabsScreenPageMixin::moddedTab).sorted(Comparator
                        .comparing(CreativeTabsScreenPageMixin::title, String.CASE_INSENSITIVE_ORDER)
                        .thenComparing(CreativeTabsScreenPageMixin::title)
                        .thenComparing(CreativeTabsScreenPageMixin::id))
                .toList();

        int tabIndex = 0;

        // Replace the default modded tab order with the sorted order
        for (int i = 0; i < sortedTabs.size(); i++) {
            if (moddedTab(sortedTabs.get(i))) {
                sortedTabs.set(i, moddedTabs.get(tabIndex++));
            }
        }

        return sortedTabs;
    }

    // Identifies tabs registered outside the vanilla namespace
    private static boolean moddedTab(CreativeModeTab tab) {
        ResourceLocation key = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(tab);
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