package android.rss.model.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.rss.model.RssFeedModel;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadRssTask implements Task {

    private Thread rssLoadTask;

    private String urlLink;

    public LoadRssTask(String urlLink) {
        this.urlLink = urlLink;
    }

    @Override
    public void start(final Handler handler) {
        // Создаем параллельный поток
        rssLoadTask = new Thread("rssLoadTask") {

            private InputStream inputStream;

            @Override
            public void run() {
                try {
                    // Открываем соединение по урлу
                    URL url = new URL(urlLink);
                    inputStream = url.openConnection().getInputStream();

                    // Получили Rss каналы из сети
                    final RssFeedModel rssFeedModel = parseFeed(inputStream);

                    // Кладем в пакет
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DATA_KEY, rssFeedModel);

                    // Пакет передаем в Сообщение
                    Message message = new Message();
                    message.setData(bundle);

                    // Отправляем Сообщение Handler'у главного потока
                    handler.sendMessage(message);

                } catch (IOException e) {
                    Log.e("RSS", "Error", e);

                } catch (XmlPullParserException e) {
                    Log.e("RSS", "Error", e);

                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e("RSS", "Error", e);
                    }
                }
            }
        };

        rssLoadTask.start();
    }

    @Override
    public void stop() {
        rssLoadTask.interrupt();
    }

    // Парсим Rss каналы из потока данных
    private RssFeedModel parseFeed(InputStream inputStream)
            throws XmlPullParserException, IOException {

        RssFeedModel rssFeed = new RssFeedModel();

        String title = null;
        String link = null;
        String description = null;
        boolean isItem = false;

        try {
            XmlPullParser xmlPullParser = Xml.newPullParser();
            xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            xmlPullParser.setInput(inputStream, null);

            xmlPullParser.nextTag();
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                int eventType = xmlPullParser.getEventType();

                String name = xmlPullParser.getName();
                if (name == null)
                    continue;

                if (eventType == XmlPullParser.END_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = false;
                    }
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
                        isItem = true;
                        continue;
                    }
                }

                Log.d("MyXmlParser", "Parsing name ==> " + name);
                String result = "";
                if (xmlPullParser.next() == XmlPullParser.TEXT) {
                    result = xmlPullParser.getText();
                    xmlPullParser.nextTag();
                }

                if (name.equalsIgnoreCase("title")) {
                    title = result;
                } else if (name.equalsIgnoreCase("link")) {
                    link = result;
                } else if (name.equalsIgnoreCase("description")) {
                    description = result;
                }

                if (title != null && link != null && description != null) {
                    if (isItem) {
                        rssFeed.addItem(new RssFeedModel(title, link, description));
                    } else {
                        rssFeed.setTitle(title);
                        rssFeed.setLink(link);
                        rssFeed.setDescription(description);
                    }

                    title = null;
                    link = null;
                    description = null;
                    isItem = false;
                }
            }

            return rssFeed;

        } finally {
            inputStream.close();
        }
    }
}
