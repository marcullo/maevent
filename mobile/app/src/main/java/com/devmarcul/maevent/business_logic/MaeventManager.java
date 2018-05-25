package com.devmarcul.maevent.business_logic;

import com.devmarcul.maevent.content_provider.hardcoded.MaeventParamsBuilder;
import com.devmarcul.maevent.data.ContentUpdater;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;

public class MaeventManager implements ContentUpdater<Maevent> {

    @Override
    public  void updateContent(Maevent event) {
        MaeventParams params = event.getParams();

        //TODO Replace dummy initialization with data base query
        params = MaeventParamsBuilder.build();

        if (Maevent.areParamsValid(params)) {
            event.setParams(params);
        }
    }
}
