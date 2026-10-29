package org.exodusstudio.frostbite.common.event.custom;

public abstract class CancellableEvent {
    private boolean canceled;

    public boolean isCanceled() {
        return canceled;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }
}
