package fuzs.completionistsindex.world.entity;

import fuzs.completionistsindex.CompletionistsIndex;

public interface EnduranceEntity {

    int getMaxEndurance();

    int getEndurance();

    void setEndurance(int endurance);

    int decreaseEndurance(int amount);

    default boolean isOutOfEndurance() {
        return this.getEndurance() <= 0;
    }

    default boolean isEnduranceFull() {
        return this.getEndurance() >= this.getMaxEndurance();
    }

    default int getEndurancePerTick() {
        return (int) Math.max(1, this.getMaxEndurance() * CompletionistsIndex.CONFIG.server().recoverEnduranceMultiplier / 300.0F);
    }

    int increaseEndurance(int amount);
}
