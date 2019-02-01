package codeit.com.codeit.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Adapter.VisitedProfileAdapter;
import codeit.com.codeit.Model.FollowResponse;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import codeit.com.codeit.ViewModel.VisitedProfilePostsViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class Visited_Profile extends AppCompatActivity implements View.OnClickListener
{
    private ProfileResponseBody profiledata;
    private String isFollowing;
    private String name;
    private String uusername;

    private SwipeRefreshLayout visited_profile_swiSwipeRefreshLayout;
    private CircleImageView userImage;
    private TextView username;
    private TextView Likesno;
    private TextView postsno;
    private TextView followersno;
    private TextView followingno;
    private Button isFollowing_bt;
    private LinearLayout likes_layout;
    private LinearLayout followers_layout;
    private LinearLayout following_layout;

    private RecyclerView recyclerView;
    private VisitedProfileAdapter visitedProfileAdapter;

    private SharedPrefManger sharedPrefManger;

    private VisitedProfilePostsViewModel viewModel;
    private LiveData<List<Post>> listLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited__profile);

        profiledata= (ProfileResponseBody) getIntent().getSerializableExtra("userData");
        isFollowing=getIntent().getStringExtra("isFollowing");
        name=getIntent().getStringExtra("name");
        uusername=getIntent().getStringExtra("username");

        visited_profile_swiSwipeRefreshLayout=findViewById(R.id.visitef_profile_swipeRefresh);
        userImage=findViewById(R.id.visited_profile_image);
        username=findViewById(R.id.visited_profile_name);
        Likesno=findViewById(R.id.visited_profile_likes_number);
        postsno=findViewById(R.id.visited_profile_posts_number);
        followersno=findViewById(R.id.visited_profile_followers_number);
        followingno=findViewById(R.id.visited_profile_following_number);
        isFollowing_bt=findViewById(R.id.visited_profile_follow_bt);
        likes_layout=findViewById(R.id.visited_profile_likes_layout);
        followers_layout=findViewById(R.id.visited_profile_followers_layout);
        following_layout=findViewById(R.id.visited_profile_following_layout);

        isFollowing_bt.setOnClickListener(this);
        likes_layout.setOnClickListener(this);
        following_layout.setOnClickListener(this);
        followers_layout.setOnClickListener(this);

        recyclerView=findViewById(R.id.visited_profile_recycleview);

        sharedPrefManger=SharedPrefManger.getInstance(this);

        readyData();


        viewModel= ViewModelProviders.of(this).get(VisitedProfilePostsViewModel.class);

        viewModel.RequestPosts(this,uusername);

        listLiveData=viewModel.getPosts();

        listLiveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {

                visitedProfileAdapter=new VisitedProfileAdapter(getApplicationContext(),posts);
                recyclerView.setAdapter(visitedProfileAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL, false));
            }
        });


        visited_profile_swiSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                readyData();
                viewModel.RequestPosts(getApplicationContext(),uusername);
                visitedProfileAdapter.notifyDataSetChanged();
                visited_profile_swiSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }


    private void readyData()
    {
        String img;
        img=profiledata.getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.mipmap.profile_default_img).into(userImage);
        username.setText(name);
        Likesno.setText(profiledata.getLikes());
        postsno.setText(profiledata.getPosts());
        followingno.setText(profiledata.getFollowing());
        followersno.setText(profiledata.getFollowers());


        if(uusername.equals(sharedPrefManger.getdata().getUsername()))
        {
            isFollowing_bt.setVisibility(View.INVISIBLE);
        }
        else {
            if (isFollowing.equals("false")) {
                isFollowing_bt.setText("Follow");
                isFollowing_bt.setBackgroundResource(R.drawable.signup_bt_shape);
            } else {
                isFollowing_bt.setText("Following");
                isFollowing_bt.setBackgroundResource(R.drawable.login_bt_shape);
            }
        }
    }



    @Override
    public void onClick(View view)
    {

        if(view == likes_layout)
        {
            Intent intent=new Intent(getApplicationContext(), Profile_Likes.class);

            intent.putExtra("username",uusername);

            startActivity(intent);
        }

        if(view == followers_layout)
        {

            Intent intent =new Intent(getApplicationContext(),Profile_Followers.class);

            intent.putExtra("username",uusername);

            startActivity(intent);
        }

        if(view == following_layout)
        {
            Intent intent =new Intent(getApplicationContext(),Profile_Following.class);

            intent.putExtra("username",uusername);

            startActivity(intent);
        }

        if(view == isFollowing_bt)
        {

            if(isFollowing_bt.getText().equals("Following")) {
                retrofit2.Call<FollowResponse> call = Common.getApi().UnFollow(sharedPrefManger.getdata().getToken()
                        , sharedPrefManger.getdata().getUsername(),
                        uusername);

                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<FollowResponse> call, Response<FollowResponse> response) {

                        if (response.code() == 200) {

                            FollowResponse followResponse=response.body();
                            followersno.setText(followResponse.getFollowers());
                            isFollowing_bt.setText("Follow");
                            isFollowing_bt.setBackgroundResource(R.drawable.signup_bt_shape);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<FollowResponse> call, Throwable t) {}
                });

            }

            else
            {
                retrofit2.Call<FollowResponse> call=Common.getApi().Follow(sharedPrefManger.getdata().getToken()
                        , sharedPrefManger.getdata().getUsername(),
                        uusername);

                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(retrofit2.Call<FollowResponse> call, Response<FollowResponse> response) {
                        if(response.code() == 200)
                        {
                            FollowResponse followResponse=response.body();
                            followersno.setText(followResponse.getFollowers());
                            isFollowing_bt.setText("Following");
                            isFollowing_bt.setBackgroundResource(R.drawable.login_bt_shape);
                        }
                        else
                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<FollowResponse> call, Throwable t) {}
                });
            }
        }
    }
}
