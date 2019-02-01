package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import java.util.List;

import codeit.com.codeit.Adapter.Profile_posts_adapter;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfilePostsViewModel extends ViewModel
{
    private MutableLiveData<List<Post>>  posts;
    private Context context;

    public MutableLiveData<List<Post>> getPosts()
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
        retrofit2.Call<List<Post>> call= Common.getApi().getPosts(SharedPrefManger.getInstance(context).get_token(),
                SharedPrefManger.getInstance(context).getdata().getUsername());

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Post>> call, Response<List<Post>> response)
            {
                if(response.code()== 200)
                {
                    posts.setValue(response.body());
                }

                else
                {
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Post>> call, Throwable t)
            {

            }
        });






    }


}
