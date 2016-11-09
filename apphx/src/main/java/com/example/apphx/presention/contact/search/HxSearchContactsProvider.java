package com.example.apphx.presention.contact.search;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by Administrator on 2016/11/8 0008
 * 根据最近查询/浏览，提供简单的搜索建议
 */

public class HxSearchContactsProvider extends SearchRecentSuggestionsProvider {
    public static final int MODE = DATABASE_MODE_QUERIES;

    public static final String AUTHORITY = "com.example.apphx.presention.contact.search";

    public HxSearchContactsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
