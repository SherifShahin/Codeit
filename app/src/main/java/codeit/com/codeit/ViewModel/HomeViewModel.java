package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;


import java.util.List;

import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel
{

   private MutableLiveData<List<HomePosts_Model>>  posts;
   private Context context;
   private SharedPrefManger sharedPrefManger;

    public MutableLiveData<List<HomePosts_Model>> getPosts()
    {
        if(posts == null)
        {
            posts=new MutableLiveData<>();

                RequestPosts(context);
        }
        return posts;
    }

    public void RequestPosts(final Context context)
    {
        sharedPrefManger=SharedPrefManger.getInstance(context);
        retrofit2.Call<List<HomePosts_Model>> call= Common.getApi().getHomePosts(sharedPrefManger.getdata().getToken(),sharedPrefManger.getdata().getUsername());

        call.enqueue(new Callback<List<HomePosts_Model>>() {
            @Override
            public void onResponse(retrofit2.Call<List<HomePosts_Model>> call, Response<List<HomePosts_Model>> response) {
                if(response.code() == 200)
                {
                    posts.setValue(response.body());
                    sharedPrefManger.set_post(response.body());
                    sharedPrefManger.setHavePosts();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<HomePosts_Model>> call, Throwable t)
            {

            }
        });

    }


}
