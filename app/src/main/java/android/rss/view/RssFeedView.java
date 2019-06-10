package android.rss.view;

import android.rss.model.RssFeed;
import android.view.View;

import java.util.List;

public interface RssFeedView extends OnRssClickListener, OnDeleteRssClickListener, View.OnClickListener {

    void showProgress();

    void hideProgress();

    void showRssList();

    void hideRssList();

    void updateRssList(List<RssFeed> rssList);
}
