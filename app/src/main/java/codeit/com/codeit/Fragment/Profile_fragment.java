package codeit.com.codeit.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import codeit.com.codeit.Activity.Profile_Followers;
import codeit.com.codeit.Activity.Profile_Following;
import codeit.com.codeit.Activity.Profile_Likes;
import codeit.com.codeit.Adapter.ViewPagerAdapter;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.Model.User_Data;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Profile_fragment extends Fragment implements View.OnClickListener
{
    private SwipeRefreshLayout profileSwipeRefreshLayout;
    private CircleImageView profile_image;
    private TextView profile_name;
    private TextView post_no;
    private TextView likes_no;
    private TextView follower_no;
    private TextView following_no;

    private LinearLayout likes_layout;
    private LinearLayout followers_layout;
    private LinearLayout following_layout;

    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;

    private SharedPrefManger sharedPrefManger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.profile_fragment, container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPrefManger=SharedPrefManger.getInstance(getContext());

        profileSwipeRefreshLayout=view.findViewById(R.id.profile_swipeRefresh);
        profile_image=view.findViewById(R.id.profile_image);
        profile_name=view.findViewById(R.id.profile_name);
        post_no=view.findViewById(R.id.profile_posts_number);
        likes_no=view.findViewById(R.id.profile_likes_number);
        follower_no=view.findViewById(R.id.profile_followers_number);
        following_no=view.findViewById(R.id.profile_following_number);
        likes_layout=view.findViewById(R.id.profile_likes_layout);
        followers_layout=view.findViewById(R.id.profile_followers_layout);
        following_layout=view.findViewById(R.id.profile_following_layout);


        likes_layout.setOnClickListener(this);
        following_layout.setOnClickListener(this);
        followers_layout.setOnClickListener(this);

        tabLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.viewpager);

        viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        ready_data();


        profileSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                ready_data();
            }
        });

    }


    private void ready_data()
    {
        User_Data userData;
        userData=sharedPrefManger.getdata();

        String img;
        img=userData.getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.mipmap.profile_default_img).into(profile_image);

        profile_name.setText(userData.getName());


        Call<ProfileResponseBody> call= Common.getApi().getprofileData(userData.getToken(),userData.getUsername());

        call.enqueue(new Callback<ProfileResponseBody>() {
            @Override
            public void onResponse(Call<ProfileResponseBody> call, Response<ProfileResponseBody> response)
            {
                if(response.code() == 200)
                {
                    ProfileResponseBody body=response.body();

                    likes_no.setText(body.getLikes());
                    post_no.setText(body.getPosts());
                    follower_no.setText(body.getFollowers());
                    following_no.setText(body.getFollowing());
                    profileSwipeRefreshLayout.setRefreshing(false);
                }
                else
                    Toast.makeText(getContext(),"error",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ProfileResponseBody> call, Throwable t)
            {
                Toast.makeText(getContext(),"Failure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view)
    {

        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(getContext());
        String username=sharedPrefManger.getdata().getUsername();

        if(view == likes_layout)
        {
            Intent intent=new Intent(getContext(), Profile_Likes.class);

            intent.putExtra("username",username);

            startActivity(intent);
        }

        if(view == followers_layout)
        {
            Intent intent = new Intent(getContext(), Profile_Followers.class);

            intent.putExtra("username",username);

            startActivity(intent);

        }

        if(view == following_layout)
        {
            Intent intent =new Intent(getContext(), Profile_Following.class);

            intent.putExtra("username",username);
            startActivity(intent);
        }
    }
}
