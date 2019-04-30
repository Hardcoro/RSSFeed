package android.rss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

class RssFeedModel implements Serializable {

    private String title;

    private String link;

    private String description;

    private List<RssFeedModel> items;

    RssFeedModel() {
        title = null;
        link = null;
        description = null;
        items = new ArrayList<>();
    }

    RssFeedModel(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.items = new ArrayList<>();
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getLink() {
        return link;
    }

    void setLink(String link) {
        this.link = link;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void addItem(RssFeedModel item) {
        items.add(item);
    }

    RssFeedModel getItem(int position) {
        return items.get(position);
    }

    int getItemsSize() {
        return items.size();
    }

    @Override
    public String toString() {
        return "Новость" + '\n' + title + '\n' + "Ссылка" + '\n' + link + '\n' + "Описание" + '\n' + description;
    }
}