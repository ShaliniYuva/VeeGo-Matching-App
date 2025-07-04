package com.example.foodordering.Helper;



import android.content.Context;
import android.content.SharedPreferences;

import com.example.foodordering.Domain.Foods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ManagementFav {
    private static final String PREF_NAME = "favourite_prefs";
    private static final String KEY_FAVOURITES = "favourites";
    private SharedPreferences sharedPreferences;
    private Gson gson = new Gson();

    public ManagementFav(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void addFood(Foods food) {
        List<Foods> favourites = getFavourites();
        favourites.add(food);
        saveFavourites(favourites);
    }

    public void removeFood(Foods food) {
        List<Foods> favourites = getFavourites();
        favourites.removeIf(item -> item.getTitle().equals(food.getTitle()));
        saveFavourites(favourites);
    }

    public boolean isFavourite(Foods food) {
        List<Foods> favourites = getFavourites();
        for (Foods fav : favourites) {
            if (fav.getTitle().equals(food.getTitle())) {
                return true;
            }
        }
        return false;
    }

    public List<Foods> getFavourites() {
        String json = sharedPreferences.getString(KEY_FAVOURITES, null);
        Type type = new TypeToken<ArrayList<Foods>>() {}.getType();
        return json == null ? new ArrayList<>() : gson.fromJson(json, type);
    }

    private void saveFavourites(List<Foods> favourites) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_FAVOURITES, gson.toJson(favourites));
        editor.apply();
    }
}

