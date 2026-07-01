package com.codex.onlyvanadv.integration.cloth;

import com.codex.onlyvanadv.config.OvaConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public final class ClothConfigCompat {
    private ClothConfigCompat() {}

    public static Screen build(Screen parent) {
        OvaConfig cfg = OvaConfig.loadOrCreate();

        ConfigBuilder b = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Only Vanilla Advancements"));
        b.setSavingRunnable(cfg::saveWithComments);

        ConfigCategory cat = b.getOrCreateCategory(Component.literal("General"));
        ConfigEntryBuilder eb = b.entryBuilder();


        cat.addEntry(eb.startStrList(Component.literal("Kept Mods"), new ArrayList<>(cfg.kept_mods))
                .setTooltip(Component.literal("Keep ALL advancements from these mods/datapacks (their short id)"))
                .setDefaultValue(List.of())
                .setSaveConsumer(list -> { cfg.kept_mods.clear(); cfg.kept_mods.addAll(list); })
                .build());

        cat.addEntry(eb.startStrList(Component.literal("Kept Advancements"), new ArrayList<>(cfg.kept_advancements))
                .setTooltip(Component.literal("Keep these specific advancement IDs (e.g. mymod:story/root)"))
                .setDefaultValue(List.of())
                .setSaveConsumer(list -> { cfg.kept_advancements.clear(); cfg.kept_advancements.addAll(list); })
                .build());

        cat.addEntry(eb.startStrList(Component.literal("Removed Mods"), new ArrayList<>(cfg.removed_mods))
                .setTooltip(Component.literal("Always remove ALL advancements from these mods/datapacks"))
                .setDefaultValue(List.of())
                .setSaveConsumer(list -> { cfg.removed_mods.clear(); cfg.removed_mods.addAll(list); })
                .build());

        cat.addEntry(eb.startStrList(Component.literal("Removed Advancements"), new ArrayList<>(cfg.removed_advancements))
                .setTooltip(Component.literal("Always remove these advancement IDs (e.g. pack:progress/special)"))
                .setDefaultValue(List.of())
                .setSaveConsumer(list -> { cfg.removed_advancements.clear(); cfg.removed_advancements.addAll(list); })
                .build());

        cat.addEntry(eb.startBooleanToggle(Component.literal("Keep Parent Advancements"), cfg.keep_parent_advancements)
                .setDefaultValue(true)
                .setTooltip(Component.literal("If enabled, also keep the required parent advancements for any kept entry"))
                .setSaveConsumer(v -> cfg.keep_parent_advancements = v)
                .build());

        // Strict behavior is always on internally: true vanilla is preserved, overrides removed unless kept.

        return b.build();
    }
}
