package codeit.com.codeit.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Model.User_Data;


public class SharedPrefManger
{
    private static String Shared_name="codeit_user_data";
    private static SharedPrefManger mInstance;
    private Context context;

    public SharedPrefManger(Context context)
    {
        this.context = context;
    }

    public static synchronized SharedPrefManger getInstance(Context context)
    {
        if(mInstance == null )
        {
            mInstance=new SharedPrefManger(context);
        }
      return mInstance;
    }

    public void save_data(User_Data userData)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();


        editor.putString("token",userData.getToken());
        editor.putString("username",userData.getUsername());
        editor.putString("name",userData.getName());
        editor.putString("email",userData.getEmail());
        editor.putString("img",userData.getImg());
        editor.putString("password",userData.getPassword());

        editor.commit();
    }

    public String get_token()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public User_Data getdata()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);

        User_Data userData=new User_Data();

        userData.setToken(sharedPreferences.getString("token",""));
        userData.setName(sharedPreferences.getString("name",""));
        userData.setEmail(sharedPreferences.getString("email",""));
        userData.setImg(sharedPreferences.getString("img",""));
        userData.setUsername(sharedPreferences.getString("username",""));
        userData.setPassword(sharedPreferences.getString("password",""));

        return userData;
    }



    public void set_post(List<HomePosts_Model> list)
    {

        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString("post_list", json);
        editor.commit();
    }


    public  List<HomePosts_Model> getposts()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sharedPreferences.getString("post_list", null);

        Type type=new TypeToken<List<HomePosts_Model>>(){}.getType();
        List<HomePosts_Model> list=gson.fromJson(json,type);
        return list;
    }





    public void setHavePosts()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putBoolean("haveposts",true);

        editor.apply();
        editor.commit();

    }


    public boolean havePosts()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean("haveposts",false);
    }






    public boolean isLogin()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        if(!sharedPreferences.getString("token","none").equals("none"))
        {
            return true;
        }
        return false;
    }


    public void clear()
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(Shared_name,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.clear();
        editor.apply();
        editor.commit();
    }


}
