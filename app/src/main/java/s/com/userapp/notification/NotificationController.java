package s.com.userapp.notification;

import android.content.Context;
import android.util.Log;

import androidx.navigation.NavOptions;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import s.com.userapp.Utils.Constants;

public class NotificationController {

    NotificationView notificationView;
    Context context;

    public NotificationController(Context context,NotificationView notificationView) {
        this.notificationView = notificationView;
        this.context=context;
    }


    public void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("TAG", "onResponse: " + response.toString());

                        notificationView.onNotificationSend(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("TAG", "onErrorResponse: Didn't work");
                           notificationView.onNotificationFail(error.getMessage());
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constants.serverKey);
                params.put("Content-Type", Constants.contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
    public interface NotificationView
    {
        void onNotificationSend(String msg);
        void onNotificationFail(String msg);
    }
}
