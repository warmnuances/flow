package under.hans.com.flow.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import under.hans.com.flow.R;
import under.hans.com.flow.models.Categories;

public class PresetCategoryAdapter extends RecyclerView.Adapter<PresetCategoryAdapter.CategoryViewHolder>  {

    final private PresetCategoryAdapter.ListItemClickedListener mOnClickListener;

    public interface ListItemClickedListener{
        void onListItemClickedListener(String itemCategory);
    }


    private List<Categories> categoryDataset;
    private Context mContext;

    private int lastSelectedPosition = -1;


    public PresetCategoryAdapter(Context mContext, List<Categories> categoryDataset, ListItemClickedListener mOnClickListener ) {
        this.mOnClickListener = mOnClickListener;
        this.categoryDataset = categoryDataset;
        this.mContext = mContext;
    }

    @Override
    public PresetCategoryAdapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        Context context = mContext;
        int layoutCategory = R.layout.category_grid_items;
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(layoutCategory, parent,false);

        PresetCategoryAdapter.CategoryViewHolder catViewHolder = new PresetCategoryAdapter.CategoryViewHolder(itemView);

        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(PresetCategoryAdapter.CategoryViewHolder holder, int position) {

        Categories category = categoryDataset.get(position);
        holder.tvCategoryName.setText(category.getCategoryName());
        holder.btCategory.setChecked(lastSelectedPosition == position);
        holder.imgCategory.setImageResource(category.getPathRes());
    }

    @Override
    public int getItemCount() {
        return categoryDataset.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategoryName;
        RadioButton btCategory;
        LinearLayout linearSelect;
        ImageView imgCategory;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            linearSelect = (LinearLayout) itemView.findViewById(R.id.linearSelect);
            tvCategoryName = (TextView)itemView.findViewById(R.id.categoryName);
            btCategory = (RadioButton) itemView.findViewById(R.id.btCategory);
            imgCategory = (ImageView)itemView.findViewById(R.id.imgCategory);


            linearSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();

                    String categoryName = tvCategoryName.getText().toString();

                    mOnClickListener.onListItemClickedListener(categoryName);

                }
            });
        }
    }
}
