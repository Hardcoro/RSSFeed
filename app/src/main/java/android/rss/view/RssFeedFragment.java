package android.rss.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.rss.R;
import android.rss.RssFeedDetailActivity;
import android.rss.model.RssFeed;
import android.rss.model.task.LoadRssTask;
import android.rss.model.task.ReadRssTask;
import android.rss.model.task.SaveRssTask;
import android.rss.presenter.RssFeedPresenter;
import android.rss.presenter.RssFeedPresenterImpl;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

public class RssFeedFragment extends Fragment implements RssFeedView  {

    private RssFeedPresenter presenter;

    private EditText editText;
    private View progressBar;
    private RssAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new RssFeedPresenterImpl(this,
                new LoadRssTask(),
                new SaveRssTask(getActivity().getApplicationContext()),
                new ReadRssTask(getActivity().getApplicationContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rss, container, false);

        editText = view.findViewById(R.id.editText);

        View addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(this);

        progressBar = view.findViewById(R.id.progressBar);

        adapter = new RssAdapter();
        adapter.setOnRssClickListener(this);
        adapter.setOnDeleteRssClickListener(this);

        recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        presenter.onAttach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        presenter.onDetach();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showRssList() {
        editText.setText(null);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRssList() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void updateRssList(List<RssFeed> rssList) {
        adapter.setRssList(rssList);
    }

    @Override
    public void onDeleteRssClick(RssFeed rss) {
        presenter.deleteRss(rss);
    }

    @Override
    public void onRssClick(RssFeed rss) {
        Intent intent = new Intent(getContext(), RssFeedDetailActivity.class);
        intent.putExtra(RssFeedDetailActivity.URL_KEY, rss.getLink());

        getContext().startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addButton) {
            presenter.addRss(editText.getText().toString());

        } else {
            Log.e("RSS", "Unknown ID = " + view.getId());
        }
    }
}
