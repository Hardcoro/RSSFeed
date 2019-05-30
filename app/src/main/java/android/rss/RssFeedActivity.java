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

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RssFeedFragment rssFeedFragment = new RssFeedFragment();
        fragmentTransaction.add(R.id.fragment_placeholder, rssFeedFragment);
        fragmentTransaction.commit();
    }

//    @Override
//    public void onClick(View v) {
//
//        String name = etName.getText().toString();
//
//        SQLiteDatabase database = dbHelper.getWritableDatabase();
//
//        ContentValues contentValues = new ContentValues();
//
//
//        switch (v.getId()) {
//
//            case R.id.btnAdd:
//                contentValues.put(DBHelper.KEY_NAME, name);
//
//                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
//                break;
//
//            case R.id.btnRead:
//                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);
//
//                if (cursor.moveToFirst()) {
//                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
//                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
//                    do {
//                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
//                                ", name = " + cursor.getString(nameIndex));
//                    } while (cursor.moveToNext());
//                } else
//                    Log.d("mLog","0 rows");
//
//                cursor.close();
//                break;
//
//            case R.id.btnClear:
//                database.delete(DBHelper.TABLE_CONTACTS, null, null);
//                break;
//        }
//        dbHelper.close();
//    }
}