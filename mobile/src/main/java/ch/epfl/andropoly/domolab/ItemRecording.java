package ch.epfl.andropoly.domolab;

import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class ItemRecording {
    //private static List<String> list_rooms = new ArrayList<String>();
    private static String[][] list_rooms = {
            {"Kitchen", "My Kitchen"},
            {"Bedroom", "My Bedroom"},
            {"Kitchen","Caca"},
            {"Living room","My boy"},
            {"Kitchen","Prout"},
            {"Restroom","Hey"},
            {"New","New"},
            {"", ""}
        };

    public static void displayRoom(){
        for(int i =0; i<list_rooms.length; i++)
            Log.e(TAG, "Type " + list_rooms[i][0] + " Name " + list_rooms[i][1]);
    }

    private static List<String> list_fav = Arrays.asList(
            "Kitchen","Restroom","Restroom","Restroom","New"
    );

    public static String[][] getList_rooms(){
        return list_rooms;
    }

    public static void remList_rooms(String name){
        //list_rooms.remove(item);
    }

    public static void addList_rooms(String type, String name){
        int length = list_rooms.length;
        list_rooms[length - 2][0] = type;
        list_rooms[length - 2][1] = name;

        list_rooms[length - 1][0] = "New";
        list_rooms[length - 1][1] = "New";
    }

    public static List<String> getList_favorites(){
        return list_fav;
    }

    public static void remList_favorites(String item){
        list_fav.remove(item);
    }

    public static void addList_favorites(String item){
        list_fav.add(list_fav.size()-1, item);
    }
}
