package android.rss.model;

import java.io.Serializable;

public class RssFeed implements Serializable {

    private String title;

    private String link;

    private String description;

    public RssFeed(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    @Override
    public String toString() {
        return "Новость" + '\n' + title + '\n' + "Ссылка" + '\n' + link + '\n' + "Описание" + '\n' + description;
    }
}