package com.example.readgroup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.apphx.presention.contact.list.HxContactsFragment;
import com.example.apphx.presention.conversation.HxConversationFragment;
import com.example.readgroup.presentation.book.booklist.BooksFragment;
import com.example.readgroup.presentation.user.user.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.text_books)
    TextView textBooks;
    @BindView(R.id.text_contacts)
    TextView textContacts;
    @BindView(R.id.text_conversations)
    TextView textConversations;
    @BindView(R.id.text_me)
    TextView textMe;


    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    @Override
    public void onContentChanged() {
        super.onContentChanged();
        unbinder = ButterKnife.bind(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);
        textBooks.setSelected(true);
    }

    @OnClick({R.id.text_books, R.id.text_contacts, R.id.text_conversations, R.id.text_me})
    public void chooseFragment(View view) {
        switch (view.getId()) {
            case R.id.text_books:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.text_contacts:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.text_conversations:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.text_me:
                viewPager.setCurrentItem(3, false);
                break;
            default:
                throw new RuntimeException();

        }
    }


    private FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new BooksFragment();
                case 1:
                    return new HxContactsFragment();
                case 2:
                    return new HxConversationFragment();
                case 3:
                    return new UserFragment();
                default:
                    throw new RuntimeException();
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    };


    //------------start ViewPager Listener-----------
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        textBooks.setSelected(position == 0);
        textContacts.setSelected(position == 1);
        textConversations.setSelected(position == 2);
        textMe.setSelected(position == 3);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //------------end ViewPager Listener-----------


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
