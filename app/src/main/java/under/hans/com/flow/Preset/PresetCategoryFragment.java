package under.hans.com.flow.Preset;

import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Adapters.PresetCategoryAdapter;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Forms.AddCategoryActivity;
import under.hans.com.flow.Forms.AddPresetActivity;
import under.hans.com.flow.models.Categories;
import under.hans.com.flow.R;

import under.hans.com.flow.Data.SqlContractClass.CategoryClass;
import under.hans.com.flow.Utils.FormatAlgorithms;

/**
 * Created by Hans on 3/31/2018.
 */

public class PresetCategoryFragment extends Fragment implements PresetCategoryAdapter.ListItemClickedListener{

    private RecyclerView mRecyclerView;
    private PresetCategoryAdapter mCategoryAdapter;
    private FloatingActionButton fabCategory;

    String getCat;

    private List<Categories> categoryDataset = new ArrayList<>();

    Categories objCategory;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.preset_category_fragment,null,false);

        categoryDataset = getCategoryDataset();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.categoryRecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(false);



        mCategoryAdapter = new PresetCategoryAdapter(getActivity(),categoryDataset,this);
        mRecyclerView.setAdapter(mCategoryAdapter);



        fabCategory = (FloatingActionButton)view.findViewById(R.id.addCategoryFab);
        fabCategory.setVisibility(View.GONE);
//        fabCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getActivity(),AddCategoryActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }



    private List<Categories> getCategoryDataset(){

        String[] projection = { CategoryClass.COLUMN_CATEGORY_NAME,
                CategoryClass.COLUMN_CATEGORY_PRIORITY,
                CategoryClass.COLUMN_CATEGORY_RECENT,
                CategoryClass.COLUMN_CATEGORY_USAGE,
                CategoryClass.COLUMN_CATEGORY_PATH};


        Cursor mCursor = getActivity().getContentResolver().query(CategoryClass.CONTENT_URI,
                projection,
                null,
                null,
                null,
                null);


        for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
            int nameIndex = mCursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_NAME);
            int priorityIndex = mCursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_PRIORITY);
            int usageIndex = mCursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_USAGE);
            int pathIndex = mCursor.getColumnIndex(CategoryClass.COLUMN_CATEGORY_PATH);

            String name = mCursor.getString(nameIndex);
            int priority = mCursor.getInt(priorityIndex);
            int usage = mCursor.getInt(usageIndex);
            int path = mCursor.getInt(pathIndex);


            boolean isPriority;

            isPriority = FormatAlgorithms.checkPriority(getContext(),name);

            int totalPriority = FormatAlgorithms.getCategoryPriority(usage,priority,isPriority);


            objCategory = new Categories();
            objCategory.setCategoryName(name);
            objCategory.setCompareSort(totalPriority);
            objCategory.setPathRes(path);


            categoryDataset.add(objCategory);
        }

        return categoryDataset;
    }

    @Override
    public void onListItemClickedListener(String itemCategory) {
        getCat = itemCategory;
//        Intent intent = new Intent(getActivity(),AddCategoryActivity.class);
//        intent.putExtra("category", getCat);
//        startActivity(intent);
    }

}
