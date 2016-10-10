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
        new FetchDataTask().execute();
    }



    class FetchDataTask extends AsyncTask<Object, Object, List<Message>> {

        @Override
        protected List<Message> doInBackground(Object... params) {
            try {
                return getJSONObjectFromURL("http://139.196.22.105:5000/stairway");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new ArrayList<>(0);
        }

        @Override
        protected void onPostExecute(List<Message> messages) {
            TextView tv = (TextView) findViewById(R.id.text1);
            tv.setText(messages.toString());
        }
    }


    public static List<Message> getJSONObjectFromURL(String urlString) throws IOException {

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

    static class Message {
        String sername;
        String ip;
        String username;
        String password;

        public Message(String sername, String ip, String username, String password) {
            this.sername = sername;
            this.ip = ip;
            this.username = username;
            this.password = password;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Message message = (Message) o;

            if (sername != null ? !sername.equals(message.sername) : message.sername != null)
                return false;
            if (ip != null ? !ip.equals(message.ip) : message.ip != null) return false;
            if (username != null ? !username.equals(message.username) : message.username != null)
                return false;
            return password != null ? password.equals(message.password) : message.password == null;

        }

        @Override
        public int hashCode() {
            int result = sername != null ? sername.hashCode() : 0;
            result = 31 * result + (ip != null ? ip.hashCode() : 0);
            result = 31 * result + (username != null ? username.hashCode() : 0);
            result = 31 * result + (password != null ? password.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Message{" +
                    "sername='" + sername + '\'' +
                    ", ip='" + ip + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    public static List<Message> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    private static List<Message> readMessagesArray(JsonReader reader) throws IOException {
        List<Message> messages = new ArrayList<Message>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public static Message readMessage(JsonReader reader) throws IOException {
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
        return new Message(sername, ip, username, password);
    }
}
