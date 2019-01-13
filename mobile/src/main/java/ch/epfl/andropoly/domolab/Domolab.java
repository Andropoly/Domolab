package ch.epfl.andropoly.domolab;

import android.app.Application;

public class Domolab extends Application {

    private static Domolab mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Domolab getContext() {
        return mContext;
    }
}
