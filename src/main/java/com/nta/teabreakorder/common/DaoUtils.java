package com.nta.teabreakorder.common;

import java.lang.reflect.Field;
import java.util.*;

public class DaoUtils {

    public static Map<String, String> getSearchDataFromParam(String searchData) {
        Map<String, String> searchMap = new HashMap<>();
        try {
            String[] searchArray = searchData.split(",");
            String[] tempData;
            for (int i = 0; i < searchArray.length; i++) {
                tempData = searchArray[i].split("=");
                if (tempData.length == 2) {
                    searchMap.put(tempData[0].trim(), tempData[1]);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return searchMap;
    }


    public static Map<String, String> getSortMapFormParam(String sortData) {
        Map<String, String> sortMap = new HashMap<>();
        try {
            String[] searchArray = sortData.split(",");
            String[] tempData;
            for (int i = 0; i < searchArray.length; i++) {
                tempData = searchArray[i].split(" ");
                if (tempData.length == 2) {
                    sortMap.put(tempData[0].trim(), tempData[1]);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return sortMap;
    }


    public static String covertSortDataToStringByAlias(String sortData, String alias) {
        StringBuilder sort = new StringBuilder();
        try {
            String[] searchArray = sortData.split(",");
            String[] tempData;
            for (int i = 0; i < searchArray.length; i++) {
                tempData = searchArray[i].split(" ");
                if (tempData.length == 2) {
                    sort.append(String.format("%s %s,", tempData[0].trim(), tempData[1]));
                }
            }
            return sort.toString().substring(0, sort.toString().length() - 1);
        } catch (Exception e) {
            return null;
        }
    }




    public static List<String> getFieldsFilter(String fields) {
        String[] fieldArray = fields.split(",");
        return Arrays.stream(fieldArray).toList();
    }

    public static Object filterField(List<String> fields, Object object) {
        try {
            Map<String, Object> target = new HashMap<>();
            Field[] totalFields = concatWithArrayCopy(object.getClass().getDeclaredFields(), object.getClass().getSuperclass().getDeclaredFields());
            for (Field field : totalFields) {
                field.setAccessible(true);
                if (fields.contains(field.getName())) {
                    target.put(field.getName(), field.get(object));
                }

            }
            return target;
        } catch (Exception e) {
            e.printStackTrace();
            return object;
        }

    }

    private static <T> T[] concatWithArrayCopy(T[] array1, T[] array2) {
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

}
