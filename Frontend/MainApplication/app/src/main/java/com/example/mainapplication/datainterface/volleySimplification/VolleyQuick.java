package com.example.mainapplication.datainterface.volleySimplification;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

//* lambda expression example: (response) -> {log.e(response)} // note response is always a string either of the response or "error"
/**
 * This class is used to make volley requests quick and simplified
 *
 * This class implements the VolleyRespInterface interface which is used as a callback for when the request is finished.
 *
 * @implNote A volleyRespInterface callback can be simplified by using a lambda expression (this is optional but recommended as it is cleaner)
 *
 * @author Ryan Leska
 *
 * */
public class VolleyQuick {
    // This class is used to quickly make volley requests

//    String url = "http://coms-309-012.class.las.iastate.edu:8080";

    /**
     * This is the url that is used for all requests,
     * this can be changed to a different url if needed,
     * currently it is set to the static ip of the server
     * */
    String url = "http://10.90.74.141:8080";

    /**
     * This method is used to make a get request
     *
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    public StringRequest vGet(String url, final VolleyRespInterface volleyRespInterface){
                              // This method is used to make a get request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("’", "'");
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        });
        return stringRequest;
    }

    /**
     * This method is used to make a get request with a json object
     * @deprecated This method is deprecated and will be removed in the future
     * Reason for deprecation: Get requests should not have a body
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @param jsonObject the json object that is being sent with the request
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    @Deprecated
    public StringRequest vGet(String url, final VolleyRespInterface volleyRespInterface, JSONObject jsonObject){
        // This method is used to make a post request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("’", "'");
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        }){
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        return stringRequest;
    }

    /**
     * This method is used to make a post request
     *
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @param jsonObject the json object that is being sent with the request
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    public StringRequest vPost(String url, final VolleyRespInterface volleyRespInterface, JSONObject jsonObject){

        // This method is used to make a post request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replaceAll("’", "'");
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        }){
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        return stringRequest;
    }

    /**
     * This method is used to make a put request
     *
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @param jsonObject the json object that is being sent with the request
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    public StringRequest vPut(String url, final VolleyRespInterface volleyRespInterface, JSONObject jsonObject){
        // This method is used to make a put request
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        }){
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        return stringRequest;
    }

    /**
     * This method is used to make a delete request
     *
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @param jsonObject the json object that is being sent with the request
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    public StringRequest vDelete(String url, final VolleyRespInterface volleyRespInterface, JSONObject jsonObject){
        // This method is used to make a delete request
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        }){
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        return stringRequest;
    }

    /**
     * This method is used to make a get request
     *
     * @param url the url that is being requested
     * @param volleyRespInterface the callback that is used when the request is finished
     * @return a StringRequest that can be used to make a request // This must be added to a volley queue to be processed // this is done in middleman class
     * */
    public StringRequest vDelete(String url, final VolleyRespInterface volleyRespInterface){
        // This method is used to make a delete request
        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, this.url + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                volleyRespInterface.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY",error.toString());
                volleyRespInterface.onSuccess("error");
            }
        });
        return stringRequest;
    }
}
