package com.udacity.stockhawk.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.udacity.stockhawk.R;

import java.util.Set;

import static com.udacity.stockhawk.data.PrefUtils.getStocks;

/**
 * Created by USER on 5/7/2017.
 */

public class InvalidQuoteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String errorMessage = context.getString(R.string.invalid_stock);
        if (QuoteSyncJob.ACTION_DATA_INVALID.equals(intent.getAction()))
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }



}
