package com.devmarcul.maevent.interfaces;

import android.os.Bundle;

public interface InstanceSaver {
    Bundle createDataOnSaveInstanceState(Bundle state);
    void recreateDataOnCreateState(final Bundle state);
    void recreateDataOnRestore(final Bundle state);
    String getStringFromBundleForDebug(final Bundle state);
}
