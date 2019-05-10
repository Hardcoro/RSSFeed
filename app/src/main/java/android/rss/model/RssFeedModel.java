package android.rss.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RssFeedModel implements Serializable {

    private String title;

    private String link;

    private String description;

    private List<RssFeedModel> items;

    public RssFeedModel() {
        title = null;
        link = null;
        description = null;
        items = new ArrayList<>();
    }

    public RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addItem(RssFeedModel item) {
        items.add(item);
    }

    public RssFeedModel getItem(int position) {
        return items.get(position);
    }

    public int getItemsSize() {
        return items.size();
    }

    @Override
    public String toString() {
        return "Новость" + '\n' + title + '\n' + "Ссылка" + '\n' + link + '\n' + "Описание" + '\n' + description;
    }
}