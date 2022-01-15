package fuzs.completionistsindex.world.entity;

public interface EnduranceEntity {
    int getMaxEndurance();

    int getEndurance();

    void setEndurance(int endurance);

    int decreaseEndurance(int amount);

    default boolean isOutOfEndurance() {
        return this.getEndurance() <= 0;
    }

    int increaseEndurance(int amount);
}
