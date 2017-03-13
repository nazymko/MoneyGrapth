package com.example.anazimko.moneygrapth;

import android.app.IntentService;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static android.database.Cursor.FIELD_TYPE_BLOB;
import static android.database.Cursor.FIELD_TYPE_FLOAT;
import static android.database.Cursor.FIELD_TYPE_INTEGER;
import static android.database.Cursor.FIELD_TYPE_NULL;
import static android.database.Cursor.FIELD_TYPE_STRING;

/**
 * Created by a.nazimko on 13.03.2017.
 */

public class SmsReadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SmsReadService(String name) {
        super(name);
    }

    public SmsReadService() {
        super("SmsReadService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        System.out.println("SmsReadService :  hello");
        read(this);

    }


    public void read(ContextWrapper wrapper) {
        final String SMS_URI_INBOX = "content://sms/inbox";
        System.out.println("SmsReadService :  hello");
        List<HashMap> sms = new ArrayList<>();
        try {
            Uri uri = Uri.parse(SMS_URI_INBOX);
            Cursor cur = wrapper.getContentResolver().query(uri, null, null, null, "date desc");
            String[] columnNames = cur.getColumnNames();
            if (cur.moveToFirst()) {
                do {
                    HashMap<String, Object> _sms = new HashMap<>();
                    for (String columnName : columnNames) {
                        _sms.put(columnName, getByName(columnName, cur));
                    }
                    sms.add(_sms);

                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            }
        } catch (SQLiteException ex) {
            Log.d("SQLiteException", ex.getMessage());
        }

        System.out.println("sms = " + sms);
    }

    private Object getByName(String columnName, Cursor cur) {
        int columnIndex = cur.getColumnIndex(columnName);
        int type = cur.getType(columnIndex);
        switch (type) {
            case FIELD_TYPE_BLOB:
                return cur.getBlob(columnIndex);
            case FIELD_TYPE_NULL:
                return null;
            case FIELD_TYPE_FLOAT:
                return cur.getFloat(columnIndex);
            case FIELD_TYPE_INTEGER:
                return cur.getLong(columnIndex);
            case FIELD_TYPE_STRING:
                return cur.getString(columnIndex);
        }
        return null;
    }
}
