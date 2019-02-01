package codeit.com.codeit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import codeit.com.codeit.Model.LoginResponseBody;
import codeit.com.codeit.Model.Login_Model;
import codeit.com.codeit.Model.User_Data;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import codeit.com.codeit.Storage.SharedPrefManger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity implements View.OnClickListener
{
    private EditText email;
    private EditText password;
    private Button Login;
    private ProgressBar Login_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email=findViewById(R.id.Login_Email);
        password=findViewById(R.id.Login_Password);
        Login=findViewById(R.id.login_activity_bt);
        Login_progressBar=findViewById(R.id.Login_progressBar);

        Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        if(view == Login)
        {
            LoginUser();
        }

    }

    private void LoginUser()
    {
        Login_progressBar.setVisibility(View.VISIBLE);

        String useremail;
        final String userpassword;

        useremail=email.getText().toString().replace(" ","");
        userpassword=password.getText().toString();


        if(useremail.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your email",Toast.LENGTH_SHORT).show();
            Login_progressBar.setVisibility(View.INVISIBLE);
            return;
        }

        if(userpassword.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your password",Toast.LENGTH_SHORT).show();
            Login_progressBar.setVisibility(View.INVISIBLE);
            return;
        }


        final Login_Model login_model=new Login_Model();

        login_model.setEmail(useremail);
        login_model.setPassword(userpassword);


        Call<LoginResponseBody> call= Common.getApi().loginUser(login_model);
        call.enqueue(new Callback<LoginResponseBody>() {
            @Override
            public void onResponse(Call<LoginResponseBody> call, Response<LoginResponseBody> response)
            {
                if(response.code() == 200)
                {
                    Login_progressBar.setVisibility(View.INVISIBLE);
                      LoginResponseBody loginResponseBody=response.body();
                      User_Data userData=new User_Data();


                      userData.setToken(loginResponseBody.getToken());
                      userData.setName(loginResponseBody.getName());
                      userData.setUsername(loginResponseBody.getUsername());
                      userData.setPassword(userpassword);
                      userData.setImg(loginResponseBody.getImg());
                      userData.setEmail(loginResponseBody.getEmail());


                    SharedPrefManger sharedPrefManger=SharedPrefManger.getInstance(getApplicationContext());
                    sharedPrefManger.save_data(userData);

                    startActivity(new Intent(getApplicationContext(),Home.class));
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                    Login_progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<LoginResponseBody> call, Throwable t)
            {
                Login_progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
