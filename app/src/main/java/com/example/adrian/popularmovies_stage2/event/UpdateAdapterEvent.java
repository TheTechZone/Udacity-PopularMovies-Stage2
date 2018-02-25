package com.example.adrian.popularmovies_stage2.event;

/**
 * Created by adrian on 25.02.2018.
 */

public class UpdateAdapterEvent {
    private boolean shouldUpdate;

    public boolean isShouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
