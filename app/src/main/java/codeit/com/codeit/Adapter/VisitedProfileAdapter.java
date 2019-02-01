package codeit.com.codeit.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import codeit.com.codeit.Activity.Post_View;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitedProfileAdapter extends RecyclerView.Adapter<VisitedProfileAdapter.viewHolder>
{
    private List<Post> postList;
    private final Context context;


    public VisitedProfileAdapter(Context context, List<Post> postList)
    {
        this.context=context;
        this.postList = postList;
    }


    @NonNull
    @Override
    public VisitedProfileAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VisitedProfileAdapter.viewHolder viewholder=new VisitedProfileAdapter.viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_post_layout, parent, false));
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull VisitedProfileAdapter.viewHolder holder, final int position)
    {
        String img;
        img=postList.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        holder.post_title.setText(postList.get(position).getTitle());
        Picasso.get().load(img).into(holder.post_image);


        holder.post_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpost(postList.get(position).getId());
            }
        });

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpost(postList.get(position).getId());
            }
        });

    }


    private void viewpost(String id)
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);

        String token=sharedPrefManger.getdata().getToken();
        String username=sharedPrefManger.getdata().getUsername();


        Call<ViewPostResponseBody> call= Common.getApi().postview(token,username,id,username);

        call.enqueue(new Callback<ViewPostResponseBody>() {
            @Override
            public void onResponse(Call<ViewPostResponseBody> call, Response<ViewPostResponseBody> response)
            {
                if(response.code() == 200)
                {
                    ViewPostResponseBody body= response.body();

                    Intent intent =new Intent(context, Post_View.class);
                    intent.putExtra("post",body);
                    context.startActivity(intent);
                }

                else
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<ViewPostResponseBody> call, Throwable t) {
                Toast.makeText(context,"failure"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

    }



    @Override
    public int getItemCount()
    {
        return postList.size();
    }


    public class viewHolder extends RecyclerView.ViewHolder
    {
        private ImageView post_image;
        private TextView post_title;

        public viewHolder(View itemView)
        {
            super(itemView);

            post_image=itemView.findViewById(R.id.profile_post_image);
            post_title=itemView.findViewById(R.id.profile_post_title);

        }
    }

}
