package com.socioboard.f_board_pro.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.CustomChoosePageLikeAdapter;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.ChoosePageFeedModel;
import com.socioboard.f_board_pro.models.ChoosePageLikeModel;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


public class ChoosePage extends Fragment
{
    private ArrayList<String> AllfeedIds;

    private ArrayList<String> AdminAccessTokenPage;

    String userFBiD = null;

    String userFBaccesToken = null;

    private List<ChoosePageLikeModel> likePageList = new ArrayList<ChoosePageLikeModel>();

    private CustomChoosePageLikeAdapter adapter;

    RequestQueue requestQueue;

    public ListView listData;

    View rootView;

    String cursor = null;

   ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.choosepage_fragment,container,false);

        listData = (ListView)rootView.findViewById(R.id.list);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        progressDialog = new ProgressDialog(getContext());
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);

        //adapter = new CustomChoosePageLikeAdapter(ChoosePage.this,likePageList);


        listData.setAdapter(adapter);

        AllfeedIds = new ArrayList<>();

        AdminAccessTokenPage = new ArrayList<>();

        getSharedPrefData(getApplicationContext());//get facebook accessToken





        // this is used to All feeds of Homepage
        listData.setClickable(true);

        listData.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView)view.findViewById(R.id.accessToken);

                String feedAccessToken = tv.getText().toString();

                //Toast.makeText(getApplicationContext(), feedAccessToken, Toast.LENGTH_LONG).show();

                new getHomeFeeds(feedAccessToken).execute();

