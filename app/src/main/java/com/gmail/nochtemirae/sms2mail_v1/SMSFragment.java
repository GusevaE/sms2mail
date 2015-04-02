package com.gmail.nochtemirae.sms2mail_v1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class SMSFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sms,
                container, false);
        getSMSList(view);
        Button button = ((Button) view.findViewById(R.id.fs_button));
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongViewCast")
            @Override
            public void onClick(View v) {
                Intent senderClass = new Intent(getActivity(), SenderEmail.class);
                senderClass.putExtra("list",
                        ((EditText) getActivity().findViewById(R.id.fs_list)).getText().toString());
                senderClass.putExtra("title",
                        ((TextView) getActivity().findViewById(R.id.fs_title)).getText().toString());
                startActivity(senderClass);
            }
        });
        return view;
    }

    private void getSMSList(View v) {
        EditText edtText = (EditText) v.findViewById(R.id.fs_list);
        Uri uriSms = Uri.parse("content://sms/inbox");
        SMSFragment context = this;
        Cursor cur = context.getActivity().getContentResolver().query(uriSms, null, null, null, null);
        SimpleDateFormat sdfrmt = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                edtText.append("\n" + sdfrmt.format(cur.getLong(4)) + "\t" + cur.getString(2) + ":\n" + cur.getString(12));
                edtText.append("\n__________\n");
            }
        }
    }
}

