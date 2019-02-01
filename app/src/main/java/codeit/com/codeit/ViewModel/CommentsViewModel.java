package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.telecom.Call;

import java.util.List;

import codeit.com.codeit.Model.Comments_ResponseBody;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsViewModel extends ViewModel
{
    private MutableLiveData<List<Comments_ResponseBody>> comments;
    private Context context;
    private String post_id;



    public MutableLiveData<List<Comments_ResponseBody>> getComments()
    {
        if(comments == null)
        {
            comments=new MutableLiveData<>();

            RequestComments(context,post_id);
        }
        return comments;
    }

    public void RequestComments(final Context context, String id)
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);
        retrofit2.Call<List<Comments_ResponseBody>> call= Common.getApi().getcomments(sharedPrefManger.getdata().getToken(),
                sharedPrefManger.getdata().getUsername(),
                id);


        call.enqueue(new Callback<List<Comments_ResponseBody>>() {
            @Override
            public void onResponse(retrofit2.Call<List<Comments_ResponseBody>> call, Response<List<Comments_ResponseBody>> response)
            {
                if(response.code() == 200)
                {
                    comments.setValue(response.body());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<List<Comments_ResponseBody>> call, Throwable t) {

            }
        });


    }


}
