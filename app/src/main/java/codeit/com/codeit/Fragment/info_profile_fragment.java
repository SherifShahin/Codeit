package codeit.com.codeit.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import codeit.com.codeit.Activity.Settings;
import codeit.com.codeit.Activity.Splash_Screen;
import codeit.com.codeit.Model.User_Data;
import codeit.com.codeit.R;
import codeit.com.codeit.Storage.SharedPrefManger;

public class info_profile_fragment  extends Fragment implements View.OnClickListener
{

    private Dialog dialog;

    private TextView name;
    private TextView email;
    private TextView password;

    private Button update;
    private Button logout;
    private Button yes_logout;
    private Button no_logout;

    private ImageView close_popup;

    private SharedPrefManger sharedPrefManger;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.info_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        sharedPrefManger=SharedPrefManger.getInstance(getActivity());

        dialog=new Dialog(getActivity());
        name=view.findViewById(R.id.profile_info_name);
        email=view.findViewById(R.id.profile_info_email);
        password=view.findViewById(R.id.profile_info_password);
        update=view.findViewById(R.id.info_update_bt);
        logout=view.findViewById(R.id.logout_bt);

        ready_Data();
        update.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    private void ready_Data()
    {
        User_Data userData=new User_Data();
        userData=sharedPrefManger.getdata();

        name.setText(userData.getName());
        email.setText(userData.getEmail());
        password.setText(userData.getPassword());
    }


    @Override
    public void onClick(View view)
    {
        if(view == update)
        {
            startActivity(new Intent(getActivity(), Settings.class));
            getActivity().finish();
        }

        if(view == logout)
        {
            showpopup();
        }

    }

    private void showpopup()
    {
        dialog.setContentView(R.layout.logout_popup);
        yes_logout=dialog.findViewById(R.id.logout_popup_bt);
        close_popup=dialog.findViewById(R.id.logout_popup_close);
        no_logout=dialog.findViewById(R.id.no_logout_popup_bt);


        dialog.setCancelable(false);

        close_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                sharedPrefManger.clear();
                startActivity(new Intent(getActivity(), Splash_Screen.class));
                getActivity().finish();
            }
        });

        no_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
}
