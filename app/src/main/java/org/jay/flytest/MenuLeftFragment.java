package org.jay.flytest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MenuLeftFragment extends Fragment {

    @BindView(R.id.one)
    ImageView mOne;

    @BindView(R.id.ll_01)
    RelativeLayout mLl01;

    @BindView(R.id.two)
    ImageView mTwo;

    @BindView(R.id.ll_02)
    RelativeLayout mLl02;

    @BindView(R.id.three)
    ImageView mThree;

    @BindView(R.id.ll_03)
    RelativeLayout mLl03;

    @BindView(R.id.four)
    ImageView mFour;

    @BindView(R.id.ll_04)
    RelativeLayout mLl04;

    @BindView(R.id.five)
    ImageView mFive;

    @BindView(R.id.ll_05)
    RelativeLayout mLl05;

    Unbinder unbinder;

    private MainActivity mainActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_menu, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    private void closeDrawer() {
        if (mainActivity != null && mainActivity.mDrawerLayout != null) {
            if (mainActivity.mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mainActivity.mDrawerLayout.closeDrawer(GravityCompat.START);
            }
        }
    }

    @OnClick({R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.ll_04, R.id.ll_05})
    public void onViewClicked(View view) {
        closeDrawer();
        switch (view.getId()) {
            case R.id.ll_01:
                Toast.makeText(getActivity(), "id=" + view.getId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_02:
                Toast.makeText(getActivity(), "id=" + view.getId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_03:
                Toast.makeText(getActivity(), "id=" + view.getId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_04:
                Toast.makeText(getActivity(), "id=" + view.getId(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_05:
                Toast.makeText(getActivity(), "id=" + view.getId(), Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
