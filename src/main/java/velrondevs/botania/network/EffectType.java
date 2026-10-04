package velrondevs.botania.network;

public enum EffectType {
	PAINT_LENS(1),
	ARENA_INDICATOR(0),
	ITEM_SMOKE(2),
	SPARK_NET_INDICATOR(2),
	SPARK_MANA_FLOW(3),
	ENCHANTER_DESTROY(0),
	BLACK_LOTUS_DISSOLVE(0),
	TERRA_PLATE(1),
	FLUGEL_EFFECT(1),
	PARTICLE_BEAM(3),
	DIVA_EFFECT(1),
	HALO_CRAFT(1),
	AVATAR_TORNADO_JUMP(1),
	AVATAR_TORNADO_BOOST(1),
	THUNDERCALLER_EFFECT(-1),
	GRASS_SEED_PARTICLES(1)
	;

	public final int argCount;

	EffectType(int argCount) {
		this.argCount = argCount;
	}
}
