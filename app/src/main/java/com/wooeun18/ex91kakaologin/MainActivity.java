package com.wooeun18.ex91kakaologin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kakao.sdk.auth.LoginClient;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.util.Utility;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    TextView tvNicname, tvEmail;
    CircleImageView ivProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvNicname= findViewById(R.id.tv_nic);
        tvEmail= findViewById(R.id.tv_email);
        ivProfile= findViewById(R.id.iv);

        //키 해시값 얻어와서 Logcat 창에 출력하기 - 카카오 개발자 사이트에서 키 해시값 등록해야해서 .
        String KeyHash= Utility.INSTANCE.getKeyHash(this);
        Log.i("KeyHash", KeyHash);
    }

    public void clickLogin(View view) {
        //카카오 계정으로 로그인 하기 .
        LoginClient.getInstance().loginWithKakaoAccount(this, new Function2<OAuthToken, Throwable, Unit>() {
            @Override //invoke 발동
            public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
                if (oAuthToken != null){ //로그인 정보객체가 있다면
                    Toast.makeText(MainActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();

                    //로그인 한 계정 정보 얻어오기
                    UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
                        @Override
                        public Unit invoke(User user, Throwable throwable) {
                            if (user != null){
                                long id= user.getId(); // 카카오 회원 번호

                                //필수동의 항목의 회원프로필 [닉네임 , 프로필 이미지]
                                String nicname= user.getKakaoAccount().getProfile().getNickname();
                                String profileImageUrl= user.getKakaoAccount().getProfile().getThumbnailImageUrl();

                                //선택동의 항목 지정한 이메일
                                String email= user.getKakaoAccount().getEmail();

                                tvNicname.setText(nicname);
                                tvEmail.setText(email);
                                Glide.with(MainActivity.this).load(profileImageUrl).into(ivProfile);

                                //다음에 접속할때 로그인 다시 하지 않으려면 SharePreference 에 로그인 정보 저장

                            }else {
                                Toast.makeText(MainActivity.this, "사용자 정보 요청 실패 \n"+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            return null;
                        }
                    });

                }else {
                    Toast.makeText(MainActivity.this, "로그인 실패 \n" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
                return null;

            }
        });
    }//clcik

    public void clickLogout(View view) {
        //로그아웃 요청
        UserApiClient.getInstance().logout(new Function1<Throwable, Unit>() {
            @Override
            public Unit invoke(Throwable throwable) {
                if (throwable != null){
                    Toast.makeText(MainActivity.this, "실패"+ throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();

                    //로그인 회원 정보들  모두 초기화
                    tvNicname.setText("닉네임");
                    tvEmail.setText("이메일");
                    Glide.with(MainActivity.this).load(R.mipmap.ic_launcher).into(ivProfile);
                }

                return null;
            }
        });
    }
}//Main