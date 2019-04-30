package android.rss;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class RssFeedActivity extends Activity {

    private static final String RSS_KEY = "RSS_KEY";

    private LinearLayout rssPlaceholder;

    private Thread rssLoadTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rssPlaceholder = findViewById(R.id.rss_placeholder);

        rssLoadTask = createRssTask("http://feed.androidauthority.com");
        // Запускаем параллельный поток
        rssLoadTask.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rssLoadTask.interrupt();
    }

    private Thread createRssTask(final String urlLink) {
        // Создаем параллельный поток
        return new Thread("rssLoadTask") {

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
                    bundle.putSerializable(RSS_KEY, rssFeedModel);

                    // Пакет передаем в Сообщение
                    Message message = new Message();
                    message.setData(bundle);

                    // Отправляем Сообщение Handler'у главного потока
                    resultHandler.sendMessage(message);

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
    }

    // Добавляем фид на экран
    private void showRssFeed(String rss) {
        TextView rssText = (TextView) LayoutInflater
                .from(RssFeedActivity.this)
                .inflate(R.layout.rss_text, rssPlaceholder, false);

        rssText.setText(rss);

        rssPlaceholder.addView(rssText);
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

    // Handler создается в Главном потоке
    private Handler resultHandler = new Handler(Looper.myLooper()) {

        @Override
        public void handleMessage(Message message) {
            // Получаем пакет из Сообщения
            Bundle bundle = message.getData();

            // Из пакета получаем Rss каналы
            RssFeedModel rssFeedModel = (RssFeedModel) bundle.getSerializable(RSS_KEY);

            // Показываем базовые данные канала
            showRssFeed(rssFeedModel.toString());

            // Показываем каналы
            for (int i = 0; i < rssFeedModel.getItemsSize(); i++) {
                showRssFeed(rssFeedModel.getItem(i).toString());
            }
        }
    };
}