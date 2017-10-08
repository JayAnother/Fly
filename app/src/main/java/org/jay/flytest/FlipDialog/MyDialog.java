package org.jay.flytest.FlipDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.jay.flytest.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by <a href="http://blog.csdn.net/student9128">Kevin</a> on 2017/8/17.
 * <p>
 * <h3>Description:</h3>
 * <p/>
 * <p/>
 */
public class MyDialog extends Dialog {

    @BindView(R.id.et_user_name)
    EditText etUserName;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.cb_auto_login)
    CheckBox cbAutoLogin;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.container)
    RelativeLayout container;
    private Context context;

    @BindView(R.id.ll_register)
    LinearLayout llRegister;


    //接口回调传递参数
    private OnClickListenerInterface mListener;
    private View view;
    //
    private String strContent;


    private int centerX;
    private int centerY;
    private int depthZ = 200;
    private int duration = 250;
    private Rotate3dAnimation mFront3DRotation;
    private Rotate3dAnimation mBack3DRotation;

    private boolean isOpen = false;

    public interface OnClickListenerInterface {

        /**
         * 确认,
         */
        void doConfirm();

        /**
         * 取消
         */
//        public void doCancel();
    }

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, String content) {
        super(context);
        this.context = context;
        this.strContent = content;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //去掉系统的黑色矩形边框
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.dialog_my, null);
        setContentView(view);
        ButterKnife.bind(this);
        etPassword.setTypeface(Typeface.DEFAULT);
        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        tvForgetPwd.setOnClickListener(new OnWidgetClickListener());
        btnBack.setOnClickListener(new OnWidgetClickListener());
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
        lp.height = (int) (d.heightPixels * 0.6); // 高度设置为屏幕的0.6
        dialogWindow.setAttributes(lp);
        setCanceledOnTouchOutside(false);
        setCancelable(true);
    }

    public void setClicklistener(OnClickListenerInterface clickListenerInterface) {
        this.mListener = clickListenerInterface;
    }

    private class OnWidgetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            int id = v.getId();
            switch (id) {
                case R.id.tv_forget_pwd:
                    startAnimation();
                    break;
                case R.id.btn_back:
                    startAnimation();
                    break;
            }
        }
    }

    private void startAnimation() {
        centerX = container.getWidth() / 2;
        centerY = container.getHeight() / 2;
        if (mFront3DRotation == null) {
            initOpenAnim();
            initCloseAnim();
        }

        if (mFront3DRotation.hasStarted() && !mFront3DRotation.hasEnded()) {
            return;
        }

        if (mBack3DRotation.hasStarted() && !mBack3DRotation.hasEnded()) {
            return;
        }

        if (isOpen) {
            container.startAnimation(mFront3DRotation);

        } else {
            container.startAnimation(mBack3DRotation);
        }
        isOpen = !isOpen;
    }

    private void initOpenAnim() {
        mFront3DRotation = new Rotate3dAnimation(context,0, 90, centerX, centerY, depthZ, true);
        mFront3DRotation.setDuration(duration);
        mFront3DRotation.setFillAfter(true);
        mFront3DRotation.setInterpolator(new AccelerateInterpolator());
        mFront3DRotation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.GONE);
                llContent.setVisibility(View.VISIBLE);
                //从270到360度，顺时针旋转视图，此时reverse参数为false，达到360度动画结束时视图变得可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(context,270, 360, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                container.startAnimation(rotateAnimation);
            }
        });
    }

    private void initCloseAnim() {
        mBack3DRotation = new Rotate3dAnimation(context,360, 270, centerX, centerY, depthZ, true);
        mBack3DRotation.setDuration(duration);
        mBack3DRotation.setFillAfter(true);
        mBack3DRotation.setInterpolator(new AccelerateInterpolator());
        mBack3DRotation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                llRegister.setVisibility(View.VISIBLE);
                llContent.setVisibility(View.GONE);
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(context,90, 0, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                container.startAnimation(rotateAnimation);
            }
        });
    }
}