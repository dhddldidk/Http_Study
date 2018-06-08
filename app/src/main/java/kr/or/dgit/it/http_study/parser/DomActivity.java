package kr.or.dgit.it.http_study.parser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import kr.or.dgit.it.http_study.R;
import kr.or.dgit.it.http_study.volley.RequestQueueSingleton;

public class DomActivity extends AppCompatActivity {

    public static final String REQ_TAG = "DomActivity";

    private RecyclerView recyclerView;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle(getIntent().getStringExtra("title"));

        recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        requestQueue = RequestQueueSingleton.getInstance(this).getRequestQueue();

        getStringRequest();
    }

    private void getStringRequest() {
        String url = "http://192.168.0.69:8080/androidHTTPTest/order.xml";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, listener, errorListener);
        stringRequest.setTag(REQ_TAG);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue !=null){
            requestQueue.cancelAll(REQ_TAG);
        }
    }



    public ArrayList<Item> parsingXml(String xml) {
        Log.d("TAG", "parsingXml: " + xml);
        ArrayList<Item> arItems = new ArrayList<>();
        Item itemClass;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"));
            Document doc = builder.parse(istream);
            Element order = doc.getDocumentElement();
            NodeList items = order.getElementsByTagName("item");
            for (int i = 0; i < items.getLength(); i++) {
                itemClass = new Item();
                Node item = items.item(i);
                Node text = item.getFirstChild();
                itemClass.setItemName(text.getNodeValue());
                NamedNodeMap Attrs = item.getAttributes();
                for (int j = 0; j < Attrs.getLength(); j++) {
                    Node attr = Attrs.item(j);
                    if (attr.getNodeName().equalsIgnoreCase("price")) {
                        itemClass.setItemPrice(Integer.parseInt(attr.getNodeValue()));
                    } else {
                        itemClass.setMakerName(attr.getNodeValue());
                    }
                }
                arItems.add(itemClass);
            }
        } catch (Exception e) { Log.i("Dom_Parser", e.getMessage());  }
        Log.d("TAG", "parsingXml: " + arItems);
        return arItems;
    }

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            recyclerView.setAdapter(new RecyclerAdapter(parsingXml(response)));
        }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {}
    };
}
