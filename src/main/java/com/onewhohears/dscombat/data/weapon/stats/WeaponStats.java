package com.onewhohears.dscombat.data.weapon.stats;

import static com.onewhohears.dscombat.DSCombatMod.MODID;

import java.util.List;
import java.util.function.BiConsumer;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.onewhohears.dscombat.data.jsonpreset.JsonPresetStats;
import com.onewhohears.dscombat.data.recipe.DSCIngredientBuilder;
import com.onewhohears.dscombat.data.weapon.instance.WeaponInstance;
import com.onewhohears.dscombat.init.ModEntities;
import com.onewhohears.dscombat.init.ModSounds;
import com.onewhohears.dscombat.util.UtilEntity;
import com.onewhohears.dscombat.util.UtilItem;
import com.onewhohears.dscombat.util.UtilMCText;
import com.onewhohears.dscombat.util.UtilParse;
import com.onewhohears.dscombat.util.UtilParticles;
import com.onewhohears.dscombat.util.UtilSound;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public abstract class WeaponStats extends JsonPresetStats {
	public static final int INFO_COLOR = 0x5F9EA0, COMPAT_COLOR = 0xADFF2F, TYPE_COLOR = 0x007FFF, SPECIAL_COLOR = 0x7FFFD4;
	protected static final ResourceLocation NONE_ICON = new ResourceLocation(MODID, "textures/ui/weapon_icons/none.png");

	private final int craftNum;
	private final int maxAge;
	private final int fireRate;
	private final boolean canShootOnGround;
	private final String entityTypeKey;
	private final String shootSoundKey;
	private final String rackTypeKey;
	private final String[] compatibleWeaponPart;
	private final String[] compatibleTurret;
	private final String itemKey;
	private final String modelId;
	private final ResourceLocation icon;
	
	private NonNullList<Ingredient> ingredients;
	private EntityType<?> entityType;
	private SoundEvent shootSound;
	private EntityType<?> rackType;
	
	public WeaponStats(ResourceLocation key, JsonObject json) {
		super(key, json);
		this.craftNum = UtilParse.getIntSafe(json, "craftNum", 0);
		this.maxAge = UtilParse.getIntSafe(json, "maxAge", 0);
		this.fireRate = UtilParse.getIntSafe(json, "fireRate", 0);
		this.canShootOnGround = UtilParse.getBooleanSafe(json, "canShootOnGround", false);
		this.entityTypeKey = UtilParse.getStringSafe(json, "entityTypeKey", "");
		this.shootSoundKey = UtilParse.getStringSafe(json, "shootSoundKey", "");
		this.rackTypeKey = UtilParse.getStringSafe(json, "rackTypeKey", "");
		this.compatibleWeaponPart = UtilParse.getStringArraySafe(json, "compatibleWeaponPart");
		this.compatibleTurret = UtilParse.getStringArraySafe(json, "compatibleTurret");
		this.itemKey = UtilParse.getStringSafe(json, "itemKey", "");
		this.modelId = UtilParse.getStringSafe(json, "modelId", getId());
		this.icon = new ResourceLocation(UtilParse.getStringSafe(json, "icon", getDefaultIconLocation()));
	}
	
	public WeaponInstance<?> createWeaponInstance() {
		return (WeaponInstance<?>) createPresetInstance();
	}
	
	public NonNullList<Ingredient> getIngredients() {
		if (ingredients == null) {
			ingredients = DSCIngredientBuilder.getIngredients(getJsonData());
		}
		return ingredients;
	}
	
	public int getCraftNum() {
		return craftNum;
	}
	
	public boolean canAngleDown() {
		return isBullet();
	}

	public int getMaxAge() {
		return maxAge;
	}

	public int getFireRate() {
		return fireRate;
	}
	
	public boolean canShootOnGround() {
		return canShootOnGround;
	}
	
	public abstract double getMobTurretRange();
	
	@Override
	public String toString() {
		return "["+getType().toString()+":"+getId()+"]";
	}
	
	public EntityType<?> getEntityType() {
		if (entityType == null) {
			entityType = UtilEntity.getEntityType(entityTypeKey, ModEntities.BULLET.get());
		}
		return entityType;
	}
	
	public SoundEvent getShootSound() {
		if (shootSound == null) {
			shootSound = UtilSound.getSoundById(shootSoundKey, ModSounds.BULLET_SHOOT_1);
		}
		return shootSound;
	}
	
	public EntityType<?> getRackEntityType() {
		if (rackType == null) {
			rackType = UtilEntity.getEntityType(rackTypeKey, ModEntities.XM12.get());
		}
		return rackType;
	}
	
	private Item item;
	private ItemStack stack;
	
	private Item getItem() {
		if (item == null) {
			item = UtilItem.getItem(itemKey);
		}
		return item;
	}
	
	public ItemStack getDisplayStack() {
		if (stack == null) {
			stack = new ItemStack(getItem());
			stack.setCount(craftNum);
			CompoundTag tag = new CompoundTag();
			tag.putString("weapon", getId());
			stack.setTag(tag);
		}
		return stack;
	}
	
	public ItemStack getNewItem() {
		return getDisplayStack().copy();
	}
	
	public String getItemKey() {
		return itemKey;
	}
	
	public String[] getCompatibleWeaponParts() {
		return compatibleWeaponPart;
	}
	
	public String[] getCompatibleTurrets() {
		return compatibleTurret;
	}
	
	public String getModelId() {
		return modelId;
	}
	
	public ResourceLocation getWeaponIcon() {
		return isNoWeapon() ? NONE_ICON : this.icon;
	}
	
	public String getDefaultIconLocation() {
		return MODID+":textures/ui/weapon_icons/default.png";
	}
	
	public abstract String getWeaponTypeCode();
	
	public void addToolTips(List<Component> tips, boolean advanced) {
		tips.add(getType().getDisplayNameComponent().setStyle(Style.EMPTY.withColor(TYPE_COLOR)));
		if (compatibleWeaponPart.length > 0) {
			MutableComponent weapons = UtilMCText.literal("Weapon Part: ").setStyle(Style.EMPTY.withColor(COMPAT_COLOR));
			for (int i = 0; i < compatibleWeaponPart.length; ++i) {
				if (i != 0) weapons.append(UtilMCText.literal(", "));
				weapons.append(UtilMCText.getItemName(compatibleWeaponPart[i]));
			}
			tips.add(weapons);
		}
		if (compatibleTurret.length > 0) {
			MutableComponent turrets = UtilMCText.literal("Turret: ").setStyle(Style.EMPTY.withColor(COMPAT_COLOR));
			for (int i = 0; i < compatibleTurret.length; ++i) {
				if (i != 0) turrets.append(UtilMCText.literal(", "));
				turrets.append(UtilMCText.getItemName(compatibleTurret[i]));
			}
			tips.add(turrets);
		}
		tips.add(UtilMCText.literal("Fire Rate: ").append(getFireRate()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
		if (advanced) {
			tips.add(UtilMCText.literal("Max Age: ").append(getMaxAge()+"").setStyle(Style.EMPTY.withColor(INFO_COLOR)));
			if (!canShootOnGround) tips.add(UtilMCText.literal("Must Fly").setStyle(Style.EMPTY.withColor(SPECIAL_COLOR)));
		}
	}
	
	public boolean isBullet() {
		return false;
	}
	
	public boolean isNoWeapon() {
		return false;
	}
	
	public boolean isIRMissile() {
		return false;
	}
	
	public boolean requiresRadar() {
		return false;
	}
	
	public boolean isAimAssist() {
		return false;
	}
	
	public static enum WeaponClientImpactType {
		SMALL_BULLET_IMPACT((level, pos) -> UtilParticles.bulletImpact(level, pos, 5)),
		SMALL_BULLET_EXPLODE((level, pos) -> UtilParticles.bulletExplode(level, pos, 2.5, true)),
		MED_BOMB_EXPLODE((level, pos) -> UtilParticles.bombExplode(level, pos, 5, true)),
		MED_MISSILE_EXPLODE((level, pos) -> UtilParticles.missileExplode(level, pos, 4, true));
		@Nullable
		public static WeaponClientImpactType getByOrdinal(int ordinal) {
			if (ordinal < 0 || ordinal >= values().length) return null;
			return values()[ordinal];
		}
		private final BiConsumer<Level, Vec3> clientImpactCallback;
		private WeaponClientImpactType(BiConsumer<Level, Vec3> clientImpactCallback) {
			this.clientImpactCallback = clientImpactCallback;
		}
		public void onClientImpact(Level level, Vec3 pos) {
			clientImpactCallback.accept(level, pos);
		}
	}
	
}
