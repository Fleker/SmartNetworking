package org.rowanieee.smartnetworking.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.fragments.ListFragment;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.ImagePickerUtils;

import java.util.List;

/**
 * Created by guest1 on 5/30/2016.
 */
public class NearbyContactsAdapter extends AbstractCompostAdapter<SavedContact> {
    private NearbyContactListener listener;
    public NearbyContactsAdapter(Context c, List myDataset, NearbyContactListener listener) {
        super(c, myDataset);
        this.listener = listener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_nearby;
    }

    @Override
    protected void populateItem(ViewHolder holder, int position, final SavedContact dataPoint) {
        ((ImageView) holder.v.findViewById(R.id.item_user_picture)).setImageBitmap(ImagePickerUtils.getBitmapFromBase64(dataPoint.getPhotoBase64()));
        ((TextView) holder.v.findViewById(R.id.item_user_name)).setText(dataPoint.getName());
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add the user!
                listener.onContactClicked(dataPoint);
            }
        });
    }

    public static RecyclerView.LayoutManager getLayoutManager(Context mContext) {
        return new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
    }

    public interface NearbyContactListener {
        void onContactClicked(SavedContact addedContact);
    }
}
