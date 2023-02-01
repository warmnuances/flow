package under.hans.com.flow.Forms;

import android.content.ContentUris;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.content.ContentValues;
import android.content.Intent;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import under.hans.com.flow.Adapters.CategoryAdapter;
import under.hans.com.flow.BudgetPlan.BudgetingActivity;
import under.hans.com.flow.BudgetPlan.CategoryBudget;
import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Preset.PresetOverview;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.models.Categories;

public class AddCategoryActivity extends AppCompatActivity
        implements CategoryAdapter.ListItemClickedListener{

    private static final String TAG = "AddCategoryActivity";
    Button btAddCategory;
    EditText etName;
    RecyclerView rvCategory;
    CategoryAdapter mAdapter;
    private List<Categories> categoryDataset = new ArrayList<>();
    private String getCategory;
    private String intentCategory;


    private static final int LOADER_ID = 12;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category_layout);

        setupWidgets();

        intentCategory = getIntent().getStringExtra("category");
        if(intentCategory!=null){
            Bundle mBundle = new Bundle();
            mBundle.putString("category",intentCategory);

            etName.setText(intentCategory);
            setCategoryOnIntent(intentCategory);
        }

    }
    private void setupWidgets(){
        btAddCategory = (Button)findViewById(R.id.btAddCategory);
        etName = (EditText)findViewById(R.id.etName);
        rvCategory = (RecyclerView)findViewById(R.id.parentRecyclerview);

        btAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIfHasInput();

            }
        });

        categoryDataset = DatabaseUtils.getCategoryDataset(this);
        mAdapter = new CategoryAdapter(this, categoryDataset,this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);

        rvCategory.setHasFixedSize(true);
        rvCategory.setLayoutManager(layoutManager);

        rvCategory.setAdapter(mAdapter);
    }

    private void checkIfHasInput(){

        String name = etName.getText().toString();
        if(!getCategory.equals("") ||!name.equals("")){
            onClickAddCategory();
            Intent intent = new Intent(AddCategoryActivity.this,PresetOverview.class);
            intent.putExtra("page",2);
            startActivity(intent);
        }else {
            Toast.makeText(AddCategoryActivity.this, "Oops! you forgot to enter something", Toast.LENGTH_SHORT).show();
        }
    }

    private void onClickAddCategory(){
        String getName = etName.getText().toString();


        ContentValues contentValues = new ContentValues();
        contentValues.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME,getName);
        contentValues.put(SqlContractClass.CategoryClass.COLUMN_CATEGORY_PARENT,getCategory);


        if(intentCategory!=null){

            String[] projection = {SqlContractClass.CategoryClass._ID};
            String selection = SqlContractClass.CategoryClass.COLUMN_CATEGORY_NAME + " =?";
            String[] selectionArgs = {intentCategory};

            Cursor cursor = getContentResolver().query(
                    SqlContractClass.CategoryClass.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    null);

            if(cursor.moveToFirst()){

                Uri categoryUri = ContentUris.withAppendedId(SqlContractClass.CategoryClass.CONTENT_URI,
                        cursor.getInt(cursor.getColumnIndex(SqlContractClass.CategoryClass._ID)));

                getContentResolver().update(categoryUri,
                        contentValues,
                        null,
                        null);
            }
            else {
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

        }else {
            getContentResolver().insert(SqlContractClass.CategoryClass.CONTENT_URI,contentValues);
        }
    }

    @Override
    public void onListItemClickedListener(String itemCategory) {
        getCategory = itemCategory;
    }

    private void setCategoryOnIntent(final String category){

        final int pos = DatabaseUtils.getCategoryPosition(this, category);
        rvCategory.scrollToPosition(pos);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onListItemClickedListener(category);
                try {
                    rvCategory.findViewHolderForAdapterPosition(pos).itemView.performClick();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        },5);
    }
}
