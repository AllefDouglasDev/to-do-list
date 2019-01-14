package com.example.allef.tad;

import android.app.Application;
import android.content.Context;

/**
 * Created by Allef on 18/05/2018.
 */

public class MyApp extends Application
{
    /** Contexto da applicação */
    private static Context mContext;

    @Override
    public void onCreate()
    {
        mContext = getApplicationContext();
        super.onCreate();
    }

    /** Retorna o context da aplicação */
    public static Context getContext()
    {
        return mContext;
    }
}
