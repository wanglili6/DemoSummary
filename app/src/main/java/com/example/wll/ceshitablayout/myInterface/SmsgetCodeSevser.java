package com.example.wll.ceshitablayout.myInterface;

import com.example.wll.ceshitablayout.pojoBean.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wll on 2017/12/24.
 * 发送信息参数
 */

public interface SmsgetCodeSevser {
    @GET("sendsms")
    Observable<UserInfo> sendsms(
            @Query("mobile") String phone
    );


}
