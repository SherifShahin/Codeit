package codeit.com.codeit.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import codeit.com.codeit.Adapter.PostLikesAdapter;
import codeit.com.codeit.Model.PostLikesResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.ViewModel.PostLikesViewModel;

public class PostLikes extends AppCompatActivity {

    private String post_id;
    private RecyclerView recyclerView;
    private PostLikesAdapter adapter;
    private PostLikesViewModel viewModel;
    private LiveData<List<PostLikesResponseBody>> listLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_likes);

        recyclerView=findViewById(R.id.postLikes_recycleview);
        post_id=getIntent().getStringExtra("post_id");

        viewModel= ViewModelProviders.of(this).get(PostLikesViewModel.class);

        viewModel.RequestLikes(this,post_id);

        listLiveData=viewModel.getLikes();


        listLiveData.observe(this, new Observer<List<PostLikesResponseBody>>() {
            @Override
            public void onChanged(@Nullable List<PostLikesResponseBody> postLikesResponseBodies)
            {
                adapter=new PostLikesAdapter(postLikesResponseBodies,getApplicationContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
            }
        });
    }
}
