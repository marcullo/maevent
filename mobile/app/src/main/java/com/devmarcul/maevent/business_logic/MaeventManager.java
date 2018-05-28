package com.devmarcul.maevent.business_logic;

import com.devmarcul.maevent.content_provider.hardcoded.MaeventParamsBuilder;
import com.devmarcul.maevent.data.ContentUpdater;
import com.devmarcul.maevent.data.Maevent;
import com.devmarcul.maevent.data.MaeventParams;
import com.devmarcul.maevent.data.ThisUser;
import com.devmarcul.maevent.utils.Prompt;

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

    public Maevent createEvent(MaeventParams params) {
        if (Maevent.areParamsValid(params)) {
            //TODO add api query

            Maevent event = new Maevent();
            event.setParams(params);
            event.setHostId(ThisUser.getProfile().id);
            return event;
        }
        return null;
    }
}
