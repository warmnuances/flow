//package under.hans.com.releasecandidate.Trends;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.support.v4.app.Fragment;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import under.hans.com.releasecandidate.Data.SqlContractClass;
//import under.hans.com.releasecandidate.Loaders.DayLineChartLoader;
//import under.hans.com.releasecandidate.R;
//import under.hans.com.releasecandidate.Utils.FormatAlgorithms;
//
///**
// * Created by Hans on 30/04/2018.
// */
//
//public class TrendsLastFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<List<Entry>> {
//
//
//    private static final String TAG = "TrendsFragment";
//    List<Entry> dataEntries = new ArrayList<>();
//
//    private final static int LINECHART_ID = 121;
//
//    private LineChart monthLineChart;
//
//    private TextView tvSpent,tvSaved,tvPageTitle,tvIndex;
//    private Context mContext;
//
//
//    public static TrendsLastFragment initTrendFrag(int position){
//        TrendsLastFragment lastFragment = new TrendsLastFragment();
//        Bundle args = new Bundle();
//        args.putInt("position",position);
//        lastFragment.setArguments(args);
//        return lastFragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        Log.d(TAG, "onCreateView: LastFrag");
//
//        View view = inflater.inflate(R.layout.trends_layout,container,false);
//        setupWidgets(view);
//
//        getLoaderManager().initLoader(LINECHART_ID,null,this);
//        monthLineChart.invalidate();
//        tvIndex.setText("LastFragment 2");
//
//        return view;
//
//    }
//
//    private void setupWidgets(View view){
//
//        tvIndex = (TextView)view.findViewById(R.id.tvIndex);
//        tvPageTitle = (TextView)view.findViewById(R.id.pageTitle);
//        tvSpent = (TextView)view.findViewById(R.id.tvSpent);
//        tvSaved = (TextView)view.findViewById(R.id.tvSpent);
//        monthLineChart = (LineChart)view.findViewById(R.id.monthLineChart);
//        chartInit();
//        setTextViewSaved();
//
//    }
//    private void chartInit(){
//
//
//        monthLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        monthLineChart.getAxisRight().setDrawLabels(false);
//        monthLineChart.getXAxis().setDrawGridLines(false);
//        monthLineChart.getAxisLeft().setDrawGridLines(false);
//        monthLineChart.getAxisRight().setDrawGridLines(false);
//        monthLineChart.getAxisRight().setDrawAxisLine(false);
//        monthLineChart.getLegend().setEnabled(false);
//        monthLineChart.getDescription().setEnabled(false);
//        monthLineChart.setPinchZoom(false);
//        monthLineChart.setDrawGridBackground(false);
//        monthLineChart.notifyDataSetChanged();
//
//    }
//    private void setTextViewSaved(){
//        Cursor mCursor;
//        int budget = 0;
//        int spendings = FormatAlgorithms.setFormattedFunds(tvSpent.getText().toString());
//
//        Log.d(TAG, "setTextViewSaved: Budget = " + budget);
//        Log.d(TAG, "setTextViewSaved: Spendings = " + spendings);
//
//        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
//        String selection= SqlContractClass.UserSettingsClass._ID + "=?";
//        String[] selectionArgs = {"1"};
//
//        mCursor = getContext().getContentResolver().query(SqlContractClass.UserSettingsClass.CONTENT_URI,
//                projection,
//                selection,
//                selectionArgs,
//                null);
//
//        if(mCursor != null && mCursor.moveToFirst()){
//            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
//            budget = mCursor.getInt(budgetIndex);
//        }
//
//        budget = budget - spendings;
//        tvSaved.setText(FormatAlgorithms.getFormattedFunds(budget));
//
//    }
//
//    private void chartDataSetInit(List<Entry> dataEntries){
//
//        if(dataEntries.isEmpty()){
//           monthLineChart.setNoDataText("Please enter records");
//
//        }else{
//            LineDataSet lineDataSet = new LineDataSet(dataEntries,"Data Set");
//            lineDataSet.setColor(Color.RED);
//
//            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
//            dataSets.add(lineDataSet);
//
//            LineData data = new LineData(dataSets);
//
//            monthLineChart.setData(data);
//            monthLineChart.notifyDataSetChanged();
//            monthLineChart.invalidate();
//        }
//
//    }
//
//    @Override
//    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
//
//        String strDate = "1/05/2018";
//
//        DayLineChartLoader lineChartLoader = new DayLineChartLoader(getContext(),strDate);
//        lineChartLoader.forceLoad();
//
//        return lineChartLoader;
//
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
//        chartDataSetInit(data);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<Entry>> loader) {
//        tvSpent.setText("");
//        monthLineChart.clear();
//    }
//}
