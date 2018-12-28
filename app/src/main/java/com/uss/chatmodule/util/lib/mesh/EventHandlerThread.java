package com.uss.chatmodule.util.lib.mesh;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import io.left.rightmesh.mesh.MeshManager;


public class EventHandlerThread extends HandlerThread {

    public static final int KEY_EVENT_PEER = 1;
    public static final int KEY_EVENT_DATA = 2;
    private Handler mWorkHandler;
    private MeshProvider meshProvider;

    public EventHandlerThread(String name, MeshProvider meshProvider) {
        super(name);
        this.meshProvider = meshProvider;
    }

    public Handler obtainHandler() {
        return mWorkHandler;
    }

    /**
     * this is the system Handler Thread that will manage internal queue
     * so the received messages will be processed according to this queue.
     */
    public void prepareHandler() {
        mWorkHandler = new Handler(getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case KEY_EVENT_PEER:
                        meshProvider.processPeerChanged((MeshManager.RightMeshEvent) msg.obj);

                        break;
                    case KEY_EVENT_DATA:
                        meshProvider.processDataReceived((MeshManager.RightMeshEvent) msg.obj);
                        break;
                }
                return true;
            }
        });
    }

}
