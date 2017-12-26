package com.example.wll.ceshitablayout.myInterface;

import com.example.wll.ceshitablayout.pojoBean.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wll on 2017/12/24.
 */

public interface SmsLoginSevser {
    @GET("login")
    Observable<UserInfo> login(
            @Query("phone") String phone,
            @Query("code") String code
    );


}
