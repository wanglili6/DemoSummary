package com.example.wll.ceshitablayout.myInterface;

import com.example.wll.ceshitablayout.pojoBean.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wll on 2017/12/24.
 * 登陆参数
 */

public  interface LoginSevser {
    @GET("login" )
    Observable<UserInfo> login(
            @Query("name") String username,
            @Query("password") String password
    );


}
