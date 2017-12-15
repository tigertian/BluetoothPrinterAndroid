package com.tigertian.bluetoothprinter.bluetooth;

/**
 * Eventbus message event
 */
public class StateEvent {
    public StateType type;

    public StateEvent(StateType type) {
        this.type = type;
    }
}
