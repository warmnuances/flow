package under.hans.com.flow.Home;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Adapters.TimelineRecyclerAdapter;
import under.hans.com.flow.Forms.AddItemsForms;
import under.hans.com.flow.Loaders.DashBoardLoader;
import under.hans.com.flow.R;
import under.hans.com.flow.Search.SearchActivity;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.models.Transaction;

import under.hans.com.flow.Data.SqlContractClass.UserSettingsClass;

/**
 * Created by Hans on 3/2/2018.
 */

public class DashBoardFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Transaction>> {



    private static final String TAG = "DashBoardFragment";

    private static final int LOADER_ID = 51;
    private TextView tvBalance;
    private TimelineRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FloatingActionButton addFab;
    private SwipeRefreshLayout mRefreshLayout;
    private ImageButton btShowMore;

    private List<Transaction> transactionDataset = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dashboard_layout, container,false);

        setHasOptionsMenu(true);
        setupWidgets(view);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            RefreshDashBoardData();
            return true;
        }else if(id == R.id.action_search){
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupWidgets(View view){
        tvBalance = (TextView)view.findViewById(R.id.tvBalance);

        addFab = (FloatingActionButton)view.findViewById(R.id.addFab);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.RecyclerViewTimeLine);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TimelineRecyclerAdapter(getActivity(),transactionDataset);

        mRecyclerView.setAdapter(mAdapter);

        getLoaderManager().initLoader(LOADER_ID, null,this);

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addItemsIntent = new Intent(getActivity(), AddItemsForms.class);
                startActivity(addItemsIntent);
            }
        });


        mRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshDashBoardData();
            }
        });

        btShowMore = (ImageButton)view.findViewById(R.id.btShowMore);
        btShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });



    }

    public void RefreshDashBoardData(){
        getLoaderManager().restartLoader(LOADER_ID,null,DashBoardFragment.this);
        mAdapter.notifyDataSetChanged();
    }

    /** ----------------------------- Level 2 Loader-------------------------------------------**/
    @Override
    public Loader<List<Transaction>> onCreateLoader(int i, Bundle bundle) {
        transactionDataset.clear();

        DashBoardLoader dashLoader = new DashBoardLoader(getActivity(),transactionDataset,"");
        dashLoader.forceLoad();

        return dashLoader;
    }

    @Override
    public void onLoadFinished(Loader<List<Transaction>> loader, List<Transaction> transactions) {
        transactionDataset.addAll(transactions);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
        loadToModels(tvBalance);
    }

    @Override
    public void onLoaderReset(Loader<List<Transaction>> loader) {
        transactionDataset.clear();
        mRecyclerView.setAdapter(null);
    }


    /** ----------------------------- Level 1 Init-------------------------------------------**/

    public void loadToModels(TextView tvFunds){
        String []projection ={UserSettingsClass.COLUMN_USER_BALANCE} ;
        String selection = UserSettingsClass._ID +  "=?";
        String[] selectionArgs ={"1"} ;

        Cursor cursor = getActivity().getContentResolver().query(UserSettingsClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null,
                null);
        if(cursor != null && cursor.moveToFirst()){
            int funds = cursor.getInt(cursor.getColumnIndex(UserSettingsClass.COLUMN_USER_BALANCE));
            tvFunds.setText(FormatAlgorithms.getFormattedFunds(funds));
        }
        cursor.close();
    }

    /** ----------------------------- Fragment States -------------------------------------------**/

    @Override
    public void onResume() {
        super.onResume();
        loadToModels(tvBalance);
        getLoaderManager().restartLoader(LOADER_ID,null,this);
        mAdapter.notifyDataSetChanged();
    }


}
