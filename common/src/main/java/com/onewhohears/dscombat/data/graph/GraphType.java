package com.onewhohears.dscombat.data.graph;

import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetStats;
import com.onewhohears.onewholibs.data.jsonpreset.JsonPresetType;

public abstract class GraphType extends JsonPresetType {
	public static final FloatFloat FLOATFLOAT = FloatFloat.INSTANCE;
	public static class FloatFloat extends GraphType {
		public static final String ID = "floatfloat";
		public static final FloatFloat INSTANCE = new FloatFloat();
		public FloatFloat() {
			super(ID, FloatFloatGraph::new);
		}
	}
	public static final AoaLiftK AOALIFTK = AoaLiftK.INSTANCE;
	public static class AoaLiftK extends GraphType {
		public static final String ID = "aoaliftk";
		public static final AoaLiftK INSTANCE = new AoaLiftK();
		public AoaLiftK() {
			super(ID, AoaLiftKGraph::new);
		}
	}
	public static final FloatFloatMulti FLOATFLOAT_MULTI = FloatFloatMulti.INSTANCE;
	public static class FloatFloatMulti extends GraphType {
		public static final String ID = "floatfloat_multi";
		public static final FloatFloatMulti INSTANCE = new FloatFloatMulti();
		public FloatFloatMulti() {
			super(ID, FFMultiGraph::new);
		}
	}
	public static final TurnRatesBySpeed TURN_RATES_SPEED = TurnRatesBySpeed.INSTANCE;
	public static class TurnRatesBySpeed extends GraphType {
		public static final String ID = "turn_rates_speed";
		public static final TurnRatesBySpeed INSTANCE = new TurnRatesBySpeed();
		public TurnRatesBySpeed() {
			super(ID, TurnRatesBySpeedGraph::new);
		}
	}
	public GraphType(String id, JsonPresetStatsFactory<? extends JsonPresetStats> statsFactory) {
		super(id, statsFactory);
	}
}
