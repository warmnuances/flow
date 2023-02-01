package under.hans.com.flow.Adapters;

import android.content.Context;
import android.net.Uri;
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

import de.hdodenhof.circleimageview.CircleImageView;
import under.hans.com.flow.models.Categories;
import under.hans.com.flow.R;

/**
 * Created by Hans on 3/30/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    final private ListItemClickedListener mOnClickListener;

    public interface ListItemClickedListener{
        void onListItemClickedListener(String itemCategory);
    }

    private List<Categories> categoryDataset;
    private Context mContext;
    private int layoutCategory = R.layout.category_individual_items;

    private int lastSelectedPosition = -1;



    public CategoryAdapter(Context context, List<Categories> categoryData, ListItemClickedListener listener) {
        this.mContext = context;
        this.categoryDataset = categoryData;
        this.mOnClickListener = listener;
    }


    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(layoutCategory, parent,false);

        CategoryViewHolder catViewHolder = new CategoryViewHolder(itemView);

        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {

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

        private ImageView imgCategory;
        private TextView tvCategoryName;
        public RadioButton btCategory;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);

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
