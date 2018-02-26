package com.example.adrian.popularmovies_stage2.event;


public class UpdateAdapterEvent {
    private boolean shouldUpdate;

    public boolean isShouldUpdate() {
        return shouldUpdate;
    }

    public void setShouldUpdate(boolean shouldUpdate) {
        this.shouldUpdate = shouldUpdate;
    }
}
