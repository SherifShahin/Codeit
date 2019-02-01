package codeit.com.codeit.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;

import java.util.List;

import codeit.com.codeit.Adapter.ProfileLikesAdapter;
import codeit.com.codeit.Model.LikesResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import codeit.com.codeit.ViewModel.LikesViewModel;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_Likes extends AppCompatActivity
{
    private String username;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private ProfileLikesAdapter adapter;

    private LikesViewModel  viewModel;
    private LiveData<List<LikesResponseBody>> listLiveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__likes);

        username=getIntent().getStringExtra("username");
        swipeRefreshLayout=findViewById(R.id.profile_likes_swipeRefreshLayout);
        recyclerView=findViewById(R.id.profile_likes_recycleview);

        viewModel= ViewModelProviders.of(this).get(LikesViewModel.class);

        viewModel.RequestLikes(this,username);

        listLiveData=viewModel.getLikes();

        listLiveData.observe(this, new Observer<List<LikesResponseBody>>() {
            @Override
            public void onChanged(@Nullable List<LikesResponseBody> likesResponseBodies) {
                adapter=new ProfileLikesAdapter(likesResponseBodies,getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                viewModel.RequestLikes(getApplicationContext(),username);
                swipeRefreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
