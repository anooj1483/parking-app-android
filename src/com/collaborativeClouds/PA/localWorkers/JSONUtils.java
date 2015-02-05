package com.collaborativeClouds.PA.localWorkers;

import com.google.gson.Gson;

public final class JSONUtils {
	  private static final Gson gson = new Gson();

	  public JSONUtils(){}

	  public boolean isJSONValid(String JSON_STRING) {
	      try {
	          gson.fromJson(JSON_STRING, Object.class);
	          return true;
	      } catch(com.google.gson.JsonSyntaxException ex) { 
	          return false;
	      }
	  }
	}
