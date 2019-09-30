package com.cotemig.fluo.database.fw;

import android.database.Cursor;

import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.List;

@Table(name = "base")
public class TOBase {

    protected static final int FIELD_TYPE_INTEGER = 1;
    protected static final int FIELD_TYPE_FLOAT = 2;
    protected static final int FIELD_TYPE_STRING = 3;

    public JSONObject getJson() throws Exception {
        return Helper.createJSON(this);
    }

    public String getColumns() throws Exception {
        return Helper.obterColunasString(this);
    }

    public String getTableName() throws Exception {
        return Helper.obterNomeTabela(this);
    }

    public void load(Cursor cursor) throws Exception {

        List<Field> colunas = Helper.obterColunas(this);

        String coluna;
        int i = 0;
        for (Field f : colunas) {
            coluna = f.getAnnotation(Column.class).name();

            if (cursor.getType(i) == FIELD_TYPE_INTEGER) {
                Helper.runSetter(coluna, this, cursor.getInt(i++));
            } else if (cursor.getType(i) == FIELD_TYPE_FLOAT) {
                Helper.runSetter(coluna, this, cursor.getFloat(i++));
            } else if (cursor.getType(i) == FIELD_TYPE_STRING) {
                Helper.runSetter(coluna, this, cursor.getString(i++));
            } else {
                i++;
            }
        }

    }

    public void loadManual(Cursor cursor) throws Exception {

    }

    public String getString() {
        return "";
    }

}