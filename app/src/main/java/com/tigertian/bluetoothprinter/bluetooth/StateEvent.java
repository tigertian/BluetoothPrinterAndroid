package com.tigertian.bluetoothprinter.bluetooth;

/**
 * Eventbus message event
 * @author tianlu
 */
public class StateEvent {
    public StateType type;

    public StateEvent(StateType type) {
        this.type = type;
    }
}
