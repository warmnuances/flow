package under.hans.com.flow.Search;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Adapters.SearchAdapter;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Loaders.DashBoardLoader;
import under.hans.com.flow.R;
import under.hans.com.flow.models.Transaction;

public class SearchActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Transaction>>{

    private static final String TAG = "SearchActivity";

    private static final int LOADER_ID = 121;


    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private SearchAdapter mSearchAdapter;
    private Bundle mBundle;

    private List<Transaction> transactionDataset = new ArrayList<>();
    private SwipeRefreshLayout mRefreshLayout;

    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_spendings_layout);

        mBundle = new Bundle();

        mRecyclerView = (RecyclerView)findViewById(R.id.spendingsRecyclerView);
        searchView= (MaterialSearchView) findViewById(R.id.search_view);
        toolbar = (Toolbar)findViewById(R.id.SearchToolbar);
        setSupportActionBar(toolbar);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchAdapter = new SearchAdapter(this,transactionDataset);

        mRecyclerView.setAdapter(mSearchAdapter);

        getSupportLoaderManager().initLoader(LOADER_ID, null,this);

        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.SearchRefresh);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSupportLoaderManager().restartLoader(LOADER_ID,null,SearchActivity.this);
                mSearchAdapter.notifyDataSetChanged();
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        searchView.setMenuItem(item);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                transactionDataset.clear();
                mBundle.putString("Query",query);
                getSupportLoaderManager().restartLoader(LOADER_ID, mBundle, SearchActivity.this);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText == ""){
                    getSupportLoaderManager().restartLoader(LOADER_ID,null,SearchActivity.this);
                }else{
                    getQueryResult(newText);
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void getQueryResult(String text){
        List<Transaction> queryResultList = new ArrayList<>();

        for(Transaction transaction : transactionDataset){
            String name = transaction.getName().toLowerCase();
            if(name.contains(text.toLowerCase())){
                queryResultList.add(transaction);
                mSearchAdapter = new SearchAdapter(this,queryResultList);
                mRecyclerView.setAdapter(mSearchAdapter);

            }
        }
    }

    @Override
    public Loader<List<Transaction>> onCreateLoader(int i, Bundle bundle) {
        String strArgs;

        if(bundle != null){

            strArgs = bundle.getString("Query");

            DashBoardLoader dashLoader = new DashBoardLoader(this,transactionDataset,strArgs);
            dashLoader.forceLoad();

            return dashLoader;

        }else{
            DashBoardLoader dashLoader = new DashBoardLoader(this,transactionDataset,"");
            dashLoader.forceLoad();

            return dashLoader;
        }


    }

    @Override
    public void onLoadFinished(Loader<List<Transaction>> loader, List<Transaction> transactions) {
        transactionDataset.addAll(transactions);
        mSearchAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<List<Transaction>> loader) {
        transactionDataset.clear();
        mRecyclerView.setAdapter(null);

    }

}
