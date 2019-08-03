package com.itxiaoer.commons.zip.rar;

import com.github.junrar.UnrarCallback;
import com.github.junrar.Volume;

public class RarUnpackMonitor implements UnrarCallback {
    @Override
    public boolean isNextVolumeReady(Volume volume) {
        return true;
    }

    @Override
    public void volumeProgressChanged(long l, long l1) {

    }
}
