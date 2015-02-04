package com.collaborativeClouds.PA.serverWorkers;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.collaborativeClouds.PA.configs.Config;

import android.util.Log;

import java.io.IOException;

/**
 * Created by anoojkrishnang on 14/12/14.
 */
public class HttpRequestWorker {


    public String GetRequest(String url){

        try {
            HttpClient mClient = new DefaultHttpClient();
            HttpGet mHttpGet = new HttpGet(url);
            mHttpGet.setHeader("username", Config.USERNAME);
            ResponseHandler<String> mResponseHandler = new BasicResponseHandler();
            String mStatus = mClient.execute(mHttpGet, mResponseHandler);
            Log.e("RESPONSE",""+mStatus);
            return mStatus;
        }catch (Exception ex){
            return "Failed "+ex;
        }
    }
    
    public String PostRequest(String url,String content,boolean isHeaderRequired){
    	try {
    		String mStatus=null;
    		DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost mHttpPost = new HttpPost(url);
            StringEntity se = new StringEntity(content);
            se.setContentType("application/json");
            
            mHttpPost.setEntity(se);
            if(isHeaderRequired){
            	mHttpPost.setHeader("username", Config.USERNAME);
            }
            
            //mHttpPost.setEntity(entity)
            HttpResponse httpresponse = httpclient.execute(mHttpPost);
            mStatus	= EntityUtils.toString(httpresponse.getEntity());
            //ResponseHandler<String> mResponseHandler = new BasicResponseHandler();
            //String mStatus = mClient.execute(mHttpPost, mResponseHandler);
            Log.e("RESPONSE",""+mStatus);
            
            return mStatus;
        }catch (Exception ex){
            return "Failed "+ex;
        }
    }
}
