package com.onewhohears.dscombat.client.renderer;

import com.onewhohears.dscombat.client.model.obj.ObjWeaponModel;
import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.client.renderer.RendererObjEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class RendererObjWeapon<T extends EntityWeapon<?>> extends RendererObjEntity<T> {

	public RendererObjWeapon(Context ctx) {
		super(ctx, new ObjWeaponModel<>());
	}
	
	public RendererObjWeapon(Context ctx, ObjWeaponModel<T> model) {
		super(ctx, model);
	}

}
