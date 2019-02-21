package codeit.com.codeit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import codeit.com.codeit.Builders.RegisterBuilder;
import codeit.com.codeit.Model.User;
import codeit.com.codeit.R;
import codeit.com.codeit.Remote.Common;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Register extends AppCompatActivity implements View.OnClickListener
{

    private EditText name;
    private EditText username;
    private EditText email;
    private EditText password;
    private Button register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name=findViewById(R.id.Register_name);
        username=findViewById(R.id.Register_username);
        email=findViewById(R.id.Register_email);
        password=findViewById(R.id.Register_password);
        register=findViewById(R.id.register_activity_bt);


        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

        if(view == register)
        {
           user_Register();
        }

    }

    private void user_Register()
    {
        String registerName=name.getText().toString();
        String registerUsername=username.getText().toString().replace(" ","");
        String registerEmail=email.getText().toString().replace(" ","");
        String registerPassword=password.getText().toString();

        if(registerName.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your name",Toast.LENGTH_SHORT).show();
            return;
        }


        if(registerUsername.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your user name",Toast.LENGTH_SHORT).show();
            return;
        }

        if(registerEmail.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(registerPassword.isEmpty())
        {
            Toast.makeText(getApplicationContext(),"you must enter your password",Toast.LENGTH_SHORT).show();
            return;
        }

        if(registerPassword.length() < 8)
        {
            Toast.makeText(getApplicationContext(),"you password is too short",Toast.LENGTH_SHORT).show();
            return;
        }


        RegisterBuilder builder = new RegisterBuilder();

        User user=builder.reset().setName(registerName).setUserName(registerUsername)
                .setPassword(registerPassword).setEmail(registerEmail).Build();

        Call<ResponseBody> call= Common.getApi().createUser(user);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200)
                {
                    Toast.makeText(getApplicationContext(),"done",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Login.class));
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error2",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
