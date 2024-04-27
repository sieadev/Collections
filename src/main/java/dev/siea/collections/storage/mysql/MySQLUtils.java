package dev.siea.collections.storage.mysql;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MySQLUtils {
    public static List<Integer> stringToIntList(String str) {
        List<Integer> list = new ArrayList<Integer>();
        String[] strs = str.split(",");
        for (String s : strs) {
            list.add(Integer.parseInt(s));
        }
        return list;
    }

    public static String intListToString(List<Integer> list) {
        StringBuilder sb = new StringBuilder();
        for (Integer i : list) {
            sb.append(i).append(",");
        }
        return sb.toString();
    }

    public static Object stringToObject(String str) {
        Object targetObject;
        try{
            targetObject = EntityType.valueOf(str);
        } catch (IllegalArgumentException e){
            targetObject = Material.valueOf(str);
        }
        return targetObject;
    }

    public static String commandsListToString(List<List<String>> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (List<String> sublist : list) {
            for (String str : sublist) {
                stringBuilder.append(str).append(",");
            }
            stringBuilder.append(";");
        }
        return stringBuilder.toString();
    }

    // Method to convert string back to List<List<String>>
    public static List<List<String>> commandsStringToList(String str) {
        List<List<String>> result = new ArrayList<>();
        String[] sublists = str.split(";");
        for (String sublist : sublists) {
            List<String> subResult = new ArrayList<>();
            String[] elements = sublist.split(",");
            for (String element : elements) {
                subResult.add(element);
            }
            result.add(subResult);
        }
        return result;
    }
}
