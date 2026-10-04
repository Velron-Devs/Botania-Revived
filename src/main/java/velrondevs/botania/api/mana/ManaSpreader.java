package velrondevs.botania.api.mana;

import velrondevs.botania.api.internal.ManaBurst;

import java.util.UUID;

public interface ManaSpreader extends ManaCollector {

	void setCanShoot(boolean canShoot);

	int getBurstParticleTick();

	void setBurstParticleTick(int i);

	int getLastBurstDeathTick();

	void setLastBurstDeathTick(int ticksExisted);

	ManaBurst runBurstSimulation();

	float getRotationX();

	float getRotationY();

	void setRotationX(float rot);

	void setRotationY(float rot);

	void commitRedirection();

	void pingback(ManaBurst burst, UUID expectedIdentity);

	UUID getIdentifier();
}
