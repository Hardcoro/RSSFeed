package android.rss.view;

import android.rss.R;
import android.rss.model.RssFeed;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class RssAdapter extends RecyclerView.Adapter<RssAdapter.RssItemViewHolder> {

    private List<RssFeed> rssList;
    private OnRssClickListener onRssClickListener;
    private OnDeleteRssClickListener onDeleteRssClickListener;

    void setRssList(List<RssFeed> rssList) {
        this.rssList = rssList;
        notifyDataSetChanged();
    }

    void setOnRssClickListener(OnRssClickListener onRssClickListener) {
        this.onRssClickListener = onRssClickListener;
    }

    void setOnDeleteRssClickListener(OnDeleteRssClickListener onDeleteRssClickListener) {
        this.onDeleteRssClickListener = onDeleteRssClickListener;
    }

    @Override
    public RssItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConstraintLayout itemView = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rss_item, parent, false);

        return new RssItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RssItemViewHolder holder, final int position) {
        final RssFeed rss = rssList.get(position);

        holder.titleView.setText(rss.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRssClickListener.onRssClick(rss);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDeleteRssClickListener.onDeleteRssClick(rss);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rssList.size();
    }

    static class RssItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        Button deleteButton;

        RssItemViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.rssText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
