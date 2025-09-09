package com.onewhohears.dscombat.client.renderer;

import com.onewhohears.dscombat.entity.weapon.EntityWeapon;

import com.onewhohears.onewholibs.client.renderer.RendererCustomAnimObjProjectileEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;

public class RendererObjWeapon<T extends EntityWeapon<?>> extends RendererCustomAnimObjProjectileEntity<T> {

	public RendererObjWeapon(Context ctx) {
		super(ctx);
	}

}
