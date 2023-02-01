package under.hans.com.flow.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;
import java.util.List;

import under.hans.com.flow.Forms.AddItemsForms;
import under.hans.com.flow.R;
import under.hans.com.flow.Utils.FormatAlgorithms;
import under.hans.com.flow.models.Transaction;

import static android.content.ContentValues.TAG;

/**
 * Created by Hans on 2/2/2018.
 */

public class TimelineRecyclerAdapter extends RecyclerView.Adapter<TimelineRecyclerAdapter.mViewHolder> {

    private Context mContext;
    private List<Transaction> transactionDataset = new ArrayList<>();



    public TimelineRecyclerAdapter(Context context, List<Transaction> data){
        this.mContext = context;
        this.transactionDataset = data;
    }

    @Override
    public mViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = mContext;
        int layoutIdIndividualItems = R.layout.timeline_individual_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdIndividualItems, parent,false);

        mViewHolder viewHolder = new mViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(mViewHolder holder, int position) {

        final Transaction transaction = transactionDataset.get(position);

        String day = transaction.getDay();
        if(Integer.parseInt(day) < 10 ){
            day = "0" + day;
        }


        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddItemsForms.class);
                intent.setData(transaction.getmUri());
                intent.putExtra("Type",transaction.getType());
                mContext.startActivity(intent);
            }
        });


        holder.tvTitle.setText(transaction.getName());
        holder.tvMonth.setText(transaction.getMonth());
        holder.tvDay.setText(day);
        holder.tvAmount.setText("$" + FormatAlgorithms.getFormattedFunds(transaction.getAmount()));

        if(transaction.getType() == 1 ){
            holder.marker
                    .setMarker(mContext.getResources().getDrawable(R.drawable.circle_indicator_negative));
            holder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.textRed));

        }else if(transaction.getType() == 2){
            holder.marker
                    .setMarker(mContext.getResources().getDrawable(R.drawable.circle_indicator_positive));
            holder.tvAmount.setTextColor(mContext.getResources().getColor(R.color.textGreen));
        }

    }

    @Override
    public int getItemCount() {
        return transactionDataset.size();
    }

   class mViewHolder extends RecyclerView.ViewHolder{


       TextView tvDay, tvMonth, tvTitle, tvAmount;
       TimelineView marker;
       ConstraintLayout constraintLayout;

        public mViewHolder(View itemView) {
            super(itemView);
            constraintLayout = (ConstraintLayout)itemView.findViewById(R.id.constraintLayout);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDay = (TextView) itemView.findViewById(R.id.tvDay);
            tvMonth = (TextView) itemView.findViewById(R.id.tvMonth);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            marker = (TimelineView) itemView.findViewById(R.id.time_marker);
        }
   }
}
