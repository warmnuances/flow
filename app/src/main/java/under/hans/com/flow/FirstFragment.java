package under.hans.com.flow;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Loaders.DayLineChartLoader;
import under.hans.com.flow.Utils.ChartValueFormatter;
import under.hans.com.flow.Utils.FormatAlgorithms;

/**
 * Created by Hans on 29/04/2018.
 */

public class FirstFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Entry>> {


    private static final String TAG = "TrendsFragment";
    List<Entry> dataEntries = new ArrayList<>();

    private final static int LINECHART_ID = 121;

    private LineChart monthLineChart;

//    private TextView tvSpent,tvSaved,tvPageTitle,tvIndex;
    private Context mContext;



    private Bundle mBundle;
    private int argsPosition;

    public static FirstFragment initTrendFrag(int position){
        FirstFragment lastFragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("position",position);
        lastFragment.setArguments(args);
        return lastFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mBundle = getArguments();
        argsPosition = mBundle.getInt("position");
        Log.d(TAG, "onCreateView: Position = " + argsPosition);

        View view = inflater.inflate(R.layout.trends_layout,container,false);
        setupWidgets(view);




        getLoaderManager().initLoader(LINECHART_ID,mBundle,this);
        monthLineChart.invalidate();
//        tvIndex.setText("LastFragment 2");
//        tvPageTitle.setText(String.valueOf(argsPosition));

        return view;

    }

    private void setupWidgets(View view){

//        tvIndex = (TextView)view.findViewById(R.id.tvIndex);
//        tvPageTitle = (TextView)view.findViewById(R.id.pageTitle);
//        tvSpent = (TextView)view.findViewById(R.id.tvSpent);
//        tvSaved = (TextView)view.findViewById(R.id.tvSpent);
        monthLineChart = (LineChart)view.findViewById(R.id.monthLineChart);
        chartInit();
//        setTextViewSaved();

    }
    private void chartInit(){

        //monthLineChart.setDoubleTapToZoomEnabled(false);
        monthLineChart.getAxisLeft().setAxisMinimum(0f);
        monthLineChart.getAxisLeft().setYOffset(2f);
        monthLineChart.getAxisLeft().setGranularity(1f);
        monthLineChart.getAxisLeft().setLabelCount(5,true);
        monthLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        monthLineChart.getAxisRight().setDrawLabels(false);
        monthLineChart.getXAxis().setDrawGridLines(false);
        monthLineChart.getAxisLeft().setDrawGridLines(false);
        monthLineChart.getAxisRight().setDrawGridLines(false);
        monthLineChart.getXAxis().setGranularity(1f);
        monthLineChart.getAxisRight().setDrawAxisLine(false);
        monthLineChart.getLegend().setEnabled(false);
        monthLineChart.getDescription().setEnabled(false);
        monthLineChart.setPinchZoom(false);
        monthLineChart.setDrawGridBackground(false);
        monthLineChart.notifyDataSetChanged();
        monthLineChart.getXAxis().setAxisMinimum(1f);

    }

    private void chartDataSetInit(List<Entry> dataEntries){

        Drawable drawable = ContextCompat.getDrawable(getActivity(), R.drawable.line_gradient);
        int colorDark = ContextCompat.getColor(getContext(), R.color.colorAccentSecondary);


        if(dataEntries.isEmpty()){
            monthLineChart.setNoDataText("No Records Found");

        }else{


            LineDataSet lineDataSet = new LineDataSet(dataEntries,"Data Set");
            lineDataSet.setColor(colorDark);
            lineDataSet.setLineWidth(3f);
            lineDataSet.setFillDrawable(drawable);
            lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            lineDataSet.setDrawFilled(true);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setDrawValues(false);
            lineDataSet.setValueTextSize(11f);
            lineDataSet.setValueFormatter(new ChartValueFormatter());
            lineDataSet.setDrawHighlightIndicators(false);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(lineDataSet);

            LineData data = new LineData(dataSets);

            monthLineChart.setData(data);
            monthLineChart.notifyDataSetChanged();
            monthLineChart.invalidate();
        }

    }

    @Override
    public Loader<List<Entry>> onCreateLoader(int id, Bundle args) {

        if(args!=null){
            int index = args.getInt("position") - 3000;

            String strDate = convertIndexToDate(index);

            DayLineChartLoader lineChartLoader = new DayLineChartLoader(getContext(),strDate);
            lineChartLoader.forceLoad();

            return lineChartLoader;
        }else{
            Toast.makeText(getActivity(), "Something Went Wrong!", Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
        chartDataSetInit(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
//        tvSpent.setText("");
        monthLineChart.clear();
    }


    public static int getCurrentMonthIndex(){
        Calendar calendar= Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        return month;

    }

    public static String convertIndexToDate(int pos){

        int index = pos+getCurrentMonthIndex();
        Calendar calendar = Calendar.getInstance();
        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");
        calendar.set(Calendar.MONTH,index);
        calendar.set(Calendar.DAY_OF_MONTH,0);
        String strDate = df.format(calendar.getTime());

        return strDate;

    }
}
