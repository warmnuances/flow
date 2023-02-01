package under.hans.com.flow.Home;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.DataTruncation;
import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Adapters.BudgetPageAdapter;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Loaders.BudgetPageLoader;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.Utils.VerticalSpaceItemDecoration;
import under.hans.com.flow.models.BudgetCategory;

public class BudgetPageFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<BudgetCategory>>{

    private static final String TAG = "BudgetPageFragment";

    private boolean isLoader1Complete = false,isLoader2Complete = false;

    private static final int VERTICAL_ITEM_SPACE = 1;
    private final static int LOADER_ID  = 1000;
    private final static int AVERAGE_ID = 1001;
    private final static int ANALYSIS_ID = 1002;

    //Currency
    private TextView tvAvgCurrency, tvBudgetCurrency;
    private TextView tvAverage,tvBudget;
    private TextView tvAnalysis;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    //Variables
    private int projectedAmount,averageAmount,budget;
    String queryStartMillis;
    String queryEndMillis;

    private Context mContext = getActivity();
    private RecyclerView rvBudget;
    private BudgetPageAdapter mAdapter;


    private List<BudgetCategory> budgetDataset = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.budget_page_layout,container,false);

        queryStartMillis  = String.valueOf(DateTimeUtils.getThisMonthMillis());
        queryEndMillis = String.valueOf(DateTimeUtils.getNextMonthMillis());


        setupWidgets(view);

        if(getBudget() != 0){
            getLoaderManager().initLoader(LOADER_ID,null,this);
            getLoaderManager().initLoader(AVERAGE_ID,null,averageLoader);
            getLoaderManager().initLoader(ANALYSIS_ID,null,analysisLoader);
        }

        setHasOptionsMenu(true);
        setBudgetInit();


        return view;
    }

    public int getBudget(){
        int budget = 0  ;
        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
        Cursor mCursor = getActivity().getContentResolver().query(
                SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                null,
                null,
                null,
                null);

        if(mCursor!= null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
            budget = mCursor.getInt(budgetIndex);

            return budget;
        }

        return 0;
    }

    private void analysisInit(){

        if(isLoader1Complete == true && isLoader2Complete == true){
            if(projectedAmount > budget){
                tvAnalysis.setText("Over Budget");
                collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.redIndicator));

            }else if(projectedAmount < budget){
                int percentage = (int)(projectedAmount/budget)*100;
                tvAnalysis.setText(percentage + "%" + " of the Budget Used");
                collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.greenIndicator));
            }else {
                tvAnalysis.setText("Enter Records");
                collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.greenIndicator));
            }
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_budget,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void setupWidgets(View view){


        tvAvgCurrency = (TextView)view.findViewById(R.id.tvAverageCurrency);
        tvBudgetCurrency = (TextView)view.findViewById(R.id.tvBudgetCurrency);

        tvAverage = (TextView)view.findViewById(R.id.tvAverageDayAmount);
        tvBudget = (TextView)view.findViewById(R.id.tvBudget);
        tvAnalysis = (TextView)view.findViewById(R.id.tvAnalysis);
        rvBudget = (RecyclerView)view.findViewById(R.id.RecyclerView_Budget);

        mAdapter = new BudgetPageAdapter(getContext(),budgetDataset);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE);
        rvBudget.addItemDecoration(verticalSpaceItemDecoration);
        rvBudget.setHasFixedSize(true);
        rvBudget.setLayoutManager(mLayoutManager);

        collapsingToolbarLayout = (CollapsingToolbarLayout)view.findViewById(R.id.collapsingToolbar);


    }

    private void setBudgetInit(){
        String[] projection = {SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET};
        Cursor mCursor = getActivity().getContentResolver().query(
                SqlContractClass.UserSettingsClass.CONTENT_URI,
                projection,
                null,
                null,
                null,
                null);

        if(mCursor!= null && mCursor.moveToFirst()){
            int budgetIndex = mCursor.getColumnIndex(SqlContractClass.UserSettingsClass.COLUMN_USER_BUDGET);
            budget = mCursor.getInt(budgetIndex);
            tvBudget.setText(String.valueOf(budget));
        }

        mCursor.close();
    }

    @Override
    public Loader<List<BudgetCategory>> onCreateLoader(int i, Bundle bundle) {
        BudgetPageLoader budgetPageLoader = new BudgetPageLoader(getContext());
        budgetPageLoader.forceLoad();

        return budgetPageLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<BudgetCategory>> loader, List<BudgetCategory> budgetCategories) {
        budgetDataset = budgetCategories;
        mAdapter = new BudgetPageAdapter(getActivity(),budgetDataset);
        rvBudget.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<List<BudgetCategory>> loader) {
        rvBudget.setAdapter(null);
    }



    private LoaderManager.LoaderCallbacks<Cursor> analysisLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            String[] projection = {SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT,
                    SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL,
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END,
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START};

            String selection = SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START + ">= ? AND " +
                    SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END + "<= ?";

            String[] selectionArgs = {queryStartMillis,queryEndMillis};

            return new CursorLoader(getActivity(),
                    SqlContractClass.PresetClass.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);

        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

            int totalAmount = 0,amountMultiplier = 1;


            if(data.getCount() >= 0  ){
                for(data.moveToFirst();!data.isAfterLast();data.moveToNext()){

                    int amtIndex = data.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_AMOUNT);
                    int timeSecStartIndex = data.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_START);
                    int timeSecEndIndex = data.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_TIMESEC_END);
                    int intervalIndex = data.getColumnIndex(SqlContractClass.PresetClass.COLUMN_PRESET_INTERVAL);


                    int amount = data.getInt(amtIndex);
                    int timeStart = data.getInt(timeSecStartIndex);
                    int timeEnd = data.getInt(timeSecEndIndex);
                    int interval = data.getInt(intervalIndex);

                    if(timeEnd == 0){
                        timeEnd = (DateTimeUtils.getNextMonthMillis())-1;
                    }

                    amountMultiplier = (timeEnd - timeStart)/interval;
                    totalAmount = totalAmount +(amountMultiplier* amount);


                }
                projectedAmount = projectedAmount + totalAmount;

                isLoader1Complete = true;
                analysisInit();
                }
            }


        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            tvAnalysis.setText("");
            isLoader1Complete = false;
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> averageLoader = new LoaderManager.LoaderCallbacks<Cursor>() {
        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

            String[] projection = {SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT};
            String selection = SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_TIMESEC + " BETWEEN ? AND ?" ;
            String[] selectionArgs = {queryStartMillis,queryEndMillis};

            return new CursorLoader(getActivity(),
                    SqlContractClass.SpendingsEntryClass.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

            int totalAmount = 0;
            for(data.moveToFirst();!data.isAfterLast();data.moveToNext()){
                int amtIndex = data.getColumnIndex(SqlContractClass.SpendingsEntryClass.COLUMN_SPENDINGS_AMOUNT);
                int amount = data.getInt(amtIndex);
                totalAmount = amount + totalAmount;

            }
            projectedAmount = projectedAmount + totalAmount;
            averageAmount = totalAmount/ DateTimeUtils.getDaysInThisMonth();

            tvAverage.setText(FormatAlgorithms.getFormattedFunds(averageAmount));
            isLoader2Complete = true;
            analysisInit();
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            tvAverage.setText("");
            isLoader2Complete = false;
        }
    };

    public void onBudgetSet(String text){
        int budgetSet;
        tvBudget.setText(text);
        budgetSet = Integer.parseInt(text);

        DatabaseUtils.updateUserBudget(getContext(),budgetSet);
    }
}
