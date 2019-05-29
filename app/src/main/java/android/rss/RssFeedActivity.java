package android.rss;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.rss.view.RssFeedFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RssFeedActivity extends Activity implements View.OnClickListener {

    Button btnAdd, btnRead, btnClear;
    EditText etName;

    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        RssFeedFragment rssFeedFragment = new RssFeedFragment();
        fragmentTransaction.add(R.id.fragment_placeholder, rssFeedFragment);
        fragmentTransaction.commit();

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        //btnRead = (Button) findViewById(R.id.btnRead);
        //btnRead.setOnClickListener(this);

        //btnClear = (Button) findViewById(R.id.btnClear);
        //btnClear.setOnClickListener(this);

        //etName = (EditText) findViewById(R.id.etName);

        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        String name = etName.getText().toString();

        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();


        switch (v.getId()) {

            case R.id.btnAdd:
                contentValues.put(DBHelper.KEY_NAME, name);

                database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
                break;

            case R.id.btnRead:
                Cursor cursor = database.query(DBHelper.TABLE_CONTACTS, null, null, null, null, null, null);

                if (cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    do {
                        Log.d("mLog", "ID = " + cursor.getInt(idIndex) +
                                ", name = " + cursor.getString(nameIndex));
                    } while (cursor.moveToNext());
                } else
                    Log.d("mLog","0 rows");

                cursor.close();
                break;

            case R.id.btnClear:
                database.delete(DBHelper.TABLE_CONTACTS, null, null);
                break;
        }
        dbHelper.close();
    }
}