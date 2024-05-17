package com.onewhohears.dscombat.data.parts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;

public class SlotType {
	
	protected static final Logger LOGGER = LogUtils.getLogger();
	
	private static final Map<String, SlotType> slotTypes = new HashMap<>();
	
	public static SlotType EXTERNAL = registerSlotType("external");
	public static SlotType EXTERNAL_TOUGH = registerSlotType("external_tough", EXTERNAL);
	
	public static SlotType SEAT = registerSlotType("seat", EXTERNAL);
	
	public static SlotType MOUNT_LIGHT = registerSlotType("mount_light", SEAT);
	public static SlotType MOUNT_MED = registerSlotType("mount_med", MOUNT_LIGHT, EXTERNAL_TOUGH);
	public static SlotType MOUNT_HEAVY = registerSlotType("mount_heavy", MOUNT_MED);
	
	public static SlotType MOUNT_TECH = registerSlotType("mount_tech", MOUNT_LIGHT);
	
	public static SlotType PYLON_LIGHT = registerSlotType("pylon_light", EXTERNAL);
	public static SlotType PYLON_MED = registerSlotType("pylon_med", PYLON_LIGHT);
	public static SlotType PYLON_HEAVY = registerSlotType("pylon_heavy", PYLON_MED, EXTERNAL_TOUGH);
	
	public static SlotType INTERNAL = registerSlotType("internal");
	public static SlotType TECH_INTERNAL = registerSlotType("tech_internal", INTERNAL);
	public static SlotType HIGH_TECH_INTERNAL = registerSlotType("high_tech_internal", TECH_INTERNAL);
	
	public static SlotType SPIN_ENGINE = registerSlotType("spin_engine", INTERNAL);
	public static SlotType PUSH_ENGINE = registerSlotType("push_engine", INTERNAL);
	public static SlotType RADIAL_ENGINE = registerSlotType("radial_engine", INTERNAL);
	
	public static SlotType registerSlotType(SlotType type) {
		slotTypes.put(type.slotTypeName, type);
		return type;
	}
	
	public static SlotType registerSlotType(String name, SlotType... parents) {
		return registerSlotType(new SlotType(name, parents));
	}
	
	public static SlotType registerSlotType(String name) {
		return registerSlotType(new SlotType(name));
	}
	
	@Nullable
	public static SlotType getByName(String name) {
		if (!slotTypes.containsKey(name)) return checkOldNames(name);
		return slotTypes.get(name);
	}
	
	@Nullable
	public static SlotType checkOldNames(String name) {
		switch (name) {
		case "wing" : return PYLON_LIGHT;
		case "advanced_internal" : return TECH_INTERNAL;
		case "frame" : return MOUNT_LIGHT;
		case "heavy_frame" : return MOUNT_HEAVY;
		case "advanced_frame" : return MOUNT_TECH;
		case "light_turret" : return MOUNT_LIGHT;
		case "med_turret" : return MOUNT_MED;
		case "heavy_turret" : return MOUNT_HEAVY;
		}
		return null;
	}
	
	@Nullable
	public static SlotType getByOldOrdinal(int o) {
		switch (o) {
		case 0: return SEAT;
		case 1: return PYLON_HEAVY;
		case 2: return MOUNT_LIGHT;
		case 3: return INTERNAL;
		case 4: return HIGH_TECH_INTERNAL;
		case 5: return MOUNT_MED;
		case 6: return MOUNT_HEAVY;
		case 7: return SPIN_ENGINE;
		case 8: return PUSH_ENGINE;
		case 9: return MOUNT_HEAVY;
		case 10: return RADIAL_ENGINE;
		}
		return null;
	}
	
	public static void updateSlotTypeChildren() {
		LOGGER.debug("UPDATE SLOT TYPE CHILDREN");
		slotTypes.forEach((name, type) -> {
			List<String> children = new ArrayList<>();
			fillChildren(type, children);
			type.children = children.toArray(new String[children.size()]);
			LOGGER.debug(name+" : "+Arrays.toString(type.children));
		});
	}
	
	private static void fillChildren(SlotType parent, List<String> children) {
		slotTypes.forEach((name, type) -> {
			if (!type.hasParent(parent)) return;
			children.add(name);
			fillChildren(type, children);
		});
	}
	
	private final String slotTypeName;
	private final SlotType[] parents;
	private final ResourceLocation bg_texture;
	private String[] children = new String[0];
	
	public SlotType(String slotTypeName, SlotType... parents) {
		this.slotTypeName = slotTypeName;
		this.parents = parents;
		this.bg_texture = new ResourceLocation("dscombat:textures/ui/slots/"+slotTypeName+".png");
	}
	
	public SlotType(String slotTypeName) {
		this.slotTypeName = slotTypeName;
		this.parents = new SlotType[0];
		this.bg_texture = new ResourceLocation("dscombat:textures/ui/slots/"+slotTypeName+".png");
	}
	
	public String getSlotTypeName() {
		return slotTypeName;
	}
	
	public boolean isChild() {
		return parents.length > 0;
	}
	
	private boolean hasParent(SlotType parent) {
		for (int i = 0; i < parents.length; ++i) 
			if (parents[i].is(parent)) 
				return true;
		return false;
	}
	
	@Override
	public String toString() {
		return getSlotTypeName();
	}
	
	public ResourceLocation getBgTexture() {
		return bg_texture;
	}
	
	public String getTranslatableName() {
		return "slottype.dscombat."+slotTypeName;
	}
	
	public boolean is(SlotType type) {
		return this.getSlotTypeName().equals(type.getSlotTypeName());
	}
	
	public boolean isCompatible(SlotType type) {
		if (type == null) return false;
		if (is(type)) return true;
		for (int i = 0; i < children.length; ++i) 
			if (children[i].equals(type.getSlotTypeName())) 
				return true;
		return false;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SlotType type) return this.is(type);
		return false;
	}
	
}
