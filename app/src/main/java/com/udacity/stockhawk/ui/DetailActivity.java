package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int _ID = 0;
    private static final int SYMBOL = 1;
    private static final int DATE = 2;
    private static final int COUNTER =3;
    private static final int CLOSE = 4;

    LineChart chart ;
    private static final int TASK_LOADER_ID = 25;
    private  final String LOG_TAG = DetailActivity.class.getName() ;
    public static final String STOCK_SYMBOL = "symbol";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        chart = (LineChart) findViewById(R.id.chart);
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null, this);
    }
    /**
     * Instantiates and returns a new AsyncTaskLoader with the given ID.
     * This loader will return task data as a Cursor or null if an error occurs.
     *
     * Implements the required callbacks to take care of loading data at all stages of loading.
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {

        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mTaskData = null;

            // Initialize a Cursor, this will hold all the task data
            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data

                // COMPLETED (5) Query and load all task data in the background; sort by priority
                // [Hint] use a try/catch block to catch any errors in loading data
                Intent intent = getIntent();
                Bundle extras = intent.getExtras();
                String symbol  = extras.getString(STOCK_SYMBOL);
                String selection = Contract.QuoteHistory.COLUMN_SYMBOL + " =? ";
                String[] selectionArgs = new String[]{symbol};
                String sortOrder = Contract.QuoteHistory.COLUMN_DATE + " ASC";

                try {
                    return getContentResolver().query(Contract.QuoteHistory.URI,
                            null,
                            selection,
                            selectionArgs,
                           sortOrder);

                } catch (Exception e) {
                   // Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        List<Entry> entries = new ArrayList<Entry>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd");
        int numberOfValues = cursor.getCount();
        final String[] valuesX  = new String[numberOfValues];
        float[] valuesY = new float[numberOfValues];

        int counter =0;
        while (cursor.moveToNext()) {
            String date = dateFormat.format(cursor.getInt(DATE));
            valuesX[counter] = date;
            valuesY[counter] = cursor.getFloat(CLOSE);
            counter++;
        }

        for (int i=0 ; i< valuesX.length ; i++) {
            entries.add(new Entry(i, valuesY[i]));
        }
        IAxisValueFormatter formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                // This will return the formatted date for the corresponding index
                return valuesX[(int) value];
            }
        };
        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(formatter);
        LineDataSet dataSet = new LineDataSet(entries, "Stock Graph"); // add entries to dataset
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.GREEN); // styling, ...
       chart.setBackgroundColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate(); // refresh
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
