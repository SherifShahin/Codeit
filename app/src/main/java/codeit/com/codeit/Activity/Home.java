package codeit.com.codeit.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import codeit.com.codeit.Fragment.Home_fragment;
import codeit.com.codeit.Fragment.Profile_fragment;
import codeit.com.codeit.Fragment.Search_fragment;
import codeit.com.codeit.Fragment.notifications_fragment;
import codeit.com.codeit.R;
import codeit.com.codeit.Storage.SharedPrefManger;

public class Home extends AppCompatActivity implements View.OnClickListener,BottomNavigationView.OnNavigationItemSelectedListener
{

    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton CreatePost ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        displayFragment(new Home_fragment());

        CreatePost=findViewById(R.id.fab_create_post);
        CreatePost.setOnClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if(!SharedPrefManger.getInstance(this).isLogin())
        {
            startActivity(new Intent(this, Splash_Screen.class));
            finish();
        }

    }

    private void displayFragment(android.support.v4.app.Fragment fragment)
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.Fragment_container,fragment)
                .commit();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item)
    {
        android.support.v4.app.Fragment fragment=null;
        switch (item.getItemId())
        {
            case R.id.menu_profile:
                fragment=new Profile_fragment();
                break;

            case R.id.menu_search:
                fragment=new Search_fragment();
                break;

            case R.id.menu_notifications:
                fragment= new notifications_fragment();
                break;

            case R.id.menu_home:
                fragment=new Home_fragment();
                break;

        }


        if(fragment != null)
        {
            displayFragment(fragment);
        }

        return true;
    }


    @Override
    public void onClick(View view)
    {

        if(view == CreatePost)
        {
            startActivity(new Intent(getApplicationContext(),Create_Post.class));
        }
    }
}
