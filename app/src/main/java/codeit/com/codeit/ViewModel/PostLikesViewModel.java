package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import java.util.List;

import codeit.com.codeit.Model.LikesResponseBody;
import codeit.com.codeit.Model.PostLikesResponseBody;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLikesViewModel  extends ViewModel
{
    MutableLiveData<List<PostLikesResponseBody>> likes;
    private Context context;
    private String id;
    private SharedPrefManger sharedPrefManger;

    public MutableLiveData<List<PostLikesResponseBody>> getLikes()
    {
        if(likes == null)
        {
            likes=new MutableLiveData<>();

            RequestLikes(context,id);
        }
        return likes;
    }

    public  void RequestLikes(Context context, String id)
    {
        sharedPrefManger=SharedPrefManger.getInstance(context);
        Call<List<PostLikesResponseBody>> call= Common.getApi().getPostLikes(sharedPrefManger.getdata().getToken()
        ,id
        );

        call.enqueue(new Callback<List<PostLikesResponseBody>>() {
            @Override
            public void onResponse(Call<List<PostLikesResponseBody>> call, Response<List<PostLikesResponseBody>> response)
            {
                if(response.code() == 200)
                {
                 likes.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<PostLikesResponseBody>> call, Throwable t) {

            }
        });
    }


}
