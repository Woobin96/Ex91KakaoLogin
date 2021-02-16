package com.wooeun18.ex91kakaologin;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //카카오 SDK 초기화
        KakaoSdk.init(this, "7f36ed68083cc8470f3dc96b0c7498ae");
    }
}
