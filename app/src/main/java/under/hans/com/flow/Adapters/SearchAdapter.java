package under.hans.com.flow.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.DatabaseUtils;
import under.hans.com.flow.Utils.DateTimeUtils;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.models.Transaction;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SpendingsViewHolder> {

    private Context mContext;
    private int layoutCategory = R.layout.search_individual_item;
    private List<Transaction> searchDataSet = new ArrayList<>();

    public SearchAdapter(Context context,List<Transaction> dataset) {
        this.mContext = context;
        this.searchDataSet = dataset;
    }

    @Override
    public SpendingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        Context context = mContext;
        LayoutInflater inflater = LayoutInflater.from(context);
        itemView = inflater.inflate(layoutCategory, parent,false);

        SpendingsViewHolder mViewHolder = new SpendingsViewHolder(itemView);


        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(SpendingsViewHolder holder, int position) {

        Transaction transaction = searchDataSet.get(position);

        holder.tvAmount.setText(FormatAlgorithms.getFormattedFunds(transaction.getAmount()));
        holder.tvCategory.setText(transaction.getCategory());
        holder.tvDate.setText(DateTimeUtils.convertMillisToDate(transaction.getTimeMillis()));
        holder.tvName.setText(transaction.getName());
        int imgResource = DatabaseUtils.getCategoryPath(mContext,transaction.getCategory());
        holder.circleImageView.setImageResource(imgResource);
    }

    @Override
    public int getItemCount() {
        return searchDataSet.size();
    }

    class SpendingsViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategory,tvName, tvAmount,tvDate;
        CircleImageView circleImageView;

        public SpendingsViewHolder(View itemView) {
            super(itemView);
            tvCategory = (TextView)itemView.findViewById(R.id.tvCategory);
            tvName = (TextView)itemView.findViewById(R.id.tvName);
            tvAmount = (TextView)itemView.findViewById(R.id.tvAmount);
            tvDate = (TextView)itemView.findViewById(R.id.tvDate);
            circleImageView = (CircleImageView)itemView.findViewById(R.id.circleImageView);
        }
    }
}
