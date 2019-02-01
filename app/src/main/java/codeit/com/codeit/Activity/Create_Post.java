package codeit.com.codeit.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

import codeit.com.codeit.Model.CreatePostResponseBody;
import codeit.com.codeit.Model.create_post_model;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Create_Post extends AppCompatActivity implements View.OnClickListener
{
    private TextView cover_image;
    private EditText title;
    private EditText post_text;
    private Button create_bt;
    private ProgressBar progressBar;


    private String image_path="";
    private String imageString = null;
    private Bitmap thumb_bitmap;

    private SharedPrefManger sharedPrefManger;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__post);

      Toolbar  chatbar=findViewById(R.id.main_app_bar);
      progressBar=findViewById(R.id.create_post_progressbar);
      cover_image=findViewById(R.id.post_cover_image);
      title=findViewById(R.id.post_title);
      post_text=findViewById(R.id.post_text);
      create_bt=findViewById(R.id.post_create_bt);

      sharedPrefManger=SharedPrefManger.getInstance(this);

        setSupportActionBar(chatbar);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater=(LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View action_bar_view=layoutInflater.inflate(R.layout.create_post_custom_bar, null);

        actionBar.setCustomView(action_bar_view);


        cover_image.setOnClickListener(this);
        create_bt.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {
        if(view == cover_image)
        {
            getimagefromGalary();
        }

        if(view == create_bt)
        {
            makePost();
        }

    }

    private void getimagefromGalary()
    {
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0 && resultCode == RESULT_OK)
        {
            if (data == null)
            {
                Toast.makeText(getApplicationContext(), "no image choosed!!", Toast.LENGTH_SHORT).show();
                return;
            }


            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri).setAspectRatio(1, 1)
                    .start(Create_Post.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_path = resultUri.getPath();
            }

        }
    }



    private void makePost()
    {

        progressBar.setVisibility(View.VISIBLE);
        String posttitle=title.getText().toString();
        String posttext=post_text.getText().toString();


        if(image_path.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must set image",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(posttext.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must set title",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(posttext.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must set post",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        if(posttitle.length()<10)
        {
            Toast.makeText(getApplicationContext(),"sorry,this title is too short",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(posttitle.length()>100)
        {
            Toast.makeText(getApplicationContext(),"sorry,this title is too long",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(posttext.length() <= 100)
        {
            Toast.makeText(getApplicationContext(),"sorry,this post is too short",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumb_bitmap = BitmapFactory.decodeFile(image_path);
        thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        create_post_model post_model=new create_post_model();

        if(!imageString.isEmpty())
        post_model.setImgFile(imageString);

        post_model.setTitle(posttitle);
        post_model.setContent(posttext);

        String token=sharedPrefManger.getdata().getToken();
        String username=sharedPrefManger.getdata().getUsername();


        Call<CreatePostResponseBody> call= Common.getApi().CreatePost(token,username,post_model);

        call.enqueue(new Callback<CreatePostResponseBody>() {
            @Override
            public void onResponse(Call<CreatePostResponseBody> call, Response<CreatePostResponseBody> response)
            {
                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(),"post created",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    finish();
                }

                else {
                    Toast.makeText(getApplicationContext(), "sorry try again later....", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CreatePostResponseBody> call, Throwable t)
            {
                Toast.makeText(getApplicationContext(),"no internet Connection",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });


    }

}
