package android.rss.view;

import android.app.Fragment;
import android.os.Bundle;
import android.rss.R;
import android.rss.model.LoadRssTask;
import android.rss.presenter.RssFeedPresenter;
import android.rss.presenter.RssFeedPresenterImpl;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RssFeedFragment extends Fragment implements RssFeedView {

    private LinearLayout rssLayout;

    private RssFeedPresenter presenter = new RssFeedPresenterImpl(this,
            new LoadRssTask("http://feed.androidauthority.com"));

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        rssLayout = view.findViewById(R.id.rss_layout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.startLoadRss();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.stopLoadRss();
    }

    @Override
    public void showRss(String rss) {
        TextView rssText = (TextView) LayoutInflater
                .from(getContext())
                .inflate(R.layout.rss_text, rssLayout, false);

        rssText.setText(rss);

        rssLayout.addView(rssText);
    }
}
