package codeit.com.codeit.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import codeit.com.codeit.Adapter.ProfileFollowersAdapter;
import codeit.com.codeit.Model.ProfileFollowerResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Followers extends AppCompatActivity {

    private String username;
    private RecyclerView recyclerView;
    private SharedPrefManger sharedPrefManger;
    private ProfileFollowersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__followers);

        username=getIntent().getStringExtra("username");
        recyclerView=findViewById(R.id.profile_followers_recycleview);

        sharedPrefManger=SharedPrefManger.getInstance(this);

        Call<List<ProfileFollowerResponseBody>> call= Common.getApi().getFollowers(sharedPrefManger.getdata().getToken(),username);

        call.enqueue(new Callback<List<ProfileFollowerResponseBody>>() {
            @Override
            public void onResponse(Call<List<ProfileFollowerResponseBody>> call, Response<List<ProfileFollowerResponseBody>> response)
            {
                if(response.code() == 200)
                {
                    List<ProfileFollowerResponseBody> list=response.body();

                    adapter=new ProfileFollowersAdapter(list,getApplicationContext());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                }
            }

            @Override
            public void onFailure(Call<List<ProfileFollowerResponseBody>> call, Throwable t) {
            }
        });




    }
}
