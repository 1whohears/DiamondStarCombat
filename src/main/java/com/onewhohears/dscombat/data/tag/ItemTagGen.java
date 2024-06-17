package com.onewhohears.dscombat.data.tag;

import org.jetbrains.annotations.Nullable;

import com.onewhohears.dscombat.DSCombatMod;
import com.onewhohears.dscombat.init.ModItems;
import com.onewhohears.dscombat.init.ModTags;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemTagGen extends ItemTagsProvider {

	public ItemTagGen(DataGenerator generator, BlockTagsProvider blockGen, @Nullable ExistingFileHelper existingFileHelper) {
		super(generator, blockGen, DSCombatMod.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ModTags.Items.ALUMINUM_INGOT)
			.addTag(ModTags.Items.FORGE_ALUMINUM_INGOT);
		tag(ModTags.Items.FORGE_ALUMINUM_INGOT)
			.add(ModItems.ALUMINUM_INGOT.get())
			.addOptional(new ResourceLocation("createindustry:aluminum_ingot"));
		tag(ModTags.Items.VEHICLE_CHAIN)
			.add(Items.CHAIN);
		tag(ModTags.Items.FORGE_OIL_BUCKET)
			.add(ModItems.OIL_BUCKET.get())
			.addOptional(new ResourceLocation("createindustry:crude_oil_fluid_bucket"));;
		tag(ModTags.Items.FOSSIL_OIL_CONVERTER)
			.add(Items.FLINT_AND_STEEL);
		tag(ModTags.Items.GAS_CAN)
			.add(ModItems.GAS_CAN.get(), ModItems.BIG_GAS_CAN.get(), ModItems.BIG_ASS_CAN.get());
		tag(ModTags.Items.SPRAY_CAN)
			.add(ModItems.SPRAYCAN.get());
		tag(ModTags.Items.AMMO)
			.add(ModItems.BULLET.get(), ModItems.BOMB.get(), ModItems.TRACK_AIR_MISSILE.get(), ModItems.TRACK_GROUND_MISSILE.get())
			.add(ModItems.IR_MISSILE.get(), ModItems.POS_MISSILE.get(), ModItems.TORPEDO.get(), ModItems.ANTIRADAR_MISSILE.get())
			.add(ModItems.B_20MM.get(), ModItems.B_50MMHE.get(), ModItems.B_120MMHE.get(), ModItems.AGM65G.get())
			.add(ModItems.AGM65L.get(), ModItems.AGM84E.get(), ModItems.AGM114K.get(), ModItems.AIM7F.get())
			.add(ModItems.AIM7MH.get(), ModItems.AIM9L.get(), ModItems.AIM9P5.get(), ModItems.AIM9X.get())
			.add(ModItems.AIM120B.get(), ModItems.AIM120C.get(), ModItems.TORPEDO1.get(), ModItems.RIFEL1.get())
			.add(ModItems.GRUETZ_BUNKER_BUSTER.get(), ModItems.MK13.get(), ModItems.AGM88G.get());
		tag(ModTags.Items.VEHICLE)
			.add(ModItems.JAVI_PLANE.get(), ModItems.ALEXIS_PLANE.get(), ModItems.WOODEN_PLANE.get(), ModItems.E3SENTRY_PLANE.get())
			.add(ModItems.BRONCO_PLANE.get(), ModItems.FELIX_PLANE.get(), ModItems.JASON_PLANE.get(), ModItems.EDEN_PLANE.get())
			.add(ModItems.NOAH_CHOPPER.get(), ModItems.ORANGE_TESLA.get(), ModItems.AXCEL_TRUCK.get(), ModItems.MRBUDGER_TANK.get())
			.add(ModItems.SMALL_ROLLER.get(), ModItems.NATHAN_BOAT.get(), ModItems.GRONK_BATTLESHIP.get(), ModItems.DESTROYER.get())
			.add(ModItems.CRUISER.get(), ModItems.CORVETTE.get(), ModItems.AIRCRAFT_CARRIER.get(), ModItems.ANDOLF_SUB.get())
			.add(ModItems.GOOGLE_SUB.get());
		tag(ModTags.Items.VEHICLE_PART)
			.addTag(ModTags.Items.VEHICLE_PART_WEAPON)
			.addTag(ModTags.Items.VEHICLE_PART_ENGINE)
			.addTag(ModTags.Items.VEHICLE_PART_FUEL_TANK)
			.addTag(ModTags.Items.VEHICLE_PART_FLARES)
			.add(ModItems.DATA_LINK.get(), ModItems.NIGHT_VISION_HUD.get(), ModItems.RADIO.get(), ModItems.ARMOR_PIECE.get())
			.add(ModItems.GIMBAL_CAMERA.get(), ModItems.SMALL_STORAGE_BOX.get(), ModItems.MED_STORAGE_BOX.get(), ModItems.LARGE_STORAGE_BOX.get())
			.add(ModItems.AR500.get(), ModItems.AR1K.get(), ModItems.AR2K.get(), ModItems.GR200.get())
			.add(ModItems.GR400.get(), ModItems.WR400.get(), ModItems.WR1K.get(), ModItems.GPR20.get())
			.add(ModItems.GPR100.get(), ModItems.AR20K.get(), ModItems.AXCEL_TRUCK_RADAR.get(), ModItems.AIR_SCAN_A.get())
			.add(ModItems.AIR_SCAN_B.get(), ModItems.SURVEY_ALL_A.get(), ModItems.SURVEY_ALL_B.get(), ModItems.SEAT.get());
		tag(ModTags.Items.VEHICLE_PART_WEAPON)
			.addTag(ModTags.Items.VEHICLE_TURRET)
			.add(ModItems.XM12.get(), ModItems.LIGHT_MISSILE_RACK.get(), ModItems.HEAVY_MISSILE_RACK.get(), ModItems.BOMB_RACK.get())
			.add(ModItems.ADL.get(), ModItems.VLS.get());
		tag(ModTags.Items.VEHICLE_TURRET)
			.add(ModItems.AA_TURRET.get(), ModItems.MINIGUN_TURRET.get(), ModItems.CIWS.get(), ModItems.MARK45_CANNON.get())
			.add(ModItems.HEAVY_TANK_TURRET.get(), ModItems.MARK7_CANNON.get(), ModItems.STEVE_UP_SMASH.get(), ModItems.SAM_LAUNCHER.get())
			.add(ModItems.TORPEDO_TUBES.get(), ModItems.MLS.get(), ModItems.CIWS.get(), ModItems.MARK45_CANNON.get());
		tag(ModTags.Items.VEHICLE_PART_ENGINE)
			.addTag(ModTags.Items.VEHICLE_ENGINE_EXTERNAL_PUSH)
			.addTag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_PUSH)
			.addTag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_RADIAL)
			.addTag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_SPIN);
		tag(ModTags.Items.VEHICLE_ENGINE_EXTERNAL_PUSH)
			.add(ModItems.CFM56.get());
		tag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_PUSH)
			.add(ModItems.TURBOFAN_F25.get(), ModItems.TURBOFAN_F145.get(), ModItems.TURBOFAN_F39.get(), ModItems.KLIMOV_RD33.get());
		tag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_RADIAL)
			.add(ModItems.CM_MANLY_52.get(), ModItems.ALLISON_V_1710.get(), ModItems.COMPOUND_TURBINE.get());
		tag(ModTags.Items.VEHICLE_ENGINE_INTERNAL_SPIN)
			.add(ModItems.C6_ENGINE.get(), ModItems.C12_ENGINE.get());
		tag(ModTags.Items.VEHICLE_PART_FUEL_TANK)
			.add(ModItems.LIGHT_FUEL_TANK.get(), ModItems.HEAVY_FUEL_TANK.get());
		tag(ModTags.Items.VEHICLE_PART_FLARES)
			.add(ModItems.BASIC_FLARE_DISPENSER.get());
	}
	
}
