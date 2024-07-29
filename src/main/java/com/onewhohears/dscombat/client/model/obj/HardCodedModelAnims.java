package com.onewhohears.dscombat.client.model.obj;

import java.util.HashMap;
import java.util.Map;

import com.onewhohears.dscombat.client.model.obj.custom.BroncoPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.CorvetteModel;
import com.onewhohears.dscombat.client.model.obj.custom.EdenPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.FelixPlaneModel;
import com.onewhohears.dscombat.client.model.obj.custom.GoogleSubModel;
import com.onewhohears.dscombat.client.model.obj.custom.JasonPlaneModel;
import com.onewhohears.dscombat.entity.vehicle.EntityVehicle;

public class HardCodedModelAnims {
	
	private static Map<String, ObjVehicleModel<EntityVehicle>> models = new HashMap<>();
	
	public static void reload() {
		models.clear();
		models.put("bronco_plane", new BroncoPlaneModel());
		models.put("corvette", new CorvetteModel());
		models.put("eden_plane", new EdenPlaneModel());
		models.put("felix_plane", new FelixPlaneModel());
		models.put("google_sub", new GoogleSubModel());
		models.put("jason_plane", new JasonPlaneModel());
		models.put("gronk_battleship", new ObjVehicleModel<>("battleship"));
	}
	
	public static ObjVehicleModel<EntityVehicle> get(String id) {
		return models.get(id);
	}
	
}
