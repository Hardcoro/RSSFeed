package android.rss.presenter;

import android.rss.model.RssFeed;

public interface RssFeedPresenter {

    void onAttach();

    void onDetach();

    void addRss(String url);

    void deleteRss(RssFeed rss);
}
