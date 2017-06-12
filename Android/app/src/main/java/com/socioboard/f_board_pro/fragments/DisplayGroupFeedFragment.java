package com.socioboard.f_board_pro.fragments;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.socioboard.f_board_pro.MainActivity;
import com.socioboard.f_board_pro.R;
import com.socioboard.f_board_pro.adapter.CommentAdapter;
import com.socioboard.f_board_pro.database.util.JSONParseraa;
import com.socioboard.f_board_pro.database.util.MainSingleTon;
import com.socioboard.f_board_pro.models.CommentModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayGroupFeedFragment extends ListFragment
{
	View rootView;
	TextView peopleLikedYou,noComments;
	ArrayList<CommentModel> commentList;
	ProgressBar progressbar;
	ListView mGroupCommentList;
	ImageView like;
	boolean likestatus=false;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
	{
		rootView = inflater.inflate(R.layout.display_group_feed, container,false);
		
		progressbar=(ProgressBar) rootView.findViewById(R.id.progressBar1);
		
		progressbar.setVisibility(View.VISIBLE);
		
		noComments=(TextView) rootView.findViewById(R.id.no_comments);
		noComments.setText("No Comments");
		noComments.setVisibility(View.INVISIBLE);
		
		like=(ImageView) rootView.findViewById(R.id.like);
		like.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				if(likestatus)//Undo()    new CallToFbUnLike().execute(); 
					new CallToFbUnLike().execute(); 
				else
					new CallToFbLike().execute();
			}
		});
		peopleLikedYou=(TextView) rootView.findViewById(R.id.people_liked_you);
		peopleLikedYou.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MainActivity.fragmentManager =getFragmentManager();
						MainActivity.swipeFragment(new DisplayLikes());		
					}
				});
			}
		});
		commentList=new ArrayList<CommentModel>();
		checkLike();
		commentList.clear();
		new GetGroupFeed().execute();
		
		return rootView;
	}
	public void  checkLike() 
	{
		if(MainSingleTon.selectedGroupFeed.getLikes()>0)
		{
			peopleLikedYou.setText(MainSingleTon.selectedGroupFeed.getLikes()+" people like this");
			like.setBackgroundResource(R.drawable.unlike);
			for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
			{
				if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedGroupFeed.getFeedID()))
				{
					if(MainSingleTon.userLikedFeedList.get(i).isLike())
					{
						like.setBackgroundResource(R.drawable.like);
						likestatus=true;
						if(MainSingleTon.selectedGroupFeed.getLikes()==1)
						{
							peopleLikedYou.setText("You like this ");
						}
						else if(MainSingleTon.selectedGroupFeed.getLikes()==2)
						{
							peopleLikedYou.setText("You and other "+(MainSingleTon.selectedGroupFeed.getLikes()-1)+" person like this");
						}
						else
						{
							peopleLikedYou.setText("You and other "+(MainSingleTon.selectedGroupFeed.getLikes()-1)+" people like this");
						}
					}
					else
					{
						likestatus=false;
						peopleLikedYou.setText(""+MainSingleTon.selectedGroupFeed.getLikes()+" people like this");
					}
				}
			}
		}
		else
		{
			like.setBackgroundResource(R.drawable.unlike);
			peopleLikedYou.setText("Be the first to like this");
			
		}
	}
	public void setUnlike() 
	{
		if(MainSingleTon.selectedGroupFeed.getLikes()>0)
		{

			like.setBackgroundResource(R.drawable.unlike);
			for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
			{
				if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedGroupFeed.getFeedID()))
				{
					MainSingleTon.selectedGroupFeed.setLikes(MainSingleTon.selectedGroupFeed.getLikes()-1);
					MainSingleTon.userLikedFeedList.get(i).setLike(false);
					likestatus=false;
				}
			}
		}		
	}
	public void setLike()
	{
			like.setBackgroundResource(R.drawable.like);
			for (int i = 0; i < MainSingleTon.userLikedFeedList.size(); i++) 
			{
				if(MainSingleTon.userLikedFeedList.get(i).getFeedId().equalsIgnoreCase(MainSingleTon.selectedGroupFeed.getFeedID()))
				{
					MainSingleTon.selectedGroupFeed.setLikes(MainSingleTon.selectedGroupFeed.getLikes()+1);
					MainSingleTon.userLikedFeedList.get(i).setLike(true);
					likestatus=true;
				}
			}
	}
	public void Undo() 
	{
		Bundle params = new Bundle();
		params.putString(AccessToken.ACCESS_TOKEN_KEY, MainSingleTon.accesstoken);
		
		new GraphRequest( null,"/"+MainSingleTon.selectedGroupFeed+"/"+"likes", params, HttpMethod.DELETE, new GraphRequest.Callback() {
			
			@Override
			public void onCompleted(GraphResponse response) 
			{
			System.out.println("Responce in new delete");
				
			}
		});
		
		
	}
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{

		mGroupCommentList = getListView();

		mGroupCommentList.setVisibility(View.INVISIBLE);
	}
	public class GetGroupFeed extends AsyncTask<Void, Void, String>
	{
		String groupFeedId =null;
		String userFBaccesToken = null;
		String type = null;

		@Override
		protected String doInBackground(Void... params) 
		{

			userFBaccesToken = MainSingleTon.accesstoken;
			groupFeedId = MainSingleTon.selectedGroupFeed.getFeedID();
			commentList.clear();

			String tokenURL = "https://graph.facebook.com/"+groupFeedId+"/comments?access_token="+userFBaccesToken;

			JSONParseraa jsonParser = new JSONParseraa();

			JSONObject jsonObject = jsonParser.getJSONFromUrl(tokenURL);

			try {

					JSONArray jsonArray =  jsonObject.getJSONArray("data");
					
					for(int i = 0; i<jsonArray.length();i++)
					{
						CommentModel commentModel=new CommentModel();
						JSONObject jsonObject2 = jsonArray.getJSONObject(i);
	
						if(jsonObject2.has("from"))
						{
							
							JSONObject jsonObject3 = jsonObject2.getJSONObject("from");
							if(jsonObject3.has("name"))
							{
								commentModel.setName(jsonObject3.getString("name"));								
							}
							if(jsonObject3.has("id"))
							{
								String id=null;
								commentModel.setFromID(id=jsonObject3.getString("id"));	
								commentModel.setProfilePic("https://graph.facebook.com/"+id+"/picture?type=small");							
							}
						}
						if(jsonObject2.has("message"))
						{
							commentModel.setComment(jsonObject2.getString("message"));	
						}
						if(jsonObject2.has("created_time"))
						{
							commentModel.setDateTime(jsonObject2.getString("created_time"));	
						}
						
						commentList.add(commentModel);
						
						
					} 
					
					getActivity().runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
									if(commentList.size()>0)
									{
										CommentAdapter commentAdapter = new CommentAdapter(getActivity(), commentList);
										setListAdapter(commentAdapter);
										progressbar.setVisibility(View.INVISIBLE);
										mGroupCommentList.setVisibility(View.VISIBLE);
									}
									else
									{
										progressbar.setVisibility(View.INVISIBLE);
										mGroupCommentList.setVisibility(View.INVISIBLE);
										noComments.setVisibility(View.VISIBLE);
									}
						}
					});
			}
			catch (JSONException e) 
			{
				
				e.printStackTrace();
			}

			return null;
		}

	}
	
	/*class to like a post*/
	public class CallToFbLike extends AsyncTask<String, Void, String>
	{

		  HttpResponse response;

		  @Override
		  protected String doInBackground(String... params)
			  {
	
	
			   HttpClient httpclient = new DefaultHttpClient();
	
			   String URL = "https://graph.facebook.com/"+ MainSingleTon.selectedGroupFeed.getFeedID() + "/likes";
	
			   HttpPost httppost = new HttpPost(URL);
	
			   try 
			   {
				    // Add your data
				    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				    nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
				    // Execute HTTP Post Request
				    response = httpclient.execute(httppost);
		
	
			   } 
			   catch (ClientProtocolException e)
			   {
				   // TODO Auto-generated catch block
			   } catch (IOException e)
			   {
				   // TODO Auto-generated catch block
			   }
			   return null;
		  }

		  @Override
		  protected void onPostExecute(String result)
		  {
			   // TODO Auto-generated method stub
			   super.onPostExecute(result);
	
			   if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			   {
				   Toast.makeText(getActivity(), "you like this", Toast.LENGTH_SHORT).show();
				   setLike();
				   checkLike();
				  
			   }
			   else
			   {
				   Toast.makeText(getActivity(), "please try after some time", Toast.LENGTH_SHORT).show();
			   }
		  }
		 }
	public class CallToFbUnLike extends AsyncTask<String, Void, String>
	{
		  HttpResponse response;

		  @Override
		  protected String doInBackground(String... params)
			  {
	
			  HttpClient httpclient = new DefaultHttpClient();
				
			   String URL = "https://graph.facebook.com/"+ MainSingleTon.selectedGroupFeed.getFeedID()+ "/likes";
	
			   HttpPost httppost = new HttpPost(URL);
	
			   try 
			   {
				    // Add your data
				    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				    nameValuePairs.add(new BasicNameValuePair("access_token",MainSingleTon.accesstoken));
				    nameValuePairs.add(new BasicNameValuePair("method","DELETE"));
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
				   
				    // Execute HTTP Post Request
				    response = httpclient.execute(httppost);
				    
				    System.out.println("response unlke......"+response.getStatusLine());
	
			   } 
			   catch (ClientProtocolException e)
			   {
				   // TODO Auto-generated catch block
			   } catch (IOException e)
			   {
				   // TODO Auto-generated catch block
			   }
			   return null;
		  }

		  @Override
		  protected void onPostExecute(String result)
		  {
			   // TODO Auto-generated method stub
			   super.onPostExecute(result);
	
			   if (response.getStatusLine().toString().equals("HTTP/1.1 200 OK"))
			   {
				   Toast.makeText(getActivity(), "you Unlike this", Toast.LENGTH_SHORT).show();
				   setUnlike();
				   checkLike();
				  
			   }
			   else
			   {
				   Toast.makeText(getActivity(), "please try after some time", Toast.LENGTH_SHORT).show();
			   }
		  }
		 }
	
	
}
