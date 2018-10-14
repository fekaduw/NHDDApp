package et.gov.fmoh.nhddapp.nhddapp.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import et.gov.fmoh.nhddapp.nhddapp.R;
import et.gov.fmoh.nhddapp.nhddapp.utils.ItemClickListenerHelper;

public abstract class CategoryListViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private ArrayList<T> items;
    private ItemClickListenerHelper mListener;

    public abstract void onBindData(ViewHolder holder, T val);
    public abstract void filter(String charText);
    public abstract void update(ArrayList<T> items);

    public CategoryListViewAdapter(Context context, ArrayList<T> items, ItemClickListenerHelper mListener){
        this.context = context;

        update(items);

        this.items = items;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View r = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_pagination, parent, false);

        ViewHolder holder = new ViewHolder(r);

        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindData(holder,items.get(position));
    }

    @Override
    public int getItemCount() {
        return items!=null? items.size() : 0;
    }

   /* public void addItems( ArrayList<T> savedCardItemz){
        items = savedCardItemz;
        this.notifyDataSetChanged();
    }*/

    public T getItem(int position){
        return items.get(position);
    }


}