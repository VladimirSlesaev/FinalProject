package com.androidjson.serverupdate_androidjsoncom;

/**
 *
 * Created by Vladimir Slesarev
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SharedPreferencesClass {
    SharedPreferences shrd, favoriteShrd;
    Context mCtx;
    ArrayList<String> favoriteList;

    SharedPreferences.Editor editor, editorFavorite;

    public SharedPreferencesClass(Context mCtx) {
        this.mCtx = mCtx;
        shrd = mCtx.getSharedPreferences("save_info", Context.MODE_PRIVATE);

        editor = shrd.edit();
        favoriteShrd = mCtx.getSharedPreferences("getFavorite", Context.MODE_PRIVATE);
        editorFavorite = favoriteShrd.edit();
        favoriteList = new ArrayList<>();
//
//        try {////get all favorite from sharedPreferences
//          //  loadOrderedCollection("myFavoriteJson");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    public int getHowMuchChoosing() {
        return shrd.getInt("HowMuchChoosing", 0);
    }

    public void setHowMuchChoosing(int howMuchChoosing) {
        editor.putInt("HowMuchChoosing", howMuchChoosing);
        editor.apply();
        editor.commit();
    }


    public String getMyStreet() {
        String check = shrd.getString("myStreet", "emptyValue");
        return check;
    }
    public void setMyStreet(String Street) {
        editor.putString("myStreet", Street);
        editor.apply();
        editor.commit();
    }




    public String getNotification() {
        String check = shrd.getString("notifi", "emptyValue");
        return check;
    }
    public void setNotification(String Street) {
        editor.putString("notifi", Street);
        editor.apply();
        editor.commit();
    }
    public String getMyApartment() {
        String check = shrd.getString("myApartment", "emptyValue");
        return check;
    }
    public void setMyApartment(String Apartment) {
        editor.putString("myApartment", Apartment);
        editor.apply();
        editor.commit();
    }

    public boolean isImServiceProvider() {
        String check = shrd.getString("isServiceProvider", "emptyValue");
        Log.d("checkIsImSevPRo", check);
        if (check.equals("true"))
            return true;
        else
            return false;
    }
    public void setIsImServiceProvider() {
        String check = shrd.getString("isServiceProvider", "emptyValue");
        editor.putString("isServiceProvider","true");

    }
    public void setIsImServiceProviderFalse() {
        String check = shrd.getString("isServiceProvider", "emptyValue");
        editor.putString("isServiceProvider","false");

    }

    public void setImServiceProviderTrue() {
        editor.putString("isServiceProvider","true");
        editor.apply();
        editor.commit();
    }


    public String getMyEmail() {
        String check = shrd.getString("user_email", "emptyValue");
        return check;
    }

    public void setMyEmail(String email) {
        editor.putString("user_email", email);
        editor.apply();
        editor.commit();
    }


}
