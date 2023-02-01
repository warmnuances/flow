package under.hans.com.flow.Adapters;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import under.hans.com.flow.Data.SqlContractClass;
import under.hans.com.flow.Data.SqlContractClass.PresetClass;

import de.hdodenhof.circleimageview.CircleImageView;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;

/**
 * Created by Hans on 3/28/2018.
 */

public class PresetSpendingsAdapter extends RecyclerView.Adapter<PresetSpendingsAdapter.presetViewHolder>{

    private static final String TAG = "PresetSpendingsAdapter";

    private Cursor mCursor;
    private Context mContext;

    public interface ItemClickedListener{
        void onItemClickedListener(int itemId);
    }

    final private ItemClickedListener mItemClickedListener;

    public PresetSpendingsAdapter(Context context, ItemClickedListener itemClickedListener) {
        this.mContext = context;
        this.mItemClickedListener = itemClickedListener;
    }

    @Override
    public presetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = mContext;
        int layoutIdIndividualItems = R.layout.preset_individual_item;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdIndividualItems,parent,false);

        presetViewHolder vh = new presetViewHolder(view);

        return vh;
    }



    @Override
    public void onBindViewHolder(presetViewHolder holder, int position) {


        int idIndex = mCursor.getColumnIndex(PresetClass._ID);
        int categoryIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_CATEGORY);
        int nameIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_NAME);
        int multiIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_MULTIPLIER);
        int constantIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_CONSTANT);
        int triggerIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_TIMETRIGGER);
        int shouldNotifyIndex = mCursor.getColumnIndex(PresetClass.COLUMN_PRESET_NOTIFY);


        mCursor.moveToPosition(position);

                String constant;
                int multiplier = mCursor.getInt(multiIndex);

                if(multiplier > 1){
                    constant = mCursor.getString(constantIndex) + "s";
                }else {
                    constant = mCursor.getString(constantIndex);
                }



                final int id = mCursor.getInt(idIndex);
                String category = mCursor.getString(categoryIndex);
                String name = mCursor.getString(nameIndex);
                int shouldNotify = mCursor.getInt(shouldNotifyIndex);


                String recurrence = "Every " + String.valueOf(multiplier) + " " + constant;
                String date = DateTimeUtils.formatDatetoString(mCursor.getInt(triggerIndex));

                if(shouldNotify == 1){
                    holder.switchNotify.setChecked(true);
                }else {
                    holder.switchNotify.setChecked(false);
                }

                holder.itemView.setId(id);
                holder.itemView.setTag(id);
                holder.tvDueDate.setText(date);
                holder.tvRecurrence.setText(recurrence);
                holder.tvCatName.setText(category);
                holder.tvName.setText(name);

                int imgResource = DatabaseUtils.getCategoryPath(mContext,category);
                holder.imgCategory.setImageResource(imgResource);


                holder.switchNotify.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    int itemID,boolNotify;
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                        itemID = id;

                        if(isChecked){
                            boolNotify = 1;
                        }else{
                            boolNotify = 0;
                        }
                        if(itemID > 0){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(PresetClass.COLUMN_PRESET_NOTIFY,boolNotify);


                            Uri presetUri = ContentUris.withAppendedId(PresetClass.CONTENT_URI,itemID);

                            int rowsUpdated = mContext.getContentResolver()
                                    .update(presetUri, contentValues,null,null);
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        if(mCursor == null){
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor updateCursor){

        if(mCursor == updateCursor){
            return null;
        }

        Cursor iCursor = mCursor;
        this.mCursor = updateCursor;

        if(updateCursor !=null){
            this.notifyDataSetChanged();
        }

        return iCursor;
    }

    class presetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDueDate,tvName,tvRecurrence,tvCatName;
        CircleImageView imgCategory;
        Switch switchNotify;
        private int itemID;


        public presetViewHolder(View itemView) {
            super(itemView);

            tvDueDate = (TextView)itemView.findViewById(R.id.tvDueDate);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvRecurrence = (TextView)itemView.findViewById(R.id.tvRecurrence);
            tvCatName = (TextView)itemView.findViewById(R.id.categoryName);
            imgCategory = (CircleImageView)itemView.findViewById(R.id.imgCategory);
            switchNotify = (Switch)itemView.findViewById(R.id.notificationSwitch);

            itemID = itemView.getId();
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemID = itemView.getId();
            mItemClickedListener.onItemClickedListener(itemID);
        }
    }
}
