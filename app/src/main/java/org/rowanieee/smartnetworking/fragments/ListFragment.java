package org.rowanieee.smartnetworking.fragments;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Will show a list of everyone you've connected with in the past
 * Created by Nick Felker on 5/26/2016.
 */
public class ListFragment extends Fragment {
    public static final int PERMISSION_WRITE_CONTACTS = 308;
    private static final String TAG = "ListFragment";

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
                newContact = new SavedContact(0);
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

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (((AppCompatActivity) getActivity()).checkSelfPermission(Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                //Resync
                resyncContacts();
            } else {
                ((AppCompatActivity) getActivity()).requestPermissions(new String[] {Manifest.permission.WRITE_CONTACTS}, PERMISSION_WRITE_CONTACTS);
            }
        } else {
            //We already have permission
            resyncContacts();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == PERMISSION_WRITE_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            resyncContacts();
        } else {
            Toast.makeText(getContext(), "Contacts will only be available in this app", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void resyncContacts() {
        //TODO Actually write to contacts DB
        //Run in a separate thread so that we don't query forever on UI thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<SavedContact> savedContacts =
                        new PersonQueryDbHelper(getContext()).readAll(new PersonQueryDbHelper(getContext()).getReadableDatabase());

                //We do have permission
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                        null, null, null, null);
                if (cur.getCount() > 0) {
                    while (cur.moveToNext()) {
                        String id = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts._ID));
                        String name = cur.getString(
                                cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        //Query note
                        String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                        String[] noteWhereParams = new String[]{id,
                                ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                        Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null);
                        if (noteCur.moveToFirst()) {
                            String note = noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                            if(note.contains("SmartNetwork")) {
                                //One of ours
                                for(SavedContact sc: savedContacts) {
                                    if(sc.getName().equals(name)) {
                                        //Already exists
                                        savedContacts.remove(sc);
                                        //TODO Update contact
                                    }
                                }
                            }
                        }
                        noteCur.close();
                    }
                }

                //Now we only have new contacts left
                ArrayList<ContentProviderOperation> contentProviderOperations = new ArrayList<>();
                for(SavedContact sc: savedContacts) {
//                    sc.getDatabaseId()
//                    sc.getAboutme()
//                    sc.getPersonalStatement()
//                    sc.getEmail()
//                    sc.getCompany()
//                    sc.getTitle()
//                    sc.getConnections()
                    //TODO Insert
                    contentProviderOperations.add(
                            ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, sc.getName())
//                                .withValue(ContactsContract.CommonDataKinds, null)
                                .build());
                    try {
                        getContext().getContentResolver().
                                applyBatch(ContactsContract.AUTHORITY, contentProviderOperations);
                    } catch (RemoteException | OperationApplicationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void showFab() {
        ((FloatingActionButton) v.findViewById(R.id.fab_add)).show();
    }
    public void hideFab() {
        ((FloatingActionButton) v.findViewById(R.id.fab_add)).hide();
    }
}
