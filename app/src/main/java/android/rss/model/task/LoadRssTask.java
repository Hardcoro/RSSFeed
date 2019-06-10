package android.rss.model.task;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.rss.model.RssFeed;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoadRssTask implements Task {

    private Thread task;

    @Override
    public void start(final Object data, final Handler handler) {
        // Создаем параллельный поток
        task = new Thread("task") {

            private InputStream inputStream;

            @Override
            public void run() {
                try {
                    // Открываем соединение по урлу
                    URL url = new URL((String) data);
                    inputStream = url.openConnection().getInputStream();

                    // Получили Rss каналы из сети
                    final List<RssFeed> rssList = parseFeed(inputStream);
                    Log.d("RSS", rssList.toString());

                    // Кладем в пакет
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(DATA_KEY, (Serializable) rssList);

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

        task.start();
    }

    @Override
    public void stop() {
        if (task != null) {
            task.interrupt();
        }
    }

    // Парсим Rss каналы из потока данных
    private List<RssFeed> parseFeed(InputStream inputStream)
            throws XmlPullParserException, IOException {

        List<RssFeed> rssList = new ArrayList<>();

        String title = null;
        String link = null;
        String description = null;

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
                    continue;
                }

                if (eventType == XmlPullParser.START_TAG) {
                    if (name.equalsIgnoreCase("item")) {
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

                    rssList.add(new RssFeed(title, link, description));

                    title = null;
                    link = null;
                    description = null;
                }
            }

            return rssList;

        } finally {
            inputStream.close();
        }
    }
}
