package codeit.com.codeit.Remote;

public class Common
{
    private static  final String BASE_URL="https://codeitblog.herokuapp.com/api/";

    public static api getApi()
    {
        return RetrofitClient.getClient(BASE_URL).create(api.class);
    }

}
