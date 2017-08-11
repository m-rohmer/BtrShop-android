package io.btrshop.rxcache;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.LifeCache;
import io.rx_cache2.ProviderKey;

/**
 * Created by martin on 08/08/2017.
 */

public interface Providers {
    @ProviderKey("string-10-seconds-ttl")
    @LifeCache(duration = 10, timeUnit = TimeUnit.SECONDS)
    Observable<String> getStringsWith10SecondsLifeTime(Observable<String> oString);
}
