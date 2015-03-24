package com.gmail.nochtemirae.sms2mail_v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
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
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    SenderMailAsync senderMailAsync = new SenderMailAsync();
                    senderMailAsync.execute();
                } else
                    Toast.makeText(context, "email!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class SenderMailAsync extends AsyncTask<Object, String, Boolean> {
        ProgressDialog WaitingDialog;

        @Override
        protected void onPreExecute() {
            WaitingDialog = ProgressDialog.show
                    (SenderEmail.this, "Отправка данных", "Отправляем сообщение...", true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            WaitingDialog.dismiss();
            Toast.makeText(context, "Отправка завершена", Toast.LENGTH_LONG).show();
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
                Toast.makeText(context, "Ошибка отправки сообщения!",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    }
}
