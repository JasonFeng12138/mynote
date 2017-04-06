package com.example.feng;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feng.mynote.R;

/**
 * Created by feng on 16/7/1.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    private SharedPreferences pref;

    private SharedPreferences.Editor editor;

    private EditText userEdit;

    private EditText passEdit;

    private Button login;

    private CheckBox rememberPassword;

    private TextView nouser;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_login,container,false);
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userEdit = (EditText) v.findViewById(R.id.inputusername);
        passEdit = (EditText) v.findViewById(R.id.inputpassword);
        nouser = (TextView) v.findViewById(R.id.no_user_login);
        rememberPassword = (CheckBox) v.findViewById(R.id.remember_pass);
        login = (Button) v.findViewById(R.id.login);
        boolean isRemember = pref.getBoolean("remember_password",false);
        if(isRemember){
            String user = pref.getString("user","");
            String password = pref.getString("pass","");
            userEdit.setText(user);
            passEdit.setText(password);
            rememberPassword.setChecked(true);
        }
        login.setOnClickListener(this);
        nouser.setOnClickListener(this);
        return v;

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                String user = userEdit.getText().toString();
                String password = passEdit.getText().toString();
                if(user.equals("admin")&& password.equals("123456")){
                    editor = pref.edit();
                    if(rememberPassword.isChecked()){
                        editor.putBoolean("remember_password",true);
                        editor.putString("user",user);
                        editor.putString("pass",password);
                    }else{
                        editor.clear();
                    }
                    editor.commit();
                    Intent intent = new Intent();
                    intent.putExtra("data", userEdit.getText().toString());
                    getActivity().setResult(2, intent);
                    getActivity().finish();

                }
                else{
                    Toast.makeText(getActivity(), "密码或账户信息错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.no_user_login:
                Intent intent = new Intent();
                intent.putExtra("data","离线");
                getActivity().setResult(2, intent);
                getActivity().finish();
                break;
        }
    }
}
