package under.hans.com.flow.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import under.hans.com.flow.R;
import under.hans.com.flow.models.Categories;

public class CategoryBudgetAdapter extends RecyclerView.Adapter<CategoryBudgetAdapter.CategoryViewHolder>{

    final private ListItemClickedListener mOnClickListener;

    public interface ListItemClickedListener{
        void onListItemClickedListener(String itemCategory);
    }

    public CategoryBudgetAdapter( Context mContext,List<Categories> categoryDataset, ListItemClickedListener mOnClickListener) {
        this.mOnClickListener = mOnClickListener;
        this.categoryDataset = categoryDataset;
        this.mContext = mContext;
    }

    private List<Categories> categoryDataset;
    private Context mContext;
    private int layoutCategory = R.layout.categorybudget_individual_items;

    private int lastSelectedPosition = -1;



    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(layoutCategory, parent,false);

        CategoryViewHolder catViewHolder = new CategoryViewHolder(itemView);

        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
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
        public RadioButton btCategory;
        ImageView imgCategory;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btCategory.performClick();
                }
            });

            tvCategoryName = (TextView)itemView.findViewById(R.id.categoryName);
            btCategory = (RadioButton) itemView.findViewById(R.id.btCategory);
            imgCategory = (ImageView)itemView.findViewById(R.id.imgCategory);

            btCategory.setOnClickListener(new View.OnClickListener() {
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
