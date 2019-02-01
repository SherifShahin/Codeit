package codeit.com.codeit.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import codeit.com.codeit.R;

public class Splash_Screen extends AppCompatActivity {

    Button Login;
    Button Register;
    ImageView logo;

    Animation from_bottom;
    Animation from_top;
    Animation from_right;
    Animation from_left;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__screen);

        Login=findViewById(R.id.log_in);
        Register=findViewById(R.id.register);
        logo=findViewById(R.id.logo);

        from_right=AnimationUtils.loadAnimation(this,R.anim.from_right);
        from_left=AnimationUtils.loadAnimation(this,R.anim.from_left);
        from_top= AnimationUtils.loadAnimation(this,R.anim.from_top);

        logo.setAnimation(from_top);
        Login.setAnimation(from_right);
        Register.setAnimation(from_left);


        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

    }
}
