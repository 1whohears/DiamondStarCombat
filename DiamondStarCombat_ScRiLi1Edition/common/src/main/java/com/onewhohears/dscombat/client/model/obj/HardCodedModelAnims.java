package com.onewhohears.dscombat.client.model.obj;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.client.model.obj.custom.*;
import com.onewhohears.dscombat.entity.parts.EntityPart;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class HardCodedModelAnims {
	
	private static final Map<String, ObjVehicleModel<EntityVehicle>> models = new HashMap<>();
	private static final Map<String, ObjPartModel<?>> partModels = new HashMap<>();
	
	public static void reload() {
		models.clear();
		models.put("bronco_plane", new BroncoPlaneModel());
		models.put("corvette", new CorvetteModel());
		models.put("google_sub", new GoogleSubModel());
		models.put("jason_plane", new JasonPlaneModel());
		partModels.clear();
		partModels.put("minigun_turret", new MinigunTurretModel());
		partModels.put("heavy_tank_turret", new AutoloadingTurretModel());
		partModels.put("steve_up_smash", new SteveUpSmashModel());
		partModels.put("sam_launcher", new SamLauncherModel());
		partModels.put("mls", new MLSModel());
		partModels.put("torpedo_tubes", new TorpedoTubesModel());
		partModels.put("aa_turret", new AATurretModel());
		partModels.put("ciws", new CIWSModel());
		partModels.put("mark7", new Mark7GunModel());
		partModels.put("mark45", new Mark45GunModel());
		partModels.put("mlrs", new MLRSModel());
		partModels.put("artillery_cannon", new ArtilleryCannonModel());
		partModels.put("air_scan_a", new Radar1Model());
		partModels.put("air_scan_b", new Radar2Model());
		partModels.put("survey_all_a", new StickRadarModel());
		partModels.put("survey_all_b", new BallRadarModel());
		partModels.put("bomb_rack", new BombRackModel());
		partModels.put("vls", new VLSModel());
	}
	
	public static ObjVehicleModel<EntityVehicle> get(String id) {
		return models.get(id);
	}

	public static <E extends EntityPart> ObjPartModel<E> getPartModel(String id) {
		return (ObjPartModel<E>) partModels.get(id);
	}

}
