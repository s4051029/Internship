package com.mirrorchannelth.internship.service;

import com.mirrorchannelth.internship.net.Connection;
/**
 * Created by boss on 4/19/16.
 */
public class LoginService {
    private Connection connection;
    private String endpoint;

    public LoginService(String endpoint){
       this.endpoint = endpoint;
    }

    public void requestLogin(String username, String password, Connection.OnConnectionCallBackListener listener){
        connection = new Connection(endpoint);

        connection.setDelayed(500);
        connection.addPostData("function", "userLogin");
        connection.addPostData("android_id", "1");
        connection.addPostData("mac_id", "1");
        connection.addPostData("device_type", "A:android");
        connection.addPostData("username", username);
        connection.addPostData("password", password);
        connection.setOnConnectionCallBackListener(listener);
        connection.execute();

    }

}
