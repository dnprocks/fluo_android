package com.cotemig.fluo.database.fw;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cotemig.fluo.helper.db.DBHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DAOBase extends DBHelper {

    private SQLiteDatabase db = null;

    public DAOBase(Context context) {
        super(context);
    }

    public static void insert(Context context, TOBase t) throws Exception {

        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        ContentValues values = Helper.createContentValuesComChave(t);

        String tabela = Helper.obterNomeTabela(t);

        d.db.insert(tabela, null, values);

        d.close();
    }

    public static long update(Context context, TOBase t) throws Exception {

        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        ContentValues values = Helper.createContentValuesSemChave(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        String tabela = Helper.obterNomeTabela(t);

        long x = d.db.update(tabela, values, where, vs);

        Log.i("Update:", "Total " + x);

        d.close();

        return x;
    }

    public static TOBase get(Context context, TOBase t) throws Exception {

        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        Cursor cursor = d.db.query(tabela, // a. table
                t.getColumns().split(","), // b. column names
                where, // c. selections
                vs, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null) {
            if (cursor.moveToNext()) {
                t.load(cursor);
            }
        }

        d.close();

        return t;
    }

    public static List<TOBase> list(Context context, TOBase t, String order) throws Exception {

        List<TOBase> l = new ArrayList<>();

        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        String tabela = Helper.obterNomeTabela(t);

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        Cursor cursor = d.db.query(tabela, // a. table
                t.getColumns().split(","), // b. column names
                where, // c. selections
                vs, // d. selections args
                null, // e. group by
                null, // f. having
                order, // g. order by
                null); // h. limit

        if (cursor != null) {
            while (cursor.moveToNext()) {
                t.loadManual(cursor);
                l.add(t);
            }
        }

        d.close();

        return l;
    }

    public static void deleteAll(Context context, TOBase t) throws Exception {
        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        long x = d.db.delete(t.getTableName(), null, null);

        Log.i("App", "Total: " + x);

        d.close();
    }

    public static void delete(Context context, TOBase t) throws Exception {

        DAOBase d = new DAOBase(context);

        if (d.db == null) {
            d.db = d.getWritableDatabase();
        }

        List<Field> colunasChave = Helper.obterColunasChave(t);

        String where = "";

        String[] vs = new String[colunasChave.size()];

        int i = 0;

        for (Field f : colunasChave) {
            Object o = Helper.runGetter(f, t);

            if (where.isEmpty()) {
                where = f.getName() + " = ? ";
            } else {
                where += " and " + f.getName() + " = ? ";
            }

            vs[i++] = (String) o;

        }

        long x = d.db.delete(t.getTableName(), where, vs);

        Log.i("App", "Total: " + x);

        d.close();
    }

}
