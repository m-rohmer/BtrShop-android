package io.btrshop;

import android.app.Application;
import android.content.Context;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;

import io.btrshop.data.source.api.ApiComponent;
import io.btrshop.data.source.api.ApiModule;
import io.btrshop.data.source.api.DaggerApiComponent;
import io.btrshop.rxcache.Repository;

/**
 * Created by denis on 18/11/16.
 */

public class BtrShopApplication extends Application {

    private static Context mContext;
    private ApiComponent mApiComponent;
    private BeaconManager beaconManager;
    static private Repository repository;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        repository = Repository.init(getFilesDir());
        mApiComponent = DaggerApiComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .apiModule(new ApiModule(mContext.getResources().getString(R.string.base_url_api)))
                .build();

    }

    public static Context getContext(){
        return mContext;
    }

    static public Repository getRepository() {
        return repository;
    }

    public ApiComponent getApiComponent() {
        return mApiComponent;
    }
}
