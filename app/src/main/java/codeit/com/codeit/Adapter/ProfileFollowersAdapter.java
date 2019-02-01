package codeit.com.codeit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Activity.Visited_Profile;
import codeit.com.codeit.Model.FollowResponse;
import codeit.com.codeit.Model.IsFollowingResponse;
import codeit.com.codeit.Model.ProfileFollowerResponseBody;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFollowersAdapter extends RecyclerView.Adapter<ProfileFollowersAdapter.viewholder>
{
    private List<ProfileFollowerResponseBody> list;
    private Context context;
    private static String following;
    private SharedPrefManger sharedPrefManger;

    public ProfileFollowersAdapter(List<ProfileFollowerResponseBody> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProfileFollowersAdapter.viewholder viewholder=new ProfileFollowersAdapter.viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profilefollowers, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final viewholder holder, final int position)
    {
        sharedPrefManger=SharedPrefManger.getInstance(context);
        final boolean isfollowing=list.get(position).getIsfollowing();
        String img;
        img=list.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.mipmap.profile_default_img).into(holder.img);
        holder.name.setText(list.get(position).getName());
        holder.username.setText("@"+list.get(position).getUsername());

        if(list.get(position).getUsername().equals(sharedPrefManger.getdata().getUsername()))
            holder.follow_bt.setVisibility(View.INVISIBLE);

        else {
            if (list.get(position).getIsfollowing()) {
                holder.follow_bt.setText("Following");
                holder.follow_bt.setBackgroundResource(R.drawable.login_bt_shape);
            } else {
                holder.follow_bt.setText("Follow");
                holder.follow_bt.setBackgroundResource(R.drawable.signup_bt_shape);
            }
        }

        holder.follow_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sharedPrefManger=SharedPrefManger.getInstance(context);

                if(list.get(position).getIsfollowing())
                {
                    retrofit2.Call<FollowResponse> call = Common.getApi().UnFollow(sharedPrefManger.getdata().getToken()
                            , sharedPrefManger.getdata().getUsername(),
                            list.get(position).getUsername());

                    call.enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<FollowResponse> call, Response<FollowResponse> response) {

                            if (response.code() == 200) {
                                holder.follow_bt.setText("Follow");
                                holder.follow_bt.setBackgroundResource(R.drawable.signup_bt_shape);
                                list.get(position).setIsfollowing(false);
                            }
                            else
                                Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<FollowResponse> call, Throwable t) {}
                    });

                }

                else
                {
                    retrofit2.Call<FollowResponse> call=Common.getApi().Follow(sharedPrefManger.getdata().getToken()
                            , sharedPrefManger.getdata().getUsername(),
                            list.get(position).getUsername());

                    call.enqueue(new Callback<FollowResponse>() {
                        @Override
                        public void onResponse(retrofit2.Call<FollowResponse> call, Response<FollowResponse> response) {
                            if(response.code() == 200)
                            {
                                holder.follow_bt.setText("Following");
                                holder.follow_bt.setBackgroundResource(R.drawable.login_bt_shape);
                                list.get(position).setIsfollowing(true);
                            }
                            else
                                Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(retrofit2.Call<FollowResponse> call, Throwable t) {}
                    });
                }

            }
        });



        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);

                retrofit2.Call<ProfileResponseBody> call= Common.getApi().getprofileData(sharedPrefManger.getdata().getToken(),
                        list.get(position).getUsername()
                );

                call.enqueue(new Callback<ProfileResponseBody>() {
                    @Override
                    public void onResponse(retrofit2.Call<ProfileResponseBody> call, Response<ProfileResponseBody> response)
                    {
                        if(response.code() == 200)
                        {
                            ProfileResponseBody body=response.body();
                            isFollowing(body,sharedPrefManger.getdata().getUsername(),list.get(position).getUsername(),list.get(position).getName());

                        }
                        else
                            Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(retrofit2.Call<ProfileResponseBody> call, Throwable t) {}
                });
            }
        });
    }



    private void isFollowing(final ProfileResponseBody body, String username1, final String username2, final String name)
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);
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

                    Intent intent=new Intent(context, Visited_Profile.class);
                    intent.putExtra("userData",body);
                    intent.putExtra("isFollowing",following);
                    intent.putExtra("name",name);
                    intent.putExtra("username",username2);

                    context.startActivity(intent);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<IsFollowingResponse> call, Throwable t) {}
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class viewholder extends RecyclerView.ViewHolder
    {

        private TextView name;
        private TextView username;
        private CircleImageView img;
        private LinearLayout linearLayout;
        private Button follow_bt;


        public viewholder(View itemView) {
            super(itemView);

            name=itemView.findViewById(R.id.profile_followers_list_item_name);
            username=itemView.findViewById(R.id.profile_followers_list_item_username);
            img=itemView.findViewById(R.id.profile_followers_list_item_img);
            linearLayout=itemView.findViewById(R.id.profile_followers_list_item_layout);
            follow_bt=itemView.findViewById(R.id.profile_followers_item_follow_bt);
        }
    }
}
