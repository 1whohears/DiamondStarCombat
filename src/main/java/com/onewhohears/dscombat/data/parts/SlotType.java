package com.onewhohears.dscombat.data.parts;

import net.minecraft.resources.ResourceLocation;

public class SlotType {
	
	public static SlotType getByName(String name) {
		
	}
	
	private final String slotTypeName;
	private final ResourceLocation bg_texture;
	
	public SlotType(String slotTypeName) {
		this.slotTypeName = slotTypeName;
		this.bg_texture = new ResourceLocation("dscombat:textures/ui/slots/"+slotTypeName+".png");
	}
	
	public String getSlotTypeName() {
		return slotTypeName;
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SlotType type) return this.is(type);
		return false;
	}
	
	/*public static enum SlotType {
		SEAT("seat"),
		LIGHT_TURRET("light_turret"),
		MED_TURRET("med_turret"),
		HEAVY_TURRET("heavy_turret"),
		
		WING("wing"),
		FRAME("frame"),
		HEAVY_FRAME("heavy_frame"),
		ADVANCED_FRAME("advanced_frame"),
		
		INTERNAL("internal"),
		ADVANCED_INTERNAL("advanced_internal"),
		
		SPIN_ENGINE("spin_engine"),
		PUSH_ENGINE("push_engine"),
		RADIAL_ENGINE("radial_engine");
		
		public static final SlotType[] SEAT_ALL = {SEAT, LIGHT_TURRET, MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_LIGHT = {LIGHT_TURRET, MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_MED = {MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] TURRET_HEAVY = {HEAVY_TURRET};
		
		public static final SlotType[] INTERNAL_ALL = {INTERNAL, ADVANCED_INTERNAL, PUSH_ENGINE, SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ADVANCED = {ADVANCED_INTERNAL};
		public static final SlotType[] INTERNAL_ENGINE_SPIN = {SPIN_ENGINE};
		public static final SlotType[] INTERNAL_ENGINE_PUSH = {PUSH_ENGINE};
		public static final SlotType[] INTERNAL_ENGINE_RADIAL = {RADIAL_ENGINE};
		
		public static final SlotType[] EXTERNAL_ALL = {WING, FRAME, HEAVY_FRAME, ADVANCED_FRAME, MED_TURRET, HEAVY_TURRET};
		public static final SlotType[] EXTERNAL_HEAVY = {HEAVY_FRAME, HEAVY_TURRET};
		public static final SlotType[] EXTERNAL_ADVANCED = {ADVANCED_FRAME};
		@Nullable
		public static SlotType getByName(String name) {
			for (int i = 0; i < values().length; ++i)
				if (values()[i].getSlotTypeName().equals(name)) 
					return values()[i];
			return null;
		}
		@Nullable
		public static SlotType getByOldOrdinal(int o) {
			switch (o) {
			case 0: return SEAT;
			case 1: return WING;
			case 2: return FRAME;
			case 3: return INTERNAL;
			case 4: return ADVANCED_INTERNAL;
			case 5: return MED_TURRET;
			case 6: return HEAVY_TURRET;
			case 7: return SPIN_ENGINE;
			case 8: return PUSH_ENGINE;
			case 9: return HEAVY_FRAME;
			case 10: return RADIAL_ENGINE;
			}
			return null;
		}
		private final String slotTypeName;
		private final ResourceLocation bg_texture;
		private SlotType(String slotTypeName) {
			this.slotTypeName = slotTypeName;
			this.bg_texture = new ResourceLocation("dscombat:textures/ui/slots/"+slotTypeName+".png");
		}
		public String getSlotTypeName() {
			return slotTypeName;
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
	}*/
	
}
