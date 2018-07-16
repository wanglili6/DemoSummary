package com.example.wll.ceshitablayout.myInterface;

import com.example.wll.ceshitablayout.pojoBean.KLineInfo;
import com.example.wll.ceshitablayout.pojoBean.UserInfo;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wll on 2018/7/16.
 */

public interface KLineSevser {
    @GET("kline")
    Observable<KLineInfo> kline(
            @Query("symbol") String symbol,
            @Query("period") String period,
            @Query("size") String size
    );
}
