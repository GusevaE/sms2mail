package com.gmail.nochtemirae.sms2mail_v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SenderEmail extends Activity {
    private Context context;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender_email);
        context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        EditText body = (EditText) findViewById(R.id.se_body);
        body.setText((String) getIntent().getSerializableExtra("list"));
        EditText title = (EditText) findViewById(R.id.se_title);
        title.setText((String) getIntent().getSerializableExtra("title"));
        Button button = ((Button) findViewById(R.id.se_button));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email = (EditText) findViewById(R.id.se_email);
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    Toast.makeText(context, getString(R.string.se_wrong_email), Toast.LENGTH_SHORT).show();
                } else if (!checkInternet())
                    Toast.makeText(context, getString(R.string.se_not_connection), Toast.LENGTH_SHORT).show();
                else {
                    SenderMailAsync senderMailAsync = new SenderMailAsync();
                    senderMailAsync.execute();
                }
            }
        });
    }

    public boolean checkInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
        }

    private class SenderMailAsync extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show
                    (SenderEmail.this, getString(R.string.se_sending), getString(R.string.se_send_mail), true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(context, getString(R.string.se_ending), Toast.LENGTH_LONG).show();
            ((Activity) context).finish();
        }

        @Override
        protected Boolean doInBackground(Object... params) {
            String title = ((EditText) findViewById(R.id.se_title)).getText().toString();
            String text = ((EditText) findViewById(R.id.se_body)).getText().toString();
            String where = ((EditText) findViewById(R.id.se_email)).getText().toString();
            final String from = "daemon4mail@yandex.ru";
            final String password = "1qaz2wsx3edc";
            final String smtp = "smtp.yandex.ru";
            try {
                MailSenderClass sender = new MailSenderClass(from, password, smtp);
                sender.sendMail(title, text, from, where);
            } catch (Exception e) {
                Toast.makeText(context, getString(R.string.se_error),
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
