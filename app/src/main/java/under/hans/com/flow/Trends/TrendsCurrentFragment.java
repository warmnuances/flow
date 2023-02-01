//package under.hans.com.releasecandidate.Trends;
//
//import android.support.v4.app.LoaderManager;
//import android.support.v4.content.Loader;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
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
// * Created by Hans on 4/24/2018.
// */
//
//public class TrendsCurrentFragment extends Fragment
//        implements LoaderManager.LoaderCallbacks<List<Entry>>{
//
//    private static final String TAG = "TrendsFragment";
//    List<Entry> dataEntries = new ArrayList<>();
//
//    private final static int LINECHART_ID = 122;
//
//    private LineChart monthLineChart;
//
//    private TextView tvSpent,tvSaved,tvPageTitle,tvIndex;
//
//
//
//    public static TrendsCurrentFragment initTrendFrag(int position){
//        TrendsCurrentFragment trendFragment = new TrendsCurrentFragment();
//        Bundle args = new Bundle();
//        args.putInt("position",position);
//        trendFragment.setArguments(args);
//        return trendFragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        Log.d(TAG, "onCreateView: CurrentFrag");
//        View view = inflater.inflate(R.layout.trends_layout,container,false);
//        setupWidgets(view);
//
//        getLoaderManager().initLoader(LINECHART_ID,null,this);
//        monthLineChart.invalidate();
//
//        tvIndex.setText("Curr Fragment 1");
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
//    @Override
//    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {
//
//        String strDate = "01/04/2018";
//
//        DayLineChartLoader lineChartLoader = new DayLineChartLoader(getContext(),strDate);
//        lineChartLoader.forceLoad();
//
//        return lineChartLoader;
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
//
//
////    @Override
////    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
////
////        String startMonthInterval,endMonthInterval;
////
////        startMonthInterval = String.valueOf(DateTimeUtils.getLastMonthMillis());
////        endMonthInterval= String.valueOf(DateTimeUtils.getThisMonthMillis());
////
////
////        Log.d(TAG, "onCreateLoader: last Month Millis = " + startMonthInterval);
////        Log.d(TAG, "onCreateLoader: This Month Millis = " + endMonthInterval);
////        String[] projection = {SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT,
////                SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC};
////        String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?"  ;
////        String[] selectionArgs = {startMonthInterval,endMonthInterval};
////
////
////        return new CursorLoader(getActivity(),
////                SqlContractClass.SpendingsEntryClass.CONTENT_URI,
////                projection,
////                selection,
////                selectionArgs,
////                null);
////    }
////
////    @Override
////    public void onLoadFinished(Loader<Cursor> loader, Cursor mCursor) {
////        Entry entryMonth;
////        int count = 0;
////        int total = 0;
////
////        if(mCursor !=null){
////            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
////
////                int amountId = mCursor.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
////                int amount = mCursor.getInt(amountId);
////
////                entryMonth = new Entry(count, FormatAlgorithms.getFloatfromInt(amount));
////                dataEntries.add(entryMonth);
////                count = count + 1;
////                total = total+ amount;
////
////            }
////
////            chartDataSetInit(dataEntries);
////            tvSpent.setText(FormatAlgorithms.getFormattedFunds(total));
////        }
////    }
////
////    @Override
////    public void onLoaderReset(Loader<Cursor> loader) {
////        tvSpent.setText("");
////        monthLineChart.clear();
////    }
////
//    private void chartDataSetInit(List<Entry> dataEntries){
//
//        if(dataEntries.isEmpty()){
//            dataEntries.add(new Entry(1,1));
//
//        }else{
//            LineDataSet lineDataSet = new LineDataSet(dataEntries,"Data Set");
//
//            lineDataSet.setFillAlpha(110);
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
//}
