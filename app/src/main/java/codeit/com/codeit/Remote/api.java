package codeit.com.codeit.Remote;

import java.util.List;

import codeit.com.codeit.Model.Comments_ResponseBody;
import codeit.com.codeit.Model.CreatePostResponseBody;
import codeit.com.codeit.Model.FollowResponse;
import codeit.com.codeit.Model.HomePosts_Model;
import codeit.com.codeit.Model.IsFollowingResponse;
import codeit.com.codeit.Model.LikesResponseBody;
import codeit.com.codeit.Model.LoginResponseBody;
import codeit.com.codeit.Model.Login_Model;
import codeit.com.codeit.Model.NotificationsResponseBody;
import codeit.com.codeit.Model.Post;
import codeit.com.codeit.Model.PostLikesResponseBody;
import codeit.com.codeit.Model.ProfileFollowerResponseBody;
import codeit.com.codeit.Model.ProfileFollowingResponseBody;
import codeit.com.codeit.Model.ProfileResponseBody;
import codeit.com.codeit.Model.Register_Model;
import codeit.com.codeit.Model.SearchResponseBody;
import codeit.com.codeit.Model.SettingsResponseBody;
import codeit.com.codeit.Model.Settings_Model;
import codeit.com.codeit.Model.ViewPostResponseBody;
import codeit.com.codeit.Model.createComment_Model;
import codeit.com.codeit.Model.create_post_model;
import codeit.com.codeit.Model.makeLikeResponseBody;
import codeit.com.codeit.Model.profileviewpostResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface api
{

    @Headers({"Content-Type: application/json"})
    @POST("users/register")
    Call<ResponseBody> createUser(@Body Register_Model register);


    @Headers({"Content-Type: application/json"})
    @POST("users/login")
    Call<LoginResponseBody> loginUser(@Body Login_Model login);



    @Headers({"Content-Type: application/json"})
    @PUT("users/{username}/update")
    Call<SettingsResponseBody> UpdateUser(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Body Settings_Model userData
    );


    @Headers({"Content-Type: application/json"})
    @GET("posts/{username}")
    Call<List<Post>> getPosts(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );



    @Headers({"Content-Type: application/json"})
    @POST("posts/{username}")
    Call<CreatePostResponseBody> CreatePost(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Body create_post_model post);



    @Headers({"Content-Type: application/json"})
    @GET("posts/{username}/{id}/{visitor}")
    Call<ViewPostResponseBody> postview(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id,
            @Path("visitor") String visitor
    );

    @Headers({"Content-Type: application/json"})
    @GET("posts/{username}/{id}")
    Call<profileviewpostResponse> profilepostview(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/profile")
    Call<ProfileResponseBody> getprofileData(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );



    @Headers({"Content-Type: application/json"})
    @GET("search/{searchData}")
    Call<SearchResponseBody> search(
            @Header("x-auth-token") String token,
            @Path("searchData") String searchData
    );


    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/home")
    Call<List<HomePosts_Model>> getHomePosts(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username1}/isfollowing/{username2}")
    Call<IsFollowingResponse> isFollowing(
            @Header("x-auth-token") String token,
            @Path("username1") String username1,
            @Path("username2") String username2
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username1}/follow/{username2}")
    Call<FollowResponse> Follow(
            @Header("x-auth-token") String token,
            @Path("username1") String username1,
            @Path("username2") String username2
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username1}/unfollow/{username2}")
    Call<FollowResponse> UnFollow(
            @Header("x-auth-token") String token,
            @Path("username1") String username1,
            @Path("username2") String username2
    );




    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/notifications")
    Call<List<NotificationsResponseBody>> getNotifications(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );


    @Headers({"Content-Type: application/json"})
    @GET("posts/{username}/like/{id}")
    Call<makeLikeResponseBody> makeLike(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id
    );


    @Headers({"Content-Type: application/json"})
    @GET("posts/{username}/unlike/{id}")
    Call<makeLikeResponseBody> makeunLike(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id
    );


    @Headers({"Content-Type: application/json"})
    @GET("comments/{username}/{id}")
    Call<List<Comments_ResponseBody>> getcomments(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id
    );



    @Headers({"Content-Type: application/json"})
    @POST("comments/{username}/{id}")
    Call<ResponseBody> createComments(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id,
           @Body createComment_Model model
    );

    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/likes")
    Call<List<LikesResponseBody>> getLikes(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/followers")
    Call<List<ProfileFollowerResponseBody>> getFollowers(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );



    @Headers({"Content-Type: application/json"})
    @GET("users/{username}/following")
    Call<List<ProfileFollowingResponseBody>> getFollowing(
            @Header("x-auth-token") String token,
            @Path("username") String username
    );


    @Headers({"Content-Type: application/json"})
    @GET("posts/{id}/likes")
    Call<List<PostLikesResponseBody>> getPostLikes(
            @Header("x-auth-token") String token,
            @Path("id") String id
    );

    @Headers({"Content-Type: application/json"})
    @DELETE("posts/{username}/{id}")
    Call<ResponseBody> deletePost(
            @Header("x-auth-token") String token,
            @Path("username") String username,
            @Path("id") String id
    );


}
