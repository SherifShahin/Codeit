package codeit.com.codeit.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.widget.Toast;

import codeit.com.codeit.Model.SearchResponseBody;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel
{
    private MutableLiveData<SearchResponseBody> searchData;
    private String searchString="";
    private Context context;


    public MutableLiveData<SearchResponseBody> getSearchData()
    {
        if(searchData == null)
        {
            searchData=new MutableLiveData<>();

            if(!searchString.isEmpty())
            search(searchString,context);
        }
        return searchData;
    }

    public void search(String seachString, final Context context)
    {
        SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(context);

        Call<SearchResponseBody> call= Common.getApi().search(sharedPrefManger.getdata().getToken(),
                seachString);


        call.enqueue(new Callback<SearchResponseBody>() {
            @Override
            public void onResponse(Call<SearchResponseBody> call, Response<SearchResponseBody> response) {
                if(response.code() == 200)
                searchData.setValue(response.body());

                else
                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<SearchResponseBody> call, Throwable t) {
                Toast.makeText(context,"Failure",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
