package org.rowanieee.smartnetworking.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.constants.Networks;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.ImagePickerUtils;

import java.util.List;

/**
 * Created by guest1 on 5/30/2016.
 */
public class CurrentContactsAdapter extends AbstractCompostAdapter<SavedContact> {
    private CurrentContactsListener listener;
    public CurrentContactsAdapter(Context c, List<SavedContact> myDataset, CurrentContactsListener listener) {
        super(c, myDataset);
        this.listener = listener;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_connected;
    }

    @Override
    protected void populateItem(ViewHolder holder, int position, final SavedContact dataPoint) {
        ((ImageView) holder.v.findViewById(R.id.item_user_picture)).setImageBitmap(ImagePickerUtils.getBitmapFromBase64(dataPoint.getPhotoBase64()));
        ((TextView) holder.v.findViewById(R.id.item_user_name)).setText(dataPoint.getName());
        ((TextView) holder.v.findViewById(R.id.item_user_title)).setText(dataPoint.getTitle()+", "+dataPoint.getCompany());
        //Build a bunch of quick network items
        if(dataPoint.getEmail() != null && dataPoint.getEmail().length() > 1) {
            ImageView email = new ImageView(mContext);
            email.setImageResource(R.drawable.ic_email);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onActionClicked("mailto:"+dataPoint.getEmail());
                }
            });
            ((LinearLayout) holder.v.findViewById(R.id.item_user_networks)).addView(email);
        }
        if(dataPoint.getConnections().has(Networks.PHONE.getKey())) {
            ImageView email = new ImageView(mContext);
            email.setImageResource(R.drawable.ic_cellphone);
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onActionClicked("tel:"+dataPoint.getConnections().getString(Networks.PHONE.getKey()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            ((LinearLayout) holder.v.findViewById(R.id.item_user_networks)).addView(email);
        }

        //Click listener
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContactClicked(dataPoint);
            }
        });
    }

    public interface CurrentContactsListener {
        void onContactClicked(SavedContact savedContact);
        void onActionClicked(String uri);
    }
}
