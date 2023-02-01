package under.hans.com.flow.Preset;

import android.content.ContentUris;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.Toast;

import under.hans.com.flow.Adapters.PresetSpendingsAdapter;
import under.hans.com.flow.AlarmService.AlarmScheduler;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Forms.AddPresetActivity;
import under.hans.com.flow.R;
import under.hans.com.flow.Search.SearchActivity;
import under.hans.com.flow.Utils.VerticalSpaceItemDecoration;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Hans on 3/12/2018.
 */

public class PresetSpendingsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        PresetSpendingsAdapter.ItemClickedListener {

    private static final int FLOW_TYPE = 1;
    private static final int VERTICAL_ITEM_SPACE = 1;

    private RecyclerView presetRecyclerView;
    private PresetSpendingsAdapter mAdapter;
    private FloatingActionButton fabPreset;

    //Loader
    private static final int LOADER_ID = 300;

    @Override
    public void onItemClickedListener(int itemId) {
        updateIntent(itemId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preset_listview_spendings, null, false);

        getLoaderManager().initLoader(LOADER_ID,null, this);
        presetRecyclerView = (RecyclerView) view.findViewById(R.id.presetRecyclerView);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        presetRecyclerView.setHasFixedSize(true);
        presetRecyclerView.setLayoutManager(mLayoutManager);
        VerticalSpaceItemDecoration verticalSpaceItemDecoration = new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE);
        presetRecyclerView.addItemDecoration(verticalSpaceItemDecoration);
        mAdapter = new PresetSpendingsAdapter(getActivity(),this);
        presetRecyclerView.setAdapter(mAdapter);

//        //swipe to delete
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT |ItemTouchHelper.RIGHT) {
//
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//                int id = (int) viewHolder.itemView.getTag();
//
//                String stringId = Integer.toString(id);
//                Uri uri = SqlContractClass.PresetClass.CONTENT_URI;
//                uri = uri.buildUpon().appendPath(stringId).build();
//
//                getActivity().getContentResolver().delete(uri,null,null);
//                getLoaderManager().restartLoader(LOADER_ID,null,PresetSpendingsFragment.this);
//
//                new AlarmScheduler().cancelAlarm(getContext(),uri);
//            }
//
//        }).attachToRecyclerView(presetRecyclerView);

        fabPreset = (FloatingActionButton)view.findViewById(R.id.addPresetFab);
        fabPreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddPresetActivity.class);
                intent.putExtra("Type",0);
                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        String[] projection = {
                SqlContractClass.PresetClass._ID,
                SqlContractClass.PresetClass.COLUMN_PRESET_NAME,
                SqlContractClass.PresetClass.COLUMN_PRESET_CATEGORY,
                SqlContractClass.PresetClass.COLUMN_PRESET_MULTIPLIER,
                SqlContractClass.PresetClass.COLUMN_PRESET_CONSTANT,
                SqlContractClass.PresetClass.COLUMN_PRESET_TIMETRIGGER,
                SqlContractClass.PresetClass.COLUMN_PRESET_NOTIFY};
        String selection = SqlContractClass.PresetClass.COLUMN_PRESET_FLOWTYPE + "=?";

        String[] selectionArgs = {"outflow"};


        return new CursorLoader(getActivity(),
                SqlContractClass.PresetClass.CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    private void updateIntent(int itemId){

        int id = itemId;

        Uri contentUri = ContentUris.withAppendedId(SqlContractClass.PresetClass.CONTENT_URI, id);

        Intent uIntent = new Intent(getActivity(), AddPresetActivity.class);
        uIntent.setData(contentUri);
        uIntent.putExtra("Type", FLOW_TYPE);
        startActivity(uIntent);

    }
}
