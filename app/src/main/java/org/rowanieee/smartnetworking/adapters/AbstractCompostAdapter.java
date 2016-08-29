package org.rowanieee.smartnetworking.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Nick Felker on 5/30/2016.
 */
public abstract class AbstractCompostAdapter<T> extends RecyclerView.Adapter<AbstractCompostAdapter.ViewHolder> {
    protected List<T> mDataset;
    protected Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View v;
        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }

    public AbstractCompostAdapter(Context c, List<T> myDataset) {
        mDataset = myDataset;
        mContext = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AbstractCompostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(mContext)
                .inflate(getLayoutResId(), parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        populateItem(holder, position, mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    //Abstract classes
    protected abstract int getLayoutResId();
    protected abstract void populateItem(ViewHolder holder, int position, T dataPoint);
//    protected abstract static RecyclerView.LayoutManager getLayoutManager(Context mContext); //FIXME
}
