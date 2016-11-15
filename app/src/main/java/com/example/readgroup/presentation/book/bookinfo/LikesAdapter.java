package com.example.readgroup.presentation.book.bookinfo;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.apphx.model.HxContactManager;
import com.example.apphx.model.HxUserManager;
import com.example.readgroup.R;
import com.example.readgroup.network.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/14 0014.
 */

public class LikesAdapter extends BaseAdapter {
    private final List<UserEntity> likes;
    private final OnInviteContactListener listener;

    public LikesAdapter(OnInviteContactListener listener) {
        likes = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return likes.size();
    }

    @Override
    public UserEntity getItem(int position) {
        return likes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getObjectId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View itemView = (convertView != null) ? convertView : createItemView(parent);

        ViewHolder viewHolder = (ViewHolder) itemView.getTag();

        viewHolder.bind(getItem(position));

        return itemView;
    }

    /**
     * 给适配器设置数据
     *
     * @param data
     */
    public void setData(List<UserEntity> data) {
        likes.clear();
        likes.addAll(data);
        notifyDataSetChanged();
    }

    private View createItemView(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_favorite, parent, false);
        itemView.setTag(new ViewHolder(itemView));
        return itemView;
    }

    class ViewHolder {
        @BindView(R.id.image_avatar)
        ImageView imageAvatar;
        @BindView(R.id.text_name)
        TextView textName;
        @BindView(R.id.button_invite)
        Button buttonInvite;
        private UserEntity userEntity;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }

        void bind(UserEntity userEntity1) {
            this.userEntity = userEntity1;
            textName.setText(userEntity.getUsername());
            boolean isContact = HxContactManager.getInstance().isContact(userEntity.getObjectId());
            boolean isSelf = userEntity.getObjectId().equals(HxUserManager.getInstance().getCurrentUserId());
            if (isSelf || isContact) {
                buttonInvite.setVisibility(View.INVISIBLE);
            } else {
                buttonInvite.setVisibility(View.VISIBLE);
            }
            Glide.with(imageAvatar.getContext())
                    .load(userEntity.getAvatar())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.hyphenate.easeui.R.drawable.ease_default_avatar)
                    .into(imageAvatar);
        }

        @OnClick(R.id.button_invite)
        public void invite() {
            listener.onInvite(userEntity);
        }
    }


    interface OnInviteContactListener {
        void onInvite(@NonNull UserEntity userEntity);
    }


}
