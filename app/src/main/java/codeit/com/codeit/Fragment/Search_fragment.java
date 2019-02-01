package codeit.com.codeit.Fragment;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import codeit.com.codeit.Adapter.SearchPostsAdapter;
import codeit.com.codeit.Adapter.SearchUsersAdapter;
import codeit.com.codeit.Model.SearchResponseBody;
import codeit.com.codeit.Model.search_posts;
import codeit.com.codeit.Model.search_users;
import codeit.com.codeit.R;
import codeit.com.codeit.ViewModel.SearchViewModel;

public class Search_fragment extends Fragment
{
    private EditText searchText;
    private SearchViewModel searchViewModel;
    private LiveData<SearchResponseBody> searchResponseBodyLiveData;
    private List<search_posts> searchPosts=new ArrayList<>();
    private List<search_users> searchUsers=new ArrayList<>();
    private RecyclerView usersRecyclerView;
    private RecyclerView postsRecyclerView;
    private SearchPostsAdapter searchPostsAdapter;
    private SearchUsersAdapter searchUsersAdapter;
    private LinearLayout search_result_layout;
    private LinearLayout search_posts_result_layout;
    private LinearLayout search_users_result_layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container,false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        searchText=view.findViewById(R.id.search_text);
        usersRecyclerView=view.findViewById(R.id.search_users_recycleview);
        postsRecyclerView=view.findViewById(R.id.search_posts_recycleview);
        search_result_layout=view.findViewById(R.id.search_result_layout);
        search_posts_result_layout=view.findViewById(R.id.search_posts_result_layout);
        search_users_result_layout=view.findViewById(R.id.search_users_result_layout);


        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(charSequence.length()>0)
                {
                    searchViewModel.search(charSequence.toString(), getContext());
                    search_result_layout.setVisibility(View.VISIBLE);
                }
                else
                    search_result_layout.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        searchViewModel= ViewModelProviders.of(getActivity()).get(SearchViewModel.class);

        searchResponseBodyLiveData=searchViewModel.getSearchData();

        searchResponseBodyLiveData.observe(getActivity(), new Observer<SearchResponseBody>() {
            @Override
            public void onChanged(@Nullable SearchResponseBody searchResponseBody)
            {
                searchPosts=searchResponseBody.getPosts();
                searchUsers=searchResponseBody.getUsers();

                if(searchUsers.size()<=0)
                   search_users_result_layout.setVisibility(View.INVISIBLE);

                if(searchPosts.size()<=0)
                    search_posts_result_layout.setVisibility(View.INVISIBLE);



                if(searchUsers.size()>0)
                {
                    search_users_result_layout.setVisibility(View.VISIBLE);
                    searchUsersAdapter=new SearchUsersAdapter(searchUsers,getContext());
                    usersRecyclerView.setAdapter(searchUsersAdapter);
                    usersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }


                if(searchPosts.size()>0)
                {
                    search_posts_result_layout.setVisibility(View.VISIBLE);
                    searchPostsAdapter=new SearchPostsAdapter(searchPosts,getContext());
                    postsRecyclerView.setAdapter(searchPostsAdapter);
                    postsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                }

            }
        });

    }
}