//                if (userFBiD != null) {
//
//                    for (int i = 0; i < MainSingleTon.useraccesstokenlist.size(); i++) {
//                        new getPageFeed(pageid).execute(MainSingleTon.useraccesstokenlist.get(i));
//                    }
//                }
            }
        });
        return rootView;
    }

    public void getSharedPrefData(Context context) {

      SharedPreferences lifesharedpref = context.getSharedPreferences("FacebookBoardAutoliker", Context.MODE_PRIVATE);

        //userFBiD = lifesharedpref.getString("likescheduler_id", null);
        userFBiD = MainSingleTon.userid;

        System.out.println("userFbid======="+userFBiD);
        //userFBaccesToken = lifesharedpref.getString("likeScheluerAccesstoken", null);
        userFBaccesToken = MainSingleTon.accesstoken;

        System.out.println("userFBaccessToken======="+userFBaccesToken);
        new getAdminPage().execute();


//        if (userFBiD != null) {
//
//            for (int i = 0; i < MainSingleTon.useraccesstokenlist.size(); i++) {
//                new getPage().execute(MainSingleTon.useraccesstokenlist.get(i));
//            }
//        }

    }

    public class getAdminPage extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("loading.....");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            System.out.println("inside getPage class=======");
            //String tokenURL = "https://graph.facebook.com/me/likes?access_token=" + userFBaccesToken;
            String tokenURL = "https://graph.facebook.com/me/accounts?access_token=" + userFBaccesToken;

            System.out.println("GetAdminPage url= " + tokenURL);


            //This Volly is use to access only admin page of facebook

            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,tokenURL, null, new Response.Listener<JSONObject>(){
                public void onResponse(JSONObject response) {

                    //    System.out.println(""+response);
                    try {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject1=jsonArray.getJSONObject(i);

                            ChoosePageLikeModel choosePageLikeModel = new ChoosePageLikeModel();

                            choosePageLikeModel.setAccess_token(jsonObject1.getString("access_token"));

                            choosePageLikeModel.setCategory(jsonObject1.getString("category"));

                            choosePageLikeModel.setName(jsonObject1.getString("name"));

                            choosePageLikeModel.setId(jsonObject1.getString("id"));

                            AdminAccessTokenPage.add(choosePageLikeModel.getAccess_token());

                            likePageList.add(choosePageLikeModel);

                        }
                        System.out.println("AdminAccessTokenPage===="+AdminAccessTokenPage);

                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //listData.setAdapter(adapter);
                }
            }, new Response.ErrorListener()
            {
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                    0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjReq);
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
            progressDialog.dismiss();
        }
    }

    public class getHomeFeeds extends AsyncTask<String, Void, String>
    {


        public   String feedAccessToken;
        public getHomeFeeds(String feedAccessToken)
        {
            this.feedAccessToken = feedAccessToken;
        }

        @Override
        protected String doInBackground(String... params)
        {
            //System.out.println("inside getPageFeed");
            String tokenURL = "https://graph.facebook.com/me/home?access_token=" + userFBaccesToken;
            System.out.println("GetHomeFeeds URL= " + tokenURL);



            final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,tokenURL, null, new Response.Listener<JSONObject>(){
                public void onResponse(JSONObject response) {
                    //    System.out.println(""+response);
                    try
                    {
                        JSONArray jsonArray=response.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
                            ChoosePageFeedModel choosePageFeedModel = new ChoosePageFeedModel();
                            choosePageFeedModel.setFeedId(jsonObject2.getString("id"));
                            AllfeedIds.add(choosePageFeedModel.getFeedId());

                        }
                            System.out.println("AllfeedsIds==========="+AllfeedIds);

                            adapter.notifyDataSetChanged();

                            if (response.has("paging"))
                            {

                                JSONObject js56 = response.getJSONObject("paging");

                                if (js56.has("next"))
                                {
                                    cursor = js56.getString("next");
                                }
                                else
                                {

                                }
                            }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    //listData.setAdapter(adapter);
                }
            }, new Response.ErrorListener()
            {
                public void onErrorResponse(VolleyError error)
                {
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 48,
                1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjReq);

            return null;

        }
                @Override
        protected void onPostExecute(String s)
                {


            if(AllfeedIds!=null)
            {
//                Thread thread = new Thread(new Runnable() {

//                    @Override
//                    public void run() {
                        System.out.println("inside==========onPosExecute->Thread->run()");
                        if(isNetworkAvailable(getApplicationContext()))
                        {
                            for(int i=0;i<AllfeedIds.size();i++)
                            {
                                new CallToFBLikePage(feedAccessToken).execute(AllfeedIds.get(i));
                            }
                        }

//                    }
//                });
//                thread.start();

            }
            else
            {
                System.out.println("AllFeedsIds ara empty");
            }
            super.onPostExecute(s);
        }
    }




    public class CallToFBLikePage extends AsyncTask<String,Void,String>
    {
        String feedAccessTokenLike;

        public CallToFBLikePage(String feedAccessToken) {

            feedAccessTokenLike=feedAccessToken;
            //Toast.makeText(getApplicationContext(),"Like feeds.....",Toast.LENGTH_LONG).show();
        }


        @Override
        protected String doInBackground(String... params) {
            HttpResponse response;

            HttpClient httpclient = new DefaultHttpClient();

            String feedID = params[0];

            String URL;
            System.out.println("inside CallToFBLikepage============");
            System.out.println("new Feed Id-------------"+feedID);
            URL = "https://graph.facebook.com/" + feedID + "/likes";
            HttpPost httppost = new HttpPost(URL);
            try {

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

//                nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));
                nameValuePairs.add(new BasicNameValuePair("access_token",feedAccessTokenLike));
                System.out.println(".............LikeSchedulerReceiver.........."+feedAccessTokenLike);
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                response = httpclient.execute(httppost);

                System.out.println("response like......"+ response.getStatusLine()+"====>" + feedID);
//
//                sharedPreferences.edit().putInt("totallikerperday", perdaylikescount).commit();
//                sharedPreferences.edit().putInt("completed_likes", complted_count).commit();
//                sharedPreferences.edit().putString("pending_likes",(perdaylikescount - complted_count) + "").commit();

            } catch (ClientProtocolException e) {

            } catch (IOException e) {

            }

            return null;
        }


    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {
                    Log.i("Class", info[i].getState().toString());
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {

                        System.out.println("Network connection available");
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
