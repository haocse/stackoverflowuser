package code.haotran.testing;

import android.app.Application;

import code.haotran.testing.BuildConfig;

import timber.log.Timber;

/**
 * @author Hao Tran
 *
 */
public class SOFApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
