package com.example.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.widget.RemoteViews;
import java.util.Iterator;

import static com.example.android.bakingapp.RecipeListActivity.recipeResults;



/**
 * Implementation of App Widget functionality.
 */


public class RecipeWidgetProvider extends AppWidgetProvider {


    public   static String[] mIngredientName;
    private static Recipe.Ingredient[] mIngredients;


    public  static String mRecipeName;


    private static Recipe mRecipe;

    //mCurrentRecipe is set in "onEnabled"
    public static int mCurrentRecipe;
    public static int mNumberOfRecipes;
    public static int mAppWidgetId;

    private static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        mAppWidgetId = appWidgetId;
        getRecipeNameAndIngredientsList();

        // create the intent to get the next recipe in the list, after clicking the 'Next' button
        Intent intentForButton = new Intent(context, RecipeWidgetRefreshReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intentForButton, 0);

        // Set up the intent that starts the RecipeWidgetService, which will
        // provide the views for this collection.
        Intent intent = new Intent(context, RecipeWidgetService.class);

        // Construct the RemoteViews object
        Resources resources = context.getResources();
        RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.recipe_app_widget_layout);
        view.setTextViewText(R.id.appwidget_id,String.valueOf(appWidgetId));
        view.setTextViewText(R.id.appwidget_recipe_name, resources.getString(R.string.appwidget_Recipe_name) + " - " + mRecipeName);

        //set the remote adapter using the listView Id
        view.setRemoteAdapter( R.id.appwidget_ingredient,intent);

        //attach the onclick listener to the button
        view.setOnClickPendingIntent(R.id.button,pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, view);
    }

    @Override
    public void onReceive(Context context, Intent intent){
        System.out.println("RecipeWidgetProvider-OnReceive");
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            System.out.println("RecipeWidgetProvider-OnUpdate");
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context,appWidgetManager,appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        mCurrentRecipe = 2;
        mNumberOfRecipes = recipeResults.size();

        System.out.println("RecipeWidgetProvider-OnEnabled");

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        System.out.println("RecipeWidgetProvider-OnDisabled");
    }

    public static void pushWidgetUpdate(Context context){
        System.out.println("pushWidgetUpdate "+ Integer.toString(mCurrentRecipe));
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.notifyAppWidgetViewDataChanged(mAppWidgetId,R.id.appwidget_ingredient);
    }

    public static void getRecipeNameAndIngredientsList()
    {
        int mIndex = 0;
        Iterator<Recipe> mIteratorRecipe;

        System.out.println("getRecipeIngredients");

        // iterate through the arrayList, copying the recipe name to a string[] array.
        mIteratorRecipe = recipeResults.iterator();
        while (mIteratorRecipe.hasNext()) {
            mIteratorRecipe.next();
            if (mIndex == mCurrentRecipe) {
                mRecipe = recipeResults.get(mIndex);
                mRecipeName = mRecipe.getName();
                mIngredientName = new String[mRecipe.ingredients.length];
                mIngredients = new Recipe.Ingredient[mRecipe.ingredients.length];
            }
            mIndex++;
        }

        // iterate through the ingredient array,
        for(mIndex = 0; mIndex < mIngredients.length; mIndex++) {
            mIngredients[mIndex] = mRecipe.ingredients[mIndex];
            if(mIngredients[mIndex].ingredient == null)
                mIngredientName[mIndex] = "ingredient " + Integer.toString(mIndex);
            else {
                mIngredientName[mIndex] = " " + Integer.toString(mIndex + 1) + ". " + mIngredients[mIndex].ingredient;
                System.out.println("getRecipeIngredients: " + mIngredientName[mIndex]);
            }
        }
    }
}

