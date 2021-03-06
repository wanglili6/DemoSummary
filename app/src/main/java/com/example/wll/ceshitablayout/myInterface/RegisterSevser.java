package com.example.wll.ceshitablayout.myInterface;

import com.example.wll.ceshitablayout.pojoBean.RegisterInfo;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wll on 2017/12/24.
 * 注册参数
 */

public  interface RegisterSevser {
    @GET("register" )
    Observable<RegisterInfo> register(
            @Query("name") String username,
            @Query("password") String password
    );


}
