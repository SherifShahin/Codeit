package codeit.com.codeit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Activity.Visited_Profile;
import codeit.com.codeit.Model.Comments_ResponseBody;
import codeit.com.codeit.Model.IsFollowingResponse;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.viewholder>
{
    private List<Comments_ResponseBody> list;
    private Context context;
    private   static String following;


    public CommentsAdapter(List<Comments_ResponseBody> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CommentsAdapter.viewholder viewholder=new CommentsAdapter.viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position)
    {
        String img;
        img=list.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.mipmap.profile_default_img).into(holder.userimage);
        holder.username.setText(list.get(position).getName());
        holder.date.setText(list.get(position).getDate());
        holder.body.setText(list.get(position).getBody());


       holder.userimage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final SharedPrefManger sharedPrefManger = SharedPrefManger.getInstance(context);

                   retrofit2.Call<ProfileResponseBody> call = Common.getApi().getprofileData(sharedPrefManger.getdata().getToken(),
                           list.get(position).getUsername()
                   );

                   call.enqueue(new Callback<ProfileResponseBody>() {
                       @Override
                       public void onResponse(retrofit2.Call<ProfileResponseBody> call, Response<ProfileResponseBody> response) {
                           if (response.code() == 200) {
                               ProfileResponseBody body = response.body();
                               isFollowing(body, sharedPrefManger.getdata().getUsername(), list.get(position).getUsername(), list.get(position).getName());
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

        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);
        retrofit2.Call<IsFollowingResponse> call= Common.getApi().isFollowing(sharedPrefManger.getdata().getToken(),
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
            public void onFailure(retrofit2.Call<IsFollowingResponse> call, Throwable t) {

            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class viewholder extends RecyclerView.ViewHolder
    {
        private CircleImageView userimage;
        private TextView username;
        private TextView date;
        private TextView body;

        public viewholder(View itemView)
        {
            super(itemView);

            userimage=itemView.findViewById(R.id.comment_user_img);
            username=itemView.findViewById(R.id.comment_username);
            date=itemView.findViewById(R.id.comment_date);
            body=itemView.findViewById(R.id.comment_body);
        }
    }

}
