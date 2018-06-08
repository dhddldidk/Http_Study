package kr.or.dgit.it.http_study.volley;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import kr.or.dgit.it.http_study.R;
import kr.or.dgit.it.http_study.parser.Item;

public class HttpVolleyActivity extends AppCompatActivity {

    private static final String REQ_TAG="REQ_TAG";
    private TextView serverResp;
    private ImageView serverImg;
    private NetworkImageView networkImageView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_volley);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(getIntent().getStringExtra("title"));

        serverResp = findViewById(R.id.server_resp);
        serverResp.setMovementMethod(new ScrollingMovementMethod());
        serverImg = findViewById(R.id.server_img);
        networkImageView = findViewById(R.id.networkImgView);
        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getStringResponse(View view) {
        final String strProduct = "Test";
        final String strMaker = "Bo";
        final String strPrice = "5000";

        String url_StringResponse = "http://192.168.0.69:8080/androidHTTPTest/input.jsp?Product="+strProduct+"&Maker="+strMaker+"&Price="+strPrice;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_StringResponse, getStringRequestListener(), getErrorListener());
        stringRequest.setTag(REQ_TAG);
        requestQueue.add(stringRequest);
    }

    private Response.Listener<String> getStringRequestListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                serverResp.setText("String Response : "+response);
            }
        };
    }

    private Response.ErrorListener getErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                serverResp.setText("Error getting response");
            }
        };
    }

    public void getStringResponsePost(View view) {
        final Item item = new Item("NoteBook","DELL",500);
        String url_StringResponse = "http://192.168.0.69:8080/androidHTTPTest/input.jsp";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_StringResponse, getStringRequestListener(), getErrorListener())
        {
            //item 정보를 서버에 전송
            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError{

                Map<String, String> params = new HashMap<String, String>();
                params.put("Product", item.getItemName());
                params.put("Maker", item.getMakerName());
                params.put("Price", String.valueOf(item.getItemPrice()));
                return params;
            }
        };
        stringRequest.setTag(REQ_TAG);
        requestQueue.add(stringRequest);
    }

    public void getJsonResponse(View view) {
        String url = "http://192.168.0.69:8080/androidHTTPTest/test.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, getJsonObjectListener(), getErrorListener());
        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }

    private Response.Listener<JSONObject> getJsonObjectListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                serverResp.setText("String Response : "+response.toString());
            }
        };
    }

    public void getJsonResponsePost(View view) {
        Item item = new Item("NoteBook2", "DELL2", 5000);

        JSONObject json = new JSONObject();
        try {
            json.put("Product", item.getItemName());
            json.put("Maker", item.getMakerName());
            json.put("Price", item.getItemPrice());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = "http://httpbin.org/post";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, getJsonObjectListener(), getErrorListener());
        jsonObjectRequest.setTag(REQ_TAG);
        requestQueue.add(jsonObjectRequest);
    }

    public void getImage(View view) {
        //방법1
        String url = "http://192.168.0.69:8080/androidHTTPTest/1.jpg";
        ImageRequest imageRequest = new ImageRequest(url, getBitMapListener(), 0, 0,null, null,getErrorListener());
        imageRequest.setTag(REQ_TAG);
        requestQueue.add(imageRequest);

        //방법2
        ImageLoader imageLoader = RequestQueueSingleton.getInstance(this).getImageLoader();
        networkImageView.setImageUrl(url, imageLoader);
    }

    private Response.Listener<Bitmap> getBitMapListener() {
        return new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                serverImg.setImageBitmap(response);
            }
        };
    }
}
