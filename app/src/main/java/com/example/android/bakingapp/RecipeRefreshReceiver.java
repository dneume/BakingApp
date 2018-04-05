package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;

import static com.example.android.bakingapp.RecipeWidgetProvider.mAppWidgetId;
import static com.example.android.bakingapp.RecipeWidgetProvider.mRecipeName;

/**
 * Created by dneum on 4/4/2018.
 */

public class RecipeRefreshReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("RecipeRefreshReceiver-OnReceive");
        getNextRecipe(context);
    }

    private void getNextRecipe(Context context){

        //increment the index for the recipeResults
        RecipeWidgetProvider.mCurrentRecipe++;
        if(RecipeWidgetProvider.mCurrentRecipe == RecipeWidgetProvider.mNumberOfRecipes)
            RecipeWidgetProvider.mCurrentRecipe = 0;

        //get the next recipeName and list of ingredients
        RecipeWidgetProvider.getRecipeNameAndIngredientsList();

        // create the intent to get the next recipe in the list, after clicking the 'Next' button
        Intent intentForButton = new Intent(context, RecipeRefreshReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentForButton, 0);

        // Construct the RemoteViews object
        Resources resources = context.getResources();
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget_layout);
        view.setTextViewText(R.id.appwidget_id,String.valueOf(mAppWidgetId));
        view.setTextViewText(R.id.appwidget_recipe_name, resources.getString(R.string.appwidget_Recipe_name) + " - " + mRecipeName);

        //attach the onclick listener to the button
        view.setOnClickPendingIntent(R.id.button,pendingIntent);

        // Instruct the widget manager to update the widget for the recipe name and widgetId
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(mAppWidgetId, view);

        // Instruct the widget manager to update the ListView
        RecipeWidgetProvider.pushWidgetUpdate(context);
    }
}
