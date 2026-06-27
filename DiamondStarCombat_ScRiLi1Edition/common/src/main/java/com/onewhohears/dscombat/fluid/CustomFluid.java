package com.onewhohears.dscombat.fluid;

import dev.architectury.core.fluid.ArchitecturyFlowingFluid;
import dev.architectury.core.fluid.ArchitecturyFluidAttributes;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;

public class CustomFluid {

    public static class Flowing extends ArchitecturyFlowingFluid.Flowing {
        public Flowing(ArchitecturyFluidAttributes attributes) {
            super(attributes);
        }
    }

    public static class Source extends ArchitecturyFlowingFluid.Source {
        public Source(ArchitecturyFluidAttributes attributes) {
            super(attributes);
            this.registerDefaultState(this.getStateDefinition().any().setValue(LEVEL, 8));
        }

        @Override
        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(LEVEL);
        }
    }

}
