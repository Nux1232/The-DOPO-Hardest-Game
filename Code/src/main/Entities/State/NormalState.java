package Entities.State;

public class NormalState implements PlayerState {
    @Override
    public void handleInput() { /* Movimiento normal */ }
    
    @Override
    public double getSpeedMultiplier() {
        return 1.0;
    }
}
