package android.rss;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class RssFeedDetailActivity extends Activity {

    public static final String URL_KEY = "URL_KEY";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String url = getIntent().getStringExtra(URL_KEY);
        ((WebView) findViewById(R.id.webView)).loadUrl(url);
    }
}
