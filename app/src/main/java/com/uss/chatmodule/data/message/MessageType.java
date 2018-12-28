package com.uss.chatmodule.data.message;

/**
 * Define the types of messages
 * incoming message
 * outgoing message
 * new message marker
 * timestamp marker
 */
public abstract class MessageType {

    static final int OUTGOING = 0;
    static final int INCOMING = 1;
    static final int TIME_STAMP = 2;
    static final int NEW_MESSAGE = 3;

}
