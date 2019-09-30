package com.cotemig.fluo.database.fw;

import android.content.ContentValues;

import com.cotemig.fluo.helper.DateTime;

import org.json.JSONObject;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static ContentValues createContentValuesComChave(TOBase t) throws Exception {

        ContentValues values = new ContentValues();

        List<Field> colunas = Helper.obterColunas(t);

        for (Field f : colunas) {

            Object o = runGetter(f, t);

            if (o == null) {
                values.putNull(f.getName());
            } else if (o.getClass() == String.class) {
                values.put(f.getName(), (String) o);
            } else if (o.getClass() == Integer.class) {
                values.put(f.getName(), (Integer) o);
            } else if (o.getClass() == Float.class) {
                values.put(f.getName(), (Float) o);
            } else if (o.getClass() == Double.class) {
                values.put(f.getName(), (Double) o);
            }
        }

        return values;
    }

    public static ContentValues createContentValuesSemChave(TOBase t) throws Exception {

        ContentValues values = new ContentValues();

        List<Field> colunas = Helper.obterColunasSemChave(t);

        for (Field f : colunas) {

            Object o = runGetter(f, t);

            if (o == null) {
                values.putNull(f.getName());
            } else if (o.getClass() == String.class) {
                values.put(f.getName(), (String) o);
            } else if (o.getClass() == Integer.class) {
                values.put(f.getName(), (Integer) o);
            } else if (o.getClass() == Float.class) {
                values.put(f.getName(), (Float) o);
            } else if (o.getClass() == Double.class) {
                values.put(f.getName(), (Double) o);
            }
        }

        return values;
    }

    public static JSONObject createJSON(TOBase t) throws Exception {

        JSONObject j = new JSONObject();

        Class<?> c = t.getClass();

        DateTime d;
        String nomeJson;
        Object o;
        String v;
        for (Field f : c.getDeclaredFields()) {
            if (f.isAnnotationPresent(Column.class) && f.getAnnotation(Column.class).showJSON()) {

                if (f.getAnnotation(Column.class).dateTime()) {
                    d = new DateTime((Timestamp) runGetter(f, t));
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    j.put(nomeJson, d.toString(f.getAnnotation(Column.class).dateTimeFormat()));
                } else if (f.getType() == String.class) {
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    o = runGetter(f, t);
                    if (o != null) {
                        v = (String) o;
                        j.put(nomeJson, v.trim());
                    }
                } else {
                    nomeJson = f.getAnnotation(Column.class).jsonName();
                    j.put(nomeJson, runGetter(f, t));
                }

            }
        }

        return j;

    }

    public static Object runGetter(Field field, TOBase o) throws Exception {

        for (Method method : o.getClass().getMethods()) {

            String methodName = method.getName().toLowerCase();

            String methodTarget = "get" + field.getName().toLowerCase();

            if (methodTarget.equals(methodName)) {
                return method.invoke(o);
            }

        }

        return null;
    }

    public static Object runSetter(String name, TOBase o, Object v) throws Exception {

        for (Method method : o.getClass().getMethods()) {

            String methodName = method.getName().toLowerCase();

            String methodTarget = "set" + name.toLowerCase();

            if (methodTarget.equals(methodName)) {
                return method.invoke(o, v);
            }

        }

        return null;
    }

    public static String obterNomeTabela(TOBase t) {

        Class<?> obj = t.getClass();

        if (obj.isAnnotationPresent(Table.class)) {
            Annotation annotation = obj.getAnnotation(Table.class);
            Table tabela = (Table) annotation;
            return tabela.name();
        } else {
            return null;
        }

    }

    public static List<Field> obterColunas(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                l.add(field);
            }
        }

        return l;
    }

    public static String obterColunasString(TOBase t) throws Exception {

        StringBuilder ret = new StringBuilder();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class)) {
                if (ret.length() == 0) {
                    ret.append(field.getAnnotation(Column.class).name());
                } else {
                    ret.append(", ").append(field.getAnnotation(Column.class).name());
                }
            }
        }

        return ret.toString();
    }

    public static List<Field> obterColunasSemChave(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).type() == Column.TYPE.COLUMN) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasChave(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).type() == Column.TYPE.KEY) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasOrdenacaoPrincipal(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).mainOrder()) {
                l.add(field);
            }
        }

        return l;
    }

    public static List<Field> obterColunasBusca(TOBase t) throws Exception {

        List<Field> l = new ArrayList<>();

        Class<?> c = t.getClass();

        for (Field field : c.getDeclaredFields()) {
            if (field.isAnnotationPresent(Column.class) && field.getAnnotation(Column.class).search()) {
                l.add(field);
            }
        }

        return l;
    }

}
