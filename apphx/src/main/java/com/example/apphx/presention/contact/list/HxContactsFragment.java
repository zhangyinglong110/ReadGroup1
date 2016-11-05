package com.example.apphx.presention.contact.list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.apphx.R;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/4 0004.
 */

public class HxContactsFragment extends EaseContactListFragment implements HxContactsView {

    private HxContactsPresenter hxContactsPresenter;
    private String selectedhxId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hxContactsPresenter = new HxContactsPresenter();
        hxContactsPresenter.atteachView(this);
        hxContactsPresenter.onCreat();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUi();
        hxContactsPresenter.loadContacts();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == listView) {
            int position = ((AdapterView.AdapterContextMenuInfo) menuInfo).position;
            EaseUser easeUser = (EaseUser) listView.getItemAtPosition(position);
            if (easeUser == null) return;
            selectedhxId = easeUser.getUsername();
            getActivity().getMenuInflater().inflate(R.menu.fragment_hx_contact_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_delete_contact) {
            hxContactsPresenter.deleteContact(selectedhxId);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void customUi() {
        hideTitleBar();
        registerForContextMenu(listView);
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.partial_hx_contact_list_header, listView, false);
        View addContacts = headerView.findViewById(R.id.layout_add_contacts);
        View notifications = headerView.findViewById(R.id.layout_notifications);
        listView.addHeaderView(headerView);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hxContactsPresenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hxContactsPresenter.onDestory();
    }

    //------start 试图------------------
    @Override
    public void setContacts(List<String> contacts) {
        HashMap<String, EaseUser> contactMap = new HashMap<>();
        contactMap.clear();
        for (String hxId : contacts) {
            EaseUser easeUser = new EaseUser(hxId);
            contactMap.put(hxId, easeUser);
        }
        //将EaseUser设置到联系人列表上，进行一个显示
        setContactsMap(contactMap);
    }

    //刷新联系人列表的数据
    @Override
    public void refreshContacts() {
        refresh();
    }

    /**
     * 删除联系人
     *
     * @param msg
     */
    @Override
    public void deleteContact(String msg) {
        String info = getString(R.string.hx_contact_error_delete_contact, msg);
        Toast.makeText(getContext(), info, Toast.LENGTH_SHORT).show();
    }

    //------end 试图------------------
}
