package codeit.com.codeit.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Pattern;

import codeit.com.codeit.Activity.Post_View;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Response;

public class HomePostsAdapter extends RecyclerView.Adapter<HomePostsAdapter.Viewholder>
{
    private List<HomePosts_Model> list;
    private Context context;


    public HomePostsAdapter(List<HomePosts_Model> list, Context context)
    {
        this.list = list;
        this.context = context;
    }

    public HomePostsAdapter()
    {

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        HomePostsAdapter.Viewholder viewholder=new HomePostsAdapter.Viewholder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_post_layout, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final Viewholder holder, final int position)
    {
           String img;

           img=list.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");


        boolean b= Patterns.WEB_URL.matcher(img).matches();

        if(!img.isEmpty() && b) {
            Picasso.get()
                    .load(img)
                    .into(holder.imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, "error img: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        holder.title.setText(list.get(position).getTitle());
        holder.likes.setText(list.get(position).getLikes());
        holder.comments.setText(list.get(position).getComments());
        holder.username.setText(list.get(position).getName());

        holder.post_layout.setOnClickListener(new View.OnClickListener() {
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
                            ActivityOptionsCompat activityOptions= null;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                                        Pair.create(holder.itemView.findViewById(R.id.Home_post_image), "postimage"),
                                        Pair.create(holder.itemView.findViewById(R.id.Home_post_title), "posttext")
                                );
                                context.startActivity(intent,activityOptions.toBundle());
                            }

                            else
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

    public class Viewholder extends RecyclerView.ViewHolder
    {
        private ImageView imageView;
        private TextView title;
        private TextView likes;
        private TextView comments;
        private TextView username;
        private LinearLayout post_layout;

        public Viewholder(View itemView)
        {
            super(itemView);

            imageView=itemView.findViewById(R.id.Home_post_image);
            title=itemView.findViewById(R.id.Home_post_title);
            likes=itemView.findViewById(R.id.Home_post_likes);
            comments=itemView.findViewById(R.id.Home_post_comments);
            username=itemView.findViewById(R.id.Home_post_username);
            post_layout=itemView.findViewById(R.id.home_post_layout);
        }
    }
}
