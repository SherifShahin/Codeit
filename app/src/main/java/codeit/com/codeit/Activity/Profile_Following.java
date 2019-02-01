package codeit.com.codeit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import codeit.com.codeit.Adapter.ProfileFollowersAdapter;
import codeit.com.codeit.Adapter.ProfileFollowingAdapter;
import codeit.com.codeit.Model.ProfileFollowerResponseBody;
import codeit.com.codeit.Model.ProfileFollowingResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Following extends AppCompatActivity {

    private String username;
    private RecyclerView recyclerView;
    private SharedPrefManger sharedPrefManger;
    private ProfileFollowingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__following);

        username=getIntent().getStringExtra("username");
        recyclerView=findViewById(R.id.profile_following_recycleview);

        sharedPrefManger=SharedPrefManger.getInstance(this);

        Call<List<ProfileFollowingResponseBody>> call= Common.getApi().getFollowing(sharedPrefManger.getdata().getToken(),username);

        call.enqueue(new Callback<List<ProfileFollowingResponseBody>>()
        {
            @Override
            public void onResponse(Call<List<ProfileFollowingResponseBody>> call, Response<List<ProfileFollowingResponseBody>> response)
            {
                if(response.code() == 200)
                {
                    List<ProfileFollowingResponseBody> list=response.body();

                    Log.e("list",""+list.size());
                    if(!list.isEmpty()) {
                        adapter = new ProfileFollowingAdapter(list,getApplicationContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ProfileFollowingResponseBody>> call, Throwable t) { }
        });
    }
}
