package com.cotemig.fluo.helper.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.cotemig.fluo.app.FluoApp;
import com.cotemig.fluo.helper.ZipHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;
    private final Context context;

    public DBHelper(Context context) {
        super(context, FluoApp.Companion.getDATABASE_NAME(), null,
                FluoApp.Companion.getCURRENT_REVISION());
        this.context = context;
    }

    public static boolean exists() {
        File f = new File(FluoApp.Companion.getABSOLUTE_DATABASE_PATH());
        return f.exists();
    }

    public synchronized static DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);

            try {
                instance.createDataBase();
            } catch (Exception e) {
                instance = null;
            }
        }

        return instance;
    }

    public static void copyDataBaseDownloaded() throws IOException {

        InputStream in = new FileInputStream(
                FluoApp.Companion.getPATH_APP_TEMP()
                        + FluoApp.Companion.getDATABASE_NAME());

        File f = new File(FluoApp.Companion.getDATABASE_PATH());
        boolean x = f.mkdirs();

        Log.i("xxx", "xxx" + x);

        OutputStream out = new FileOutputStream(
                FluoApp.Companion.getABSOLUTE_DATABASE_PATH());

        int length;
        byte[] buffer = new byte[1024];

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        out.flush();
        out.close();

        in.close();
    }

    public static void createDataBase(final OnCreateDataBaseListener listener) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                boolean success = false;

                try {
                    success = DBHelper
                            .getInstance(FluoApp.Companion.getInstance()) != null;
                } catch (Exception e) {
                    success = false;
                    e.printStackTrace();
                }

                return success;
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (listener != null) {
                    listener.onCreateDataBase(result.booleanValue());
                }
            }

        }.execute();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        return SQLiteDatabase.openDatabase(
                FluoApp.Companion.getABSOLUTE_DATABASE_PATH(), null,
                SQLiteDatabase.OPEN_READWRITE
                        | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            checkDB = SQLiteDatabase.openDatabase(
                    FluoApp.Companion.getABSOLUTE_DATABASE_PATH(), null,
                    SQLiteDatabase.OPEN_READONLY
                            | SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream in = context.getAssets().open(
                FluoApp.Companion.getASSETS_DATABASE_PATH() + "/"
                        + FluoApp.Companion.getDATABASE_NAME());

        File dir = new File(FluoApp.Companion.getDATABASE_PATH());
        dir.mkdirs();

        OutputStream out = new FileOutputStream(
                FluoApp.Companion.getABSOLUTE_DATABASE_PATH());

        int length;
        byte[] buffer = new byte[1024];

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        out.flush();
        out.close();

        in.close();
    }

    private void createDataBase() throws IOException {
        if (!checkDataBase()) {
            SQLiteDatabase checkDB = getReadableDatabase();
            checkDB.close();

            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    public interface OnCreateDataBaseListener {
        public void onCreateDataBase(boolean success);
    }

    public static void salvarBanco(Context context, InputStream input) throws Exception {


        OutputStream output = null;

        File f = new File(FluoApp.Companion.getDATABASE_PATH_TEMP());
        f.mkdir();

        f = new File(FluoApp.Companion.getPATH_APP_TEMP());
        f.mkdirs();

        f = new File(FluoApp.Companion.getPATH_APP_TEMP() + FluoApp.Companion.getDATABASE_NAME());
        if (f.exists()) {
            f.delete();
        }

        String file = FluoApp.Companion.getPATH_APP()
                + FluoApp.Companion.getDATABASE_NAME_TEMP();

        output = new FileOutputStream(file);

        byte[] buffer = new byte[1024];
        int len;

        while ((len = input.read(buffer)) > 0) {
            output.write(buffer, 0, len);
        }

        ZipHelper.unzip(file, FluoApp.Companion.getPATH_APP());

        copyDataBaseDownloaded();

    }

}
