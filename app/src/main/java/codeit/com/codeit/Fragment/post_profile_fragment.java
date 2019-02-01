package codeit.com.codeit.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codeit.com.codeit.Adapter.Profile_posts_adapter;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Model.Profile_posts;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import codeit.com.codeit.ViewModel.HomeViewModel;
import codeit.com.codeit.ViewModel.ProfilePostsViewModel;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

import static codeit.com.codeit.R.layout.post_profile_framgent;

public class post_profile_fragment extends Fragment
{

    private Profile_posts_adapter adapter;
    private RecyclerView recyclerView;
    private LiveData<List<Post>> listLiveData;
    private ProfilePostsViewModel viewModel;
    private ProgressBar progressBar;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.post_profile_framgent, container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.profile_posts_recycle_view);
        progressBar=view.findViewById(R.id.profile_posts_progressbar);

        viewModel=ViewModelProviders.of(getActivity()).get(ProfilePostsViewModel.class);

        viewModel.RequestPosts(getContext());

        listLiveData=viewModel.getPosts();

        listLiveData.observe(this, new Observer<List<Post>>() {
            @Override
            public void onChanged(@Nullable List<Post> posts) {

                adapter=new Profile_posts_adapter(getContext(),posts,progressBar);
                adapter.setHasStableIds(true);
                recyclerView.setAdapter(adapter);
                recyclerView.setHasFixedSize(true);
                recyclerView.setItemViewCacheSize(20);
                recyclerView.setDrawingCacheEnabled(true);
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false));
            }
        });
    }
}
