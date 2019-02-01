package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import codeit.com.codeit.Adapter.ProfileLikesAdapter;
import codeit.com.codeit.Model.Comments_ResponseBody;
import codeit.com.codeit.Model.LikesResponseBody;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Callback;
import retrofit2.Response;

public class LikesViewModel extends ViewModel
{
    MutableLiveData<List<LikesResponseBody>> likes;
    private Context context;
    private String username;
    private SharedPrefManger sharedPrefManger;

    public MutableLiveData<List<LikesResponseBody>> getLikes()
    {
        if(likes == null)
        {
            likes=new MutableLiveData<>();

            RequestLikes(context,username);
        }
        return likes;
    }

    public void RequestLikes(Context context, String username)
    {
        sharedPrefManger= SharedPrefManger.getInstance(context);

        retrofit2.Call<List<LikesResponseBody>> call= Common.getApi().getLikes(sharedPrefManger.getdata().getToken(),username);

        call.enqueue(new Callback<List<LikesResponseBody>>() {
            @Override
            public void onResponse(retrofit2.Call<List<LikesResponseBody>> call, Response<List<LikesResponseBody>> response)
            {
                if(response.code() == 200)
                {
                    likes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<LikesResponseBody>> call, Throwable t) {

            }
        });
    }
}
