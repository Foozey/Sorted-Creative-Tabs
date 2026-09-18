package com.fooze.sortedcreativetabs.mixin;

import com.fooze.sortedcreativetabs.Config;
import com.fooze.sortedcreativetabs.SortedCreativeTabs;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeTabsScreenMixin {
    @Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/CreativeModeTabs;tabs()Ljava/util/List;"))
    private static List<CreativeModeTab> sortModdedTabs() {
        return SortedCreativeTabs.sortModdedTabs(CreativeModeTabs.tabs(), Config.get().enableSorting());
    }
}