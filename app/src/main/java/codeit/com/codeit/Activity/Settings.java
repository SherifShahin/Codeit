package codeit.com.codeit.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

import codeit.com.codeit.Builders.SettingsBuilder;
import codeit.com.codeit.Model.LoginResponseBody;
import codeit.com.codeit.Model.SettingsResponseBody;
import codeit.com.codeit.Model.Settings_Model;
import codeit.com.codeit.Model.User;
import codeit.com.codeit.Model.User_Data;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Callback;
import retrofit2.Response;

public class Settings extends AppCompatActivity implements View.OnClickListener
{
    private ProgressBar progressBar;
    private CircleImageView settings_image_view;
    private TextView add_image;
    private EditText name;
    private EditText email;
    private EditText password;
    private Button confirm_bt;

    private String image_path="";
    private String imageString = null;
    private Bitmap thumb_bitmap;

    private SharedPrefManger sharedPrefManger;
    private User_Data userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBar=findViewById(R.id.settings_progressbar);
        settings_image_view=findViewById(R.id.settings_image_view);
        add_image=findViewById(R.id.settings_add_user_img);
        name=findViewById(R.id.settings_name);
        email=findViewById(R.id.settings_email);
        password=findViewById(R.id.settings_password);
        confirm_bt=findViewById(R.id.settings_confirm_bt);

        add_image.setOnClickListener(this);
        confirm_bt.setOnClickListener(this);

        sharedPrefManger=SharedPrefManger.getInstance(getApplicationContext());

        ready_data();

    }

    private void ready_data()
    {
        userdata=sharedPrefManger.getdata();

        if(!userdata.getImg().isEmpty())
        {
            String img;
            img=userdata.getImg().replace("http://localhost:5000","http://192.168.43.180:5000");

            Picasso.get().load(img).placeholder(R.mipmap.profile_default_img).into(settings_image_view);
        }

        if(!userdata.getName().isEmpty())
        name.setText(userdata.getName());
        if(!userdata.getEmail().isEmpty())
        email.setText(userdata.getEmail());
        if(!userdata.getPassword().isEmpty())
        password.setText(userdata.getPassword());

    }

    @Override
    public void onClick(View view)
    {
        if(view == add_image)
            getimagefromGalary();

        if(view == confirm_bt)
            update_data();

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
                    .start(Settings.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                image_path = resultUri.getPath();
                Picasso.get().load(resultUri).placeholder(R.mipmap.profile_default_img).into(settings_image_view);
            }

        }
    }


    private void update_data()
    {
        SettingsBuilder builder=new SettingsBuilder();
        builder.reset();

        String uname=name.getText().toString();
        String uemail=email.getText().toString();
        String upassword=password.getText().toString();


        if(!image_path.isEmpty())
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            thumb_bitmap = BitmapFactory.decodeFile(image_path);
            thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 60,baos);
            byte[] imageBytes = baos.toByteArray();
            imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            builder.setImgfile(imageString);
        }


        if(!uname.isEmpty())
        {
            if(!uname.equals(userdata.getName()) && uname.length()>=5)
            {
                builder.setName(uname);
            }
            else
                builder.setName(userdata.getName());
        }


        if(!uemail.isEmpty())
        {
            if(!uemail.equals(userdata.getEmail()))
            {
                builder.setEmail(uemail);
            }
            else
                builder.setEmail(userdata.getEmail());
        }


        if(!upassword.isEmpty() && upassword.length() >=8)
        {
                builder.setPassword(upassword);
        }
        else
            builder.setPassword(userdata.getPassword());

        final User user=builder.Build();

        progressBar.setVisibility(View.VISIBLE);

        retrofit2.Call<SettingsResponseBody> call= Common.getApi().UpdateUser(userdata.getToken(),userdata.getUsername(),user);

        call.enqueue(new Callback<SettingsResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<SettingsResponseBody> call, Response<SettingsResponseBody> response)
            {
             if(response.code() != 404)
             {
                 progressBar.setVisibility(View.INVISIBLE);
                 Toast.makeText(getApplicationContext(),"updated",Toast.LENGTH_SHORT).show();

                 SettingsResponseBody responseBody=response.body();

                 User_Data userData=new User_Data();

                 userData.setToken(sharedPrefManger.getdata().getToken());
                 userData.setName(responseBody.getName());
                 userData.setUsername(responseBody.getUsername());
                 userData.setImg(responseBody.getImg());
                 userData.setEmail(responseBody.getEmail());
                 userData.setPassword(user.password);

                 sharedPrefManger.save_data(userData);

                 ready_data();

                 startActivity(new Intent(getApplicationContext(),Home.class));
                 finish();
             }
             else
             {
                 progressBar.setVisibility(View.INVISIBLE);
                 Toast.makeText(getApplicationContext(),"sorry,try again later..",Toast.LENGTH_SHORT).show();
             }

            }

            @Override
            public void onFailure(retrofit2.Call<SettingsResponseBody> call, Throwable t)
            {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Failure",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
