package codeit.com.codeit.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import codeit.com.codeit.Activity.Post_View;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Profile_posts_adapter extends RecyclerView.Adapter<Profile_posts_adapter.viewHolder>
{

    private static List<Post> postList;
    private final Context context;
    private ProgressBar progressBar;
    private Dialog dialog;
    private Button yes_delete;
    private Button no_delete;
    private ImageView close_popup;

    public Profile_posts_adapter(Context context, List<Post> postList,ProgressBar progressBar)
    {
        this.context=context;
        this.postList = postList;
        this.progressBar=progressBar;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder viewholder=new viewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_profile_post_layout, parent, false));
        return viewholder;
    }


    @Override
    public void onBindViewHolder(@NonNull final viewHolder holder, final int position)
    {
        String img;
        img=postList.get(position).getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

        holder.post_title.setText(postList.get(position).getTitle());
        Picasso.get().load(img).into(holder.post_image);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewpost(postList.get(position).getId(),holder);
            }
        });


        holder.delete_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup(postList.get(position).getId(),position);
            }
        });

    }

    private void showpopup(final String id, final int position)
    {
        dialog=new Dialog(context);

        dialog.setContentView(R.layout.delete_post_popup);
        yes_delete=dialog.findViewById(R.id.yes_delete_popup_bt);
        no_delete=dialog.findViewById(R.id.no_delete_popup_bt);
        close_popup=dialog.findViewById(R.id.delete_popup_close);

        dialog.setCancelable(false);

        yes_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);

                Call call=Common.getApi().deletePost(sharedPrefManger.getdata().getToken()
                ,sharedPrefManger.getdata().getUsername(),id);


                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response)
                    {
                        if(response.code() == 200)
                        {
                            dialog.dismiss();
                            postList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, postList.size());
                            notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            }
        });

        no_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void viewpost(String id, final viewHolder holder)
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
                    progressBar.setVisibility(View.INVISIBLE);
                    ViewPostResponseBody body= response.body();
                    Intent intent =new Intent(context, Post_View.class);
                    intent.putExtra("post",body);
                    context.startActivity(intent);
                }

                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ViewPostResponseBody> call, Throwable t) {
                Toast.makeText(context,"failure"+t.getMessage(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
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
        private LinearLayout linearLayout;
        private ImageView post_image;
        private TextView post_title;
        private ImageView delete_post;


        public viewHolder(View itemView)
        {
            super(itemView);

            linearLayout=itemView.findViewById(R.id.user_profile_post_layout);
            post_image=itemView.findViewById(R.id.user_post_image);
            post_title=itemView.findViewById(R.id.user_post_title);
            delete_post=itemView.findViewById(R.id.user_post_delete);

        }
    }
}
