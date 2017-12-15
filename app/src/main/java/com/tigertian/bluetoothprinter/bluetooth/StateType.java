package com.tigertian.bluetoothprinter.bluetooth;

/**
 * Eventbus message type
 */
public enum StateType {
    /**
     * Constants that indicate the current connection state
     * nothing to do
     */
    STATE_NONE,
    /**
     * now listening for incoming connections
     */
    STATE_LISTEN,

    /**
     * now initiating an outgoing connection
     */
    STATE_CONNECTING,

    /**
     * now connected to a remote device
     */
    STATE_CONNECTED,

    /**
     * the connection is lost
     */
    STATE_LOST,

    /**
     * failed to connect
     */
    STATE_FAILED
}
