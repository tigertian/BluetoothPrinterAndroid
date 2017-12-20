package com.tigertian.bluetoothprinter.bluetooth;

/**
 * Just the bt state message, no content.
 *
 * @author tianlu
 */
public class StateEvent {
    public StateType type;

    public StateEvent(StateType type) {
        this.type = type;
    }
}
