package com.onewhohears.dscombat;

import com.onewhohears.dscombat.entity.weapon.EntityWeapon;
import com.onewhohears.dscombat.integration.distant_players.DSCDistantPlayers;
import com.onewhohears.dscombat.integration.minigame.DSCMiniGames;
import com.onewhohears.dscombat.integration.minigame.gen.DSCKitGenerator;
import com.onewhohears.dscombat.integration.minigame.gen.DSCShopGenerator;
import com.onewhohears.dscombat.integration.tacview.DSCTacViewMain;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DependencySafety {
	
	public static void fmlCommonSetup() {
		if (DSCombatMod.minigamesLoaded) DSCMiniGames.registerGames();
		if (DSCombatMod.distantPlayersLoaded) DSCDistantPlayers.register();
		if (DSCombatMod.tacViewLoaded) DSCTacViewMain.init();
	}
	
	public static void serverDataGen(PackOutput output, Consumer<JsonPresetGenerator<?>> register) {
		if (DSCombatMod.minigamesLoaded) {
            register.accept(new DSCKitGenerator(output));
            register.accept(new DSCShopGenerator(output));
		}
	}

	public static void addExtraEntityToRDP(@NotNull MinecraftServer server, @NotNull Entity entity,
                                           @NotNull ServerPlayer... visibleTo) {
		if (DSCombatMod.distantPlayersLoaded) DSCDistantPlayers.addExtraEntity(server, entity, visibleTo);
	}

    public static void onWeaponShoot(@NotNull EntityWeapon<?> weapon) {
        if (DSCombatMod.tacViewLoaded) DSCTacViewMain.onWeaponShoot(weapon);
    }
	
}
