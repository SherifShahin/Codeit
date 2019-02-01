package codeit.com.codeit.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.List;

import codeit.com.codeit.Adapter.CommentsAdapter;
import codeit.com.codeit.Adapter.HomePostsAdapter;
import codeit.com.codeit.Model.Comments_ResponseBody;
import codeit.com.codeit.Model.createComment_Model;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import codeit.com.codeit.ViewModel.CommentsViewModel;
import codeit.com.codeit.ViewModel.HomeViewModel;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class Comments extends AppCompatActivity
{
    private String post_id;
    private String post_username;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private EditText setcomment;
    private Button comment_bt;

    private CommentsViewModel viewModel;
    private LiveData<List<Comments_ResponseBody>> listLiveData;
    private CommentsAdapter commentsAdapter;

    private SharedPrefManger sharedPrefManger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        progressBar=findViewById(R.id.comments_progressbar);
        setcomment=findViewById(R.id.comment_edittext);
        comment_bt=findViewById(R.id.comment_button);
        recyclerView=findViewById(R.id.comments_recycleview);
        post_id= getIntent().getStringExtra("post_id");
        post_username=getIntent().getStringExtra("post_username");
        viewModel= ViewModelProviders.of(this).get(CommentsViewModel.class);

        sharedPrefManger=SharedPrefManger.getInstance(this);

        viewModel.RequestComments(this,post_id);

        listLiveData=viewModel.getComments();

        listLiveData.observe(this, new Observer<List<Comments_ResponseBody>>() {
            @Override
            public void onChanged(@Nullable List<Comments_ResponseBody> comments_responseBodies)
            {
                commentsAdapter=new CommentsAdapter(comments_responseBodies,getApplicationContext());
                recyclerView.setAdapter(commentsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            }
        });


        comment_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String body=setcomment.getText().toString();
                String username = sharedPrefManger.getdata().getUsername();
                String token=sharedPrefManger.getdata().getToken();

                if(!body.isEmpty())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    createComment_Model commentModel=new createComment_Model();

                    commentModel.setBody(body);
                    commentModel.setUsername(username);


                    retrofit2.Call<ResponseBody> call= Common.getApi().createComments(token,post_username,post_id,commentModel);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {

                            if(response.code() == 200)
                            {
                                viewModel.RequestComments(getApplicationContext(),post_id);
                                commentsAdapter.notifyDataSetChanged();
                                setcomment.setText("");
                                progressBar.setVisibility(View.INVISIBLE);
                            }
                            else
                                progressBar.setVisibility(View.INVISIBLE);

                        }

                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });



    }
}
