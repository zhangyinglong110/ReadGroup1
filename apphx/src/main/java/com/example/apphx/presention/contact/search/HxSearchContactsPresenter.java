package com.example.apphx.presention.contact.search;

import com.example.apphx.basemvp.MvpPresenter;
import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.event.HxSearchContactEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2016/11/8 0008.
 */

public class HxSearchContactsPresenter extends MvpPresenter<HxSearchContactsView> {

    private final HxContactManager hxContactManager;

    public HxSearchContactsPresenter() {
        hxContactManager = HxContactManager.getInstance();
    }

    @Override
    public HxSearchContactsView getNullObject() {
        return HxSearchContactsView.NULL;
    }

    public void searchContact(String query) {
        getView().startLoading();
        hxContactManager.asyncSearchContact(query);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(HxSearchContactEvent event) {
        getView().stopLoading();

        if (event.isSuccess) {
            getView().showContacts(event.contacts);

            if (event.contacts.size() == 0) {
                getView().showSearchError("No match result!");
            }
        } else {
            getView().showSearchError(event.errorMeassage);
        }
    }
}
