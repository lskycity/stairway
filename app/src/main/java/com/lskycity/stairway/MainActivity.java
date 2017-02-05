package com.lskycity.stairway;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new FetchDataTask().execute("http://139.196.22.105:5000/stairway");
    }


    class FetchDataTask extends AsyncTask<String, Object, List<ConnectInfo>> {

        @Override
        protected List<ConnectInfo> doInBackground(String... params) {
            try {
                return getJSONObjectFromURL(params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>(0);
        }

        @Override
        protected void onPostExecute(List<ConnectInfo> messages) {
            TextView tv = (TextView) findViewById(R.id.text1);
            tv.setText(messages.toString());
        }
    }


    public static List<ConnectInfo> getJSONObjectFromURL(String urlString) throws IOException {

        HttpURLConnection urlConnection = null;

        URL url = new URL(urlString);

        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000 /* milliseconds */);
        urlConnection.setConnectTimeout(15000 /* milliseconds */);

        urlConnection.setDoOutput(true);

        urlConnection.connect();

        return readJsonStream(url.openStream());


    }

    public static List<ConnectInfo> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    private static List<ConnectInfo> readMessagesArray(JsonReader reader) throws IOException {
        List<ConnectInfo> messages = new ArrayList<ConnectInfo>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public static ConnectInfo readMessage(JsonReader reader) throws IOException {
        String sername = null;
        String ip = null;
        String username = null;
        String password = null;


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("name")) {
                sername = reader.nextString();
            } else if (name.equals("ip")) {
                ip = reader.nextString();
            } else if (name.equals("username")) {
                username = reader.nextString();
            } else {
                password = reader.nextString();
            }
        }
        reader.endObject();
        return new ConnectInfo(sername, ip, username, password);
    }
}
