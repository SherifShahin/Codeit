package codeit.com.codeit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Activity.Post_View;
import codeit.com.codeit.Model.LikesResponseBody;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileLikesAdapter extends RecyclerView.Adapter<ProfileLikesAdapter.viewholder>
{
    private List<LikesResponseBody> list;
    private Context context;

    public ProfileLikesAdapter(List<LikesResponseBody> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProfileLikesAdapter.viewholder viewholder=new ProfileLikesAdapter.viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_post_layout, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, final int position)
    {
        String img;
        img=list.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.drawable.image_palceholder).into(holder.imageView);
        holder.username.setText(list.get(position).getUsername());
        holder.title.setText(list.get(position).getTitle());
        holder.likes.setText(list.get(position).getLikes());
        holder.comments.setText(list.get(position).getComments());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);

                Call<ViewPostResponseBody> call= Common.getApi().postview(sharedPrefManger.getdata().getToken(),
                        list.get(position).getUsername(),
                        list.get(position).getId(),
                        sharedPrefManger.getdata().getUsername()
                );

                call.enqueue(new retrofit2.Callback<ViewPostResponseBody>() {
                    @Override
                    public void onResponse(Call<ViewPostResponseBody> call, Response<ViewPostResponseBody> response) {

                        if(response.code() == 200)
                        {
                            ViewPostResponseBody body= response.body();

                            Intent intent =new Intent(context, Post_View.class);
                            intent.putExtra("post",body);
                            context.startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<ViewPostResponseBody> call, Throwable t) {

                    }
                });

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewholder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private TextView title;
        private TextView username;
        private TextView likes;
        private TextView comments;
        private LinearLayout linearLayout;

        public viewholder(View itemView)
        {
            super(itemView);

            imageView=itemView.findViewById(R.id.Home_post_image);
            title=itemView.findViewById(R.id.Home_post_title);
            username=itemView.findViewById(R.id.Home_post_username);
            likes=itemView.findViewById(R.id.Home_post_likes);
            comments=itemView.findViewById(R.id.Home_post_comments);
            linearLayout=itemView.findViewById(R.id.home_post_layout);

        }
    }
}
