package io.btrshop.rxcache;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;

/**
 * Created by martin on 08/08/2017.
 */

public class Repository {
    private final Providers providers;

    public static Repository init(File cacheDir) {
        return new Repository(cacheDir);
    }

    public Repository(File cacheDir) {
        providers = new RxCache.Builder()
                .persistence(cacheDir, new GsonSpeaker())
                .using(Providers.class);
    }

    public Observable<String> getStrings(String s) {
        return providers.getStringsWith10SecondsLifeTime(getExpensiveStrings(s));
    }

    private Observable<String> getExpensiveStrings(String s) {
        return Observable.just(new String(s));
    }
}
