package kr.or.dgit.it.http_study.parser;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kr.or.dgit.it.http_study.R;
import kr.or.dgit.it.http_study.volley.RequestQueueSingleton;

public class SaxActivity extends AppCompatActivity {

    public static final String REQ_TAG = "SaxActivity";

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

    Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            recyclerView.setAdapter(new RecyclerAdapter(parsingXml(response)));
        }
    };

    public ArrayList<Item> parsingXml(String xml) {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SaxHandler handler = null;

        try {
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            handler = new SaxHandler();
            reader.setContentHandler(handler);
            try(InputStream istream = new ByteArrayInputStream(xml.getBytes("utf-8"))){
                reader.parse(new InputSource(istream));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.arItem;

    }

    private class SaxHandler extends DefaultHandler {
        boolean initem = false;
        Item itemClass;
        ArrayList<Item> arItem = new ArrayList<Item>();

        public void startDocument() {}

        public void endDocument() {}

        public void startElement(String uri, String localName, String qName, Attributes atts) {
            if (localName.equals("item")) {
                initem = true;
                itemClass = new Item();
            }

            if (atts.getLength() > 0) {
                for (int i = 0; i < atts.getLength(); i++) {
                    if (atts.getLocalName(i).equalsIgnoreCase("price")) {
                        itemClass.setItemPrice(Integer.parseInt(atts.getValue(i)));
                    } else {
                        itemClass.setMakerName(atts.getValue(i));
                    }
                }
            }
        }

        public void endElement(String uri, String localName, String qName) {}

        public void characters(char[] chars, int start, int length) {
            if (initem) {
                itemClass.setItemName(new String(chars, start, length).toString());
                arItem.add(itemClass);
                initem = false;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(requestQueue !=null){
            requestQueue.cancelAll(REQ_TAG);
        }
    }


    Response.ErrorListener errorListener = new Response.ErrorListener(){
        @Override
        public void onErrorResponse(VolleyError error) {}
    };

}
