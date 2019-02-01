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


import java.util.List;

import codeit.com.codeit.Adapter.NotificationsAdapter;
import codeit.com.codeit.Model.NotificationsResponseBody;
import codeit.com.codeit.R;
import codeit.com.codeit.ViewModel.NotificationsViewModel;


public class notifications_fragment extends Fragment
{

    private RecyclerView recyclerView;
    private SwipeRefreshLayout notifications_swipeRefreshLayout;
    private NotificationsAdapter adapter;

    private NotificationsViewModel viewModel;
    private LiveData<List<NotificationsResponseBody>> listLiveData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.notifications_fragment, container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=view.findViewById(R.id.notifications_recycleview);
        notifications_swipeRefreshLayout=view.findViewById(R.id.notifications_swipeRefresh);

        viewModel=ViewModelProviders.of(getActivity()).get(NotificationsViewModel.class);

        viewModel.RequestNotifications(getContext());

        listLiveData=viewModel.getNotificaitons();


        listLiveData.observe(this, new Observer<List<NotificationsResponseBody>>() {
            @Override
            public void onChanged(@Nullable List<NotificationsResponseBody> notificationsResponseBodies) {
                adapter=new NotificationsAdapter(notificationsResponseBodies,getContext());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
            }
        });


        notifications_swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.RequestNotifications(getContext());
                adapter.notifyDataSetChanged();
                notifications_swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
}
