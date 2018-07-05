package com.example.wll.ceshitablayout.animation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wll.ceshitablayout.R;
import com.example.wll.ceshitablayout.adapter.ShopCartAdapter;
import com.example.wll.ceshitablayout.bean.AnimationBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 购物车动画
 */
public class ShopCartActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_select)
    TextView tvSelect;
    @BindView(R.id.rl_select)
    RelativeLayout rlSelect;
    @BindView(R.id.rl_title_bg)
    RelativeLayout rlTitleBg;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.img_shop_cart)
    ImageView imgShopCart;
    @BindView(R.id.ll_contants)
    RelativeLayout llContants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_cart);
        ButterKnife.bind(this);
        ivBack.setVisibility(View.VISIBLE);
        rlBack.setVisibility(View.VISIBLE);
        tvTitle.setText("购物车动画");
        rlBack.setOnClickListener(this);
        List<AnimationBean> mlist = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            AnimationBean bena = new AnimationBean();
            bena.setName("商品" + i);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.common_tab);
            bena.setBitmap(bitmap);
            mlist.add(bena);
        }
        recycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ShopCartAdapter shopCartadapter = new ShopCartAdapter(this, mlist, llContants, imgShopCart);
        recycleView.setAdapter(shopCartadapter);
        shopCartadapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
