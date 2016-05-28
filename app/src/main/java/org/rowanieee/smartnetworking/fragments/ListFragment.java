package org.rowanieee.smartnetworking.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.felkertech.settingsmanager.SettingsManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.rowanieee.smartnetworking.R;
import org.rowanieee.smartnetworking.activities.UserInfoActivity;
import org.rowanieee.smartnetworking.database.PersonQueryDbHelper;
import org.rowanieee.smartnetworking.model.SavedContact;
import org.rowanieee.smartnetworking.utils.NetworksUtils;

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
        v = inflater.inflate(R.layout.fragment_list, container, false);
        v.findViewById(R.id.fab_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SavedContact newContact;
                try {
                    newContact = new SavedContact(null);
                    MaterialDialog contactCreator = new MaterialDialog.Builder(getContext())
                            .title(R.string.new_contact_title)
                            .customView(R.layout.userinfo, false)
                            .positiveText(R.string.action_connect)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    PersonQueryDbHelper pqdh = new PersonQueryDbHelper(getContext());
                                    pqdh.insert(pqdh.getWritableDatabase(), newContact);
                                    redrawContacts();
                                }
                            })
                            .show();
                    View dialogView = contactCreator.getCustomView();
                    //Do some custom adjustments on this layout
                    dialogView.findViewById(R.id.fab_edit).setVisibility(View.GONE);
                    NetworksUtils.initializeNetworkFieldsForUser(newContact,
                            (LinearLayout) dialogView.findViewById(R.id.contact_container), getContext());
                    //Load your preferences
                    ((EditText) dialogView.findViewById(R.id.editName)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            newContact.setName(s.toString());
                        }
                    });
                    ((EditText) dialogView.findViewById(R.id.editCompany)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            newContact.setCompany(s.toString());
                        }
                    });
                    ((EditText) dialogView.findViewById(R.id.editTitle)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            newContact.setTitle(s.toString());
                        }
                    });
                    ((EditText) dialogView.findViewById(R.id.editPerState)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            newContact.setPersonalStatement(s.toString());
                        }
                    });
                    ((EditText) dialogView.findViewById(R.id.editEmail)).addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            newContact.setEmail(s.toString());
                        }
                    });
                    //TODO Profile pictures
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        redrawContacts();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        redrawContacts();
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
        v.findViewById(R.id.printlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent visitUser = new Intent(getContext(), UserInfoActivity.class);
                visitUser.putExtra(UserInfoActivity.EXTRA_PERSON_ID, 1);
                startActivity(visitUser);
            }
        });
    }
}
