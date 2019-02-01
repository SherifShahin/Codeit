package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.List;

import codeit.com.codeit.Adapter.NotificationsAdapter;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Model.NotificationsResponseBody;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsViewModel extends ViewModel
{
    private MutableLiveData<List<NotificationsResponseBody>> notifications;
    private Context context;

    public MutableLiveData<List<NotificationsResponseBody>> getNotificaitons()
    {
        if(notifications == null)
        {
            notifications=new MutableLiveData<>();

            RequestNotifications(context);
        }
        return notifications;
    }


    public void RequestNotifications(final Context context)
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);
        retrofit2.Call<List<NotificationsResponseBody>> call= Common.getApi().getNotifications(sharedPrefManger.getdata().getToken()
                ,sharedPrefManger.getdata().getUsername()
        );

        call.enqueue(new Callback<List<NotificationsResponseBody>>() {
            @Override
            public void onResponse(retrofit2.Call<List<NotificationsResponseBody>> call, Response<List<NotificationsResponseBody>> response)
            {
                if(response.code() == 200)
                {
                    notifications.setValue(response.body());
                }
                else
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(retrofit2.Call<List<NotificationsResponseBody>> call, Throwable t) {

            }
        });

    }


}
