package under.hans.com.flow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import under.hans.com.flow.R;
import under.hans.com.flow.models.BudgetCategory;

import static android.support.constraint.Constraints.TAG;

public class BudgetPageAdapter extends RecyclerView.Adapter<BudgetPageAdapter.BudgetViewHolder> {

    private Context mContext;
    private List<BudgetCategory> budgetCategoryDataset;

    public BudgetPageAdapter(Context mContext, List<BudgetCategory> budgetCategoryDataset) {
        this.mContext = mContext;
        this.budgetCategoryDataset = budgetCategoryDataset;
    }

    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = mContext;
        int layoutIdIndividualItems = R.layout.budgetpage_individual_items;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdIndividualItems, parent,false);

        BudgetViewHolder viewHolder = new BudgetViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        BudgetCategory budgetCategory = budgetCategoryDataset.get(position);

        holder.tvCategory.setText(budgetCategory.getCategoryName());
        int budgetPercentage = budgetCategory.getCategoryPercentage();

        if(budgetPercentage < 0  || budgetPercentage > 100){
            budgetPercentage = 100;
        }

        holder.pgPercentageBar.setProgress(budgetPercentage);
        holder.tvPercentage.setText(String.valueOf(budgetPercentage));

    }

    @Override
    public int getItemCount() {
        return budgetCategoryDataset.size();
    }

    class BudgetViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory,tvPercentage;
        ProgressBar pgPercentageBar;

        public BudgetViewHolder(View itemView) {
            super(itemView);
            tvCategory = (TextView)itemView.findViewById(R.id.tvCategory);
            tvPercentage = (TextView)itemView.findViewById(R.id.tvPercentage);
            pgPercentageBar = (ProgressBar)itemView.findViewById(R.id.categoryProgressBar);
        }
    }
}
