package android.rss;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.rss.view.RssFeedFragment;

public class RssFeedActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            RssFeedFragment rssFeedFragment = new RssFeedFragment();
            fragmentTransaction.add(R.id.content, rssFeedFragment);
            fragmentTransaction.commit();
        }
    }
}