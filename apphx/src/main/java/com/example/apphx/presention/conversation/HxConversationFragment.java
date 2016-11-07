package com.example.apphx.presention.conversation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.example.apphx.R;
import com.example.apphx.presention.chat.HxChatActivity;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

/**
 * Created by Administrator on 2016/11/7 0007.
 */

public class HxConversationFragment extends EaseConversationListFragment implements HxConversationListView {

    private HxConversationsPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new HxConversationsPresenter();
        presenter.onCreat();
        presenter.atteachView(this);
        setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                HxChatActivity.open(getContext(), conversation.getUserName());
            }
        });
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        customUI();
    }

    private void customUI() {
        hideTitleBar();
        // 注册上下文菜单
        registerForContextMenu(conversationListView);
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v == conversationListView) {
            getActivity().getMenuInflater().inflate(R.menu.fragment_hx_conversation_list, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessagge;
        if (item.getItemId() == R.id.menu_delete_conversation) {
            deleteMessagge = false;
        } else if (item.getItemId() == R.id.menu_delete_conversation_and_message) {
            deleteMessagge = true;
        } else {
            throw new RuntimeException("Wrong breach!");
        }
        int position = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).position;
        EMConversation conversation = conversationListView.getItem(position);
        presenter.deleteConversation(conversation, deleteMessagge);

        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestory();
    }

    //-----------试图层的一个表现 start Interface---------------------
    @Override
    public void refreshConversations() {
        refresh();
    }
}
