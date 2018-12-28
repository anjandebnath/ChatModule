package com.uss.chatmodule;

import android.app.Application;
import android.content.Context;

/**
 * Created by Anjan Debnath on 12/5/2018.
 * Copyright (c) 2018, W3 Engineers Ltd. All rights reserved.
 */
public class ChatModuleApplication extends Application {

    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();

        sContext = getApplicationContext();


    }

    public static ChatModuleApplication get(Context context) {
        return (ChatModuleApplication) context.getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }




}
