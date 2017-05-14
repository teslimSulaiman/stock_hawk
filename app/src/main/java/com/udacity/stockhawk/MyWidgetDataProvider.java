package com.udacity.stockhawk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.data.Contract;
import com.udacity.stockhawk.ui.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 5/11/2017.
 */

public class MyWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Cursor mCursor = null;
    private Intent intent;

    @Override
    public void onCreate() {

    }
    private void initialData(){

        if (mCursor !=null) mCursor.close();
        mCursor = context.getContentResolver().query(Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onDataSetChanged() {
        initialData();
    }

    public MyWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
    }

    @Override
    public void onDestroy() {

        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mCursor == null || mCursor.getCount() == 0) return null;
        mCursor.moveToPosition(position);

        int priceIndex = mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE);
        int symbolIndex = mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
        Float price = mCursor.getFloat(priceIndex);
        String symbol = mCursor.getString(symbolIndex);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        remoteViews.setTextViewText(R.id.symbolId, symbol);
        remoteViews.setTextViewText(R.id.priceId,"$"+price);

        Bundle extras = new Bundle();
        extras.putString(DetailActivity.STOCK_SYMBOL, symbol);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.symbolId, fillInIntent);
        remoteViews.setOnClickFillInIntent(R.id.priceId, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
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

