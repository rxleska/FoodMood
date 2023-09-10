package com.example.mainapplication.ui.notifications;

/**
 * The OnServerResponse interface is used to define the onSuccess method that is used to handle the response from the server.
 * This interface is no longer used and is deprecated.
 * */
@Deprecated
public interface OnServerResponse {
    /**
     * The onSuccess method is used to handle the response from the server. Used as a callback.
     * @param result The result of the server response. 0 = success, 1 = failure. or other error codes.
     * */
    void onSuccess(int result);
}
