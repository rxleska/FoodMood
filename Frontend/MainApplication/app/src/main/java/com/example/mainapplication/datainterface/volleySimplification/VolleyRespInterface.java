package com.example.mainapplication.datainterface.volleySimplification;

/**
 * The interface Volley resp interface is used to create a callback for Volley requests.
 *
 * @author Ryan Leska
 */
public interface VolleyRespInterface {

    /**
     * onSuccess is meant to be called when a Volley request is finished.
     *
     * @param result the result of the Volley request
     *               (this is the result of the request, either an error String or a JSONObject)
     * */
    void onSuccess(String result);
}
