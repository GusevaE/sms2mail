package com.gmail.nochtemirae.sms2mail_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.provider.ContactsContract.CommonDataKinds.Phone;

public class ContactsFragment extends Fragment {

    @SuppressLint("InlinedApi")
    String[] projection = new String[]{
            ContactsContract.Data.CONTACT_ID,
            ContactsContract.Data.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Event.CONTACT_ID,
            ContactsContract.CommonDataKinds.Event.START_DATE,
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts,
                container, false);
        Button button = ((Button) view.findViewById(R.id.fc_button));
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                Intent senderClass = new Intent(getActivity(), SenderEmail.class);
                senderClass.putExtra("list",
                        ((EditText) getActivity().findViewById(R.id.fc_list)).getText().toString());
                senderClass.putExtra("title",
                        ((TextView) getActivity().findViewById(R.id.fc_title)).getText().toString());
                startActivity(senderClass);
            }
        });
        getContList(view);
        return view;
    }

    private void getContList(View v) {
        final EditText edtText = (EditText) v.findViewById(R.id.fc_list);
        Uri uri = ContactsContract.Data.CONTENT_URI;
        final ContactsFragment context = this;
        Cursor cur = context.getActivity().getContentResolver()
                .query(uri, projection, null, null, null);
        long id = -1;
        while (cur.moveToNext()) {
            if (id != cur.getLong(0)) {
                edtText.append("\n[" + cur.getString(0) + "]\t" + cur.getString(1) + ":\n");
                id = cur.getLong(cur.getColumnIndex(ContactsContract.Data.CONTACT_ID));
                edtText.append(getPhone(String.valueOf(id)) + "\n");
            }
        }
    }

    private String getPhone(String contactID) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String where = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?";
        String[] selectionArgs = new String[]{contactID};
        ContactsFragment context = this;
        Cursor result = context.getActivity().getContentResolver()
                .query(uri, null, where, selectionArgs, null);
        if (result.moveToFirst()) {
            String phone = result.getString
                    (result.getColumnIndex(Phone.NUMBER));
            if (phone == null) {
                result.close();
                return null;
            }
            result.close();
            return phone;
        }
        result.close();
        return null;
    }
}