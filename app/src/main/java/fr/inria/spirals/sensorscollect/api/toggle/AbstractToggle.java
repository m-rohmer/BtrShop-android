package fr.inria.spirals.sensorscollect.api.toggle;

import android.util.Log;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractToggle implements Toggle {

    private final Lock toggleLock = new ReentrantLock();
    private boolean running;

    @Override
    public final void start() {
        try {
            toggleLock.lock();
            if (isRunning()) {
                Log.i(getName(), "I'm already started");
            } else {
                try {
                    Log.i(getName(), "Starting me...");
                    if (!canStart()) {
                        throw new ToggleException("Unable to start because of unsatisfied prerequisites");
                    }
                    doStart();
                    running = true;
                    Log.i(getName(), "I'm started");
                } catch (final ToggleException e) {
                    Log.e(getName(), "I'm unable to be started", e);
                }
            }
        } finally {
            toggleLock.unlock();
        }

    }

    @Override
    public final void stop() {
        try {
            toggleLock.lock();
            if (isRunning()) {
                Log.i(getName(), "Stopping me...");
                try {
                    doStop();
                    running = false;
                    Log.i(getName(), "I'm stopped");
                } catch (final ToggleException e) {
                    Log.e(getName(), "I'm unable to be stopped", e);
                }
            } else {
                Log.i(getName(), "I'm already stopped");
            }
        } finally {
            toggleLock.unlock();
        }
    }

    @Override
    public void toggle() {
        try {
            toggleLock.lock();
            if (isRunning()) {
                stop();
            } else {
                start();
            }
        } finally {
            toggleLock.unlock();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public String getName() {
        return getClass().getCanonicalName();
    }

    protected abstract boolean canStart();

    protected abstract void doStart();

    protected abstract void doStop();

}
