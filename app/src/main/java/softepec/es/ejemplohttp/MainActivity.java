package softepec.es.ejemplohttp;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private Button getBtn;
    private Button postBtn;
    private Button getqueryBtn;

    private TextView respone;
    private EditText userDataET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getBtn = findViewById(R.id.get_b);
        getqueryBtn = findViewById(R.id.get_query_b);
        postBtn = findViewById(R.id.post_b);

        respone = findViewById(R.id.respone_t);
        userDataET = findViewById(R.id.post_tv);
    }

    public void getHttp(View v) {
        //instantiate async task which call service using OkHttp in the background
        // and execute it passing required parameter to it
        //get http request using okhttp
        new MainActivity.OkHttpAync().execute(this, "get", "");
    }

    public void getParamHttp(View v) {
        //same async task is used to call different services
        //request type is sent as parameter to async task to identify which service to call
        //get http request with query string using okhttp
        String userData = userDataET.getText().toString();
        new MainActivity.OkHttpAync().execute(this, "getparam", userData);
    }

    public void postHttp(View v) {
        //post http request using okhttp
        String userData = userDataET.getText().toString();
        new MainActivity.OkHttpAync().execute(this, "post", userData);
    }

    private class OkHttpAync extends AsyncTask<Object, Void, Object> {

        private String TAG = MainActivity.OkHttpAync.class.getSimpleName();
        private Context contx;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object... params) {
            contx = (Context) params[0];
            String requestType = (String) params[1];
            String requestParam = (String) params[2];

            Log.e(TAG, "processing http request in async task");

            if ("get".equals(requestType)) {
                Log.e(TAG, "processing get http request using OkHttp");
                return getHttpResponse();
            } else if ("getparam".equals(requestType)) {
                Log.e(TAG, "processing get http request with query parameters using OkHttp");
                return getParamHttpResponse(requestParam);
            } else if ("post".equals(requestType)) {
                Log.e(TAG, "processing post http request using OkHttp");
                return postHttpResponse(requestParam);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object result) {
            super.onPostExecute(result);

            if (result != null) {
                Log.e(TAG, "populate UI after response from service using OkHttp client");
                respone.setText((String) result);
            }
        }
    }

    public Object getHttpResponse() {
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://jsonplaceholder.typicode.com/posts";
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "error in getting response get request okhttp");
        }
        return null;
    }


    public Object getParamHttpResponse(String requestParam) {
        OkHttpClient httpClient = new OkHttpClient();

        String url = "https://jsonplaceholder.typicode.com/posts/"+requestParam;
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "error in getting response get request okhttp");
        }
        return null;
    }


    public Object postHttpResponse(String requestParam) {
        OkHttpClient httpClient = new OkHttpClient();
        String url = "https://jsonplaceholder.typicode.com/posts";

        RequestBody formBody = new FormBody.Builder()
                .add("post", requestParam)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();
        Response response = null;
        try {
            response = httpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                Log.e(TAG, "Got response from server using OkHttp ");
                return response.body().string();
            }

        } catch (IOException e) {
            Log.e(TAG, "error in getting response post request okhttp");
        }
        return null;

    }
}
