package org.rowanieee.smartnetworking.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.model.SavedContact;

import java.util.ArrayList;

/**
 * Will show a list of everyone you've connected with in the past
 * Created by Nick Felker on 5/26/2016.
 */
public class ListFragment extends Fragment {
    private View v;

    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_settings, container, false);
        v.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaterialDialog contactCreator = new MaterialDialog.Builder(getContext())
                        .title("We are adding a contact!")
                        .customView(R.layout.add_dialog, false)
                        .positiveText("Add")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                //TODO Add in user data
                                String name = ((EditText) dialog.getCustomView().findViewById(R.id.name)).getText().toString();
                                String info = ((EditText) dialog.getCustomView().findViewById(R.id.info)).getText().toString();
                                PersonQueryDbHelper pqdh = new PersonQueryDbHelper(getContext());
                                try {
                                    pqdh.insert(pqdh.getWritableDatabase(), new SavedContact(name, "", "",
                                            "", new JSONObject("{info: "+info+"}")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                redrawContacts();
                            }
                        })
                        .show();
            }
        });
        return v;
    }
    /* TODO @(Seamus) Replace this function with an appropriate RecyclerView */
    public void redrawContacts() {
        PersonQueryDbHelper pqdh = new PersonQueryDbHelper(getContext());
        ArrayList<SavedContact> contacts = pqdh.readAll(pqdh.getReadableDatabase());
        String printable = "";
        for(SavedContact contact: contacts) {
            printable += contact.getName()+" "+contact.getEmail()+"\n"+contact.getAboutme()+"\n"+contact.getConnections()+"\n\n\n";
        }
        ((TextView) v.findViewById(R.id.printlist)).setText(printable);
    }
}
