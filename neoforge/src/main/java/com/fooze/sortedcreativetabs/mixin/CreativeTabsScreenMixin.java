package com.fooze.sortedcreativetabs.mixin;

import com.fooze.sortedcreativetabs.Config;
import com.fooze.sortedcreativetabs.SortedCreativeTabs;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.common.CreativeModeTabRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(CreativeModeInventoryScreen.class)
public class CreativeTabsScreenMixin {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/CreativeModeTabRegistry;getSortedCreativeModeTabs()Ljava/util/List;"))
    private static List<CreativeModeTab> sortModdedTabs() {
        return SortedCreativeTabs.sortModdedTabs(CreativeModeTabRegistry.getSortedCreativeModeTabs(), Config.get().enableSorting());
    }
}