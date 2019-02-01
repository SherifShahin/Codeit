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
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.Model.search_posts;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchPostsAdapter extends  RecyclerView.Adapter<SearchPostsAdapter.viewHolder>
{

    private List<search_posts> postsList;
    private Context context;


    public SearchPostsAdapter(List<search_posts> postsList, Context context)
    {
        this.postsList = postsList;
        this.context=context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchPostsAdapter.viewHolder viewholder=new SearchPostsAdapter.viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_post_result, parent, false));
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position)
    {

        String img;
        img=postsList.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        Picasso.get().load(img).placeholder(R.drawable.image_palceholder).into(holder.PostImage);

        holder.title.setText(postsList.get(position).getTitle());

        holder.post_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SharedPrefManger sharedPrefManger =SharedPrefManger.getInstance(context);

                Call<ViewPostResponseBody> call= Common.getApi().postview(sharedPrefManger.getdata().getToken(),
                        postsList.get(position).getUsername(),
                        postsList.get(position).getId(),
                        sharedPrefManger.getdata().getUsername()
                );

                call.enqueue(new Callback<ViewPostResponseBody>() {
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
        return postsList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder
    {
        private TextView title;
        private ImageView PostImage;
        private LinearLayout post_layout;

        public viewHolder(View view) {
            super(view);

            title=view.findViewById(R.id.profile_post_title);
            PostImage=view.findViewById(R.id.profile_post_image);
            post_layout=view.findViewById(R.id.search_post_result);
        }
    }
}
