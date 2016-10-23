package com.smk.simplesearchable.clients;

import android.util.Log;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyErrorHandler implements ErrorHandler {
	
	public Throwable handleError(RetrofitError cause) {
	    Response r = cause.getResponse();
	    if (r != null) {
	    	Log.d("Retrofix","Reponse Status: "+ r.getStatus() +" , Reason: "+r.getReason());
	    }
	    return cause;
	}
}