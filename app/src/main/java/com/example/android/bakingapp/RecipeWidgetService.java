package com.example.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dneum on 3/27/2018.
 */
public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext());
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private static List<String> recipeAdapter = new ArrayList<>();
    private static int mRecipeAdapterSize;

    public RecipeRemoteViewsFactory(Context context) {
        System.out.println("RecipeWidgetService-Constructor");
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        int mSizeOf;
        String mRecipeName;
        String[] mIngredientItems;

        System.out.println("RecipeWidgetService-onDataSetChanged");
        recipeAdapter.clear();
        mIngredientItems = RecipeWidgetProvider.mIngredientName;

        mSizeOf = mIngredientItems.length;
        for(int i = 0; i < mSizeOf; i++) {
            mRecipeName = mIngredientItems[i];
            recipeAdapter.add(mRecipeName);
        }
        mRecipeAdapterSize = recipeAdapter.size();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        System.out.println("RecipeWidgetService-getCount="+Integer.toString(mRecipeAdapterSize));
        return mRecipeAdapterSize;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        System.out.println("getViewAt-getCount="+Integer.toString(mRecipeAdapterSize));
        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.item_list_of_ingredients);
        if(position < mRecipeAdapterSize ) {
            view.setTextViewText(R.id.ingredients_textView, recipeAdapter.get(position));
            System.out.println("getViewAt=" + Integer.toString(position) + " " + recipeAdapter.get(position));
        }
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        System.out.println("RecipeWidgetService-getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
