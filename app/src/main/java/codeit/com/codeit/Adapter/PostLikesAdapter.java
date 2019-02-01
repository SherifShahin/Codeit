package codeit.com.codeit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Activity.Visited_Profile;
import codeit.com.codeit.Model.IsFollowingResponse;
import codeit.com.codeit.Model.PostLikesResponseBody;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLikesAdapter extends RecyclerView.Adapter<PostLikesAdapter.viewholder>
{
    private List<PostLikesResponseBody> list;
    private Context context;
    private static  String following;

    public PostLikesAdapter(List<PostLikesResponseBody> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PostLikesAdapter.viewholder viewholder=new PostLikesAdapter.viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_likes, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position)
    {

        String img;
        img=list.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).into(holder.userimg);
        holder.name.setText(list.get(position).getName());
        holder.username.setText(list.get(position).getUsername());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
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
        private CircleImageView userimg;
        private TextView name;
        private TextView username;
        private LinearLayout linearLayout;

        public viewholder(View itemView) {
            super(itemView);

            userimg=itemView.findViewById(R.id.postLikes_img);
            name=itemView.findViewById(R.id.postLikes_name);
            username=itemView.findViewById(R.id.postLikes_username);
            linearLayout=itemView.findViewById(R.id.PostLikes_layout);
        }
    }

}
