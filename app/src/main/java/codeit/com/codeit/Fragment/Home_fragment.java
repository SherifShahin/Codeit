package codeit.com.codeit.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import codeit.com.codeit.Adapter.HomePostsAdapter;
import codeit.com.codeit.InternetConnection.CheckNetwork;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.R;
import codeit.com.codeit.Storage.SharedPrefManger;
import codeit.com.codeit.ViewModel.HomeViewModel;

public class Home_fragment extends Fragment
{
    private RecyclerView recyclerView;
    private HomeViewModel homeViewModel;
    private HomePostsAdapter homePostsAdapter;
    private LiveData<List<HomePosts_Model>> listLiveData;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar home_progressbar;
    private boolean internetConnect;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout=view.findViewById(R.id.Home_swipeRefresh);
        recyclerView=view.findViewById(R.id.Home_Posts_recycleview);
        home_progressbar=view.findViewById(R.id.Home_progressBar);

        home_progressbar.setVisibility(View.VISIBLE);

        final CheckNetwork checkNetwork=new CheckNetwork();

        internetConnect=checkNetwork.isInternetAvailable(getContext());

        if(internetConnect)
        {
            Connected();
        }
        else
        {
           noConnect();
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh()
            {
                internetConnect=checkNetwork.isInternetAvailable(getContext());
                if(internetConnect) {
                    homeViewModel.RequestPosts(getContext());
                    homePostsAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    home_progressbar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    swipeRefreshLayout.setRefreshing(false);
                    noConnect();
                }
            }
        });

    }

    private void Connected()
    {
        homeViewModel= ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        homeViewModel.RequestPosts(getContext());

        listLiveData=homeViewModel.getPosts();


        listLiveData.observe(getActivity(), new Observer<List<HomePosts_Model>>() {
            @Override
            public void onChanged(@Nullable List<HomePosts_Model> homePosts_models) {
                home_progressbar.setVisibility(View.INVISIBLE);
                homePostsAdapter=new HomePostsAdapter(homePosts_models,getContext());
                recyclerView.setAdapter(homePostsAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            }
        });

    }


    private void noConnect()
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(getContext());

        home_progressbar.setVisibility(View.INVISIBLE);
        Toast.makeText(getContext(),"there is no internet Connect",Toast.LENGTH_LONG).show();
        home_progressbar.setVisibility(View.INVISIBLE);

        if(sharedPrefManger.havePosts()) {
            homePostsAdapter = new HomePostsAdapter(sharedPrefManger.getposts(), getContext());
            recyclerView.setAdapter(homePostsAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }
    }


}
