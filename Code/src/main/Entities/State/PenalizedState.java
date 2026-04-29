package Entities.State;

public class PenalizedState implements PlayerState {
    @Override
    public void handleInput() { /* Movimiento lento */ }
    
    @Override
    public double getSpeedMultiplier() {
        return 0.7;
    }
}
