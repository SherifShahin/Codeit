package codeit.com.codeit.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import codeit.com.codeit.Model.IsFollowingResponse;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.Model.makeLikeResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class Post_View extends AppCompatActivity
{
    private ImageView post_image;
    private TextView post_title;
    private CircleImageView user_image;
    private TextView username;
    private TextView publishdate;
    private TextView content;
    private ImageView liked;
    private TextView likesno;
    private boolean isliked;
    private FloatingActionButton comments;

    private static String following;


    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post__view);

        post_image=findViewById(R.id.post_view_image);
        post_title=findViewById(R.id.postview_title);
        user_image=findViewById(R.id.postview_userimage);
        username=findViewById(R.id.postview_username);
        publishdate=findViewById(R.id.postview_publishdate);
        content=findViewById(R.id.postview_content);
        liked=findViewById(R.id.postview_like_bt);
        likesno=findViewById(R.id.postview_likesno);
        comments=findViewById(R.id.fab_write_comment);


        final ViewPostResponseBody body= (ViewPostResponseBody) getIntent().getSerializableExtra("post");

        if(!body.getUsername().equals(SharedPrefManger.getInstance(getApplicationContext()).getdata().getUsername()))
        {
            if (body.isIsliked()) {
                liked.setImageResource(R.mipmap.like);
                isliked = true;
            } else {
                liked.setImageResource(R.mipmap.empty_like);
                isliked = false;
            }
        }

        else
            liked.setVisibility(View.INVISIBLE);


        final String img;
        img=body.getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).into(post_image);
        post_title.setText(body.getTitle());

        String img2;
        img2=body.getUserImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img2).placeholder(R.mipmap.profile_default_img).into(user_image);
        username.setText(body.getName());
        publishdate.setText(body.getCreated());
        content.setText(body.getContent());
        likesno.setText(body.getLikes());


        liked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(getApplicationContext());

                String token=sharedPrefManger.getdata().getToken();
                String username=sharedPrefManger.getdata().getUsername();

                if(isliked == true)
                {
                    retrofit2.Call<makeLikeResponseBody> call=Common.getApi().makeunLike(token,username,body.getId());

                    call.enqueue(new Callback<makeLikeResponseBody>() {
                        @Override
                        public void onResponse(retrofit2.Call<makeLikeResponseBody> call, Response<makeLikeResponseBody> response) {

                            if(response.code() == 200)
                            {
                                liked.setImageResource(R.mipmap.empty_like);
                                isliked=false;
                                likesno.setText(response.body().getNumber());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<makeLikeResponseBody> call, Throwable t) { }
                    });

                }
                else
                {
                    retrofit2.Call<makeLikeResponseBody> call= Common.getApi().makeLike(token,username,body.getId());

                    call.enqueue(new Callback<makeLikeResponseBody>() {
                        @Override
                        public void onResponse(retrofit2.Call<makeLikeResponseBody> call, Response<makeLikeResponseBody> response) {
                            if(response.code() == 200)
                            {
                                liked.setImageResource(R.mipmap.like);
                                isliked=true;
                                likesno.setText(response.body().getNumber());
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<makeLikeResponseBody> call, Throwable t) {}
                    });
                }
            }
        });

        comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),Comments.class);

                intent.putExtra("post_id",body.getId());
                intent.putExtra("post_username",body.getUsername());

                startActivity(intent);

            }
        });



        likesno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent=new Intent(getApplicationContext(),PostLikes.class);

                intent.putExtra("post_id",body.getId());

                startActivity(intent);
            }
        });




        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String post_username=body.getUsername();
                final String post_name=body.getName();
                final SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(getApplicationContext());

                retrofit2.Call<ProfileResponseBody> call= Common.getApi().getprofileData(sharedPrefManger.getdata().getToken(),
                        body.getUsername()
                );

                call.enqueue(new Callback<ProfileResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ProfileResponseBody> call, Response<ProfileResponseBody> response)
                    {
                        if(response.code() == 200)
                        {
                            ProfileResponseBody body=response.body();
                            isFollowing(body,sharedPrefManger.getdata().getUsername(),post_username,post_name);

                        }

                    }

                    @Override
                    public void onFailure(retrofit2.Call<ProfileResponseBody> call, Throwable t) {
                    }
                });

            }
        });
    }


    private void isFollowing(final ProfileResponseBody body, String username1, final String username2, final String name)
    {

        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(this);
        retrofit2.Call<IsFollowingResponse> call=Common.getApi().isFollowing(sharedPrefManger.getdata().getToken(),
                username1,
                username2
        );

        call.enqueue(new Callback<IsFollowingResponse>() {
            @Override
            public void onResponse(retrofit2.Call<IsFollowingResponse> call, Response<IsFollowingResponse> response)
            {
                if(response.code() == 200)
                {
                    IsFollowingResponse body1=response.body();

                    following=body1.getIsfollowing();

                    Intent intent=new Intent(getApplicationContext(), Visited_Profile.class);
                    intent.putExtra("userData",body);
                    intent.putExtra("isFollowing",following);
                    intent.putExtra("name",name);
                    intent.putExtra("username",username2);

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<IsFollowingResponse> call, Throwable t) {}
        });
    }

}
