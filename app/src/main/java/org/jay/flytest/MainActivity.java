package org.jay.flytest;

import org.jay.flytest.animation.ViewHelper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    public DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        setDrawerRightEdgeSize(mDrawerLayout,this,0.2f);
        initDrawerEvents();
    }


    private void initDrawerEvents() {
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerStateChanged(int newState) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View mContent = mDrawerLayout.getChildAt(0);
                View mMenu = drawerView;
                float scale = 1 - slideOffset;
                float scale2 = 1 - slideOffset;
                float rightScale = 0.8f + scale * 0.2f;
                Log.d("jay", "onDrawerSlide: [rightScale]="+rightScale);
                float leftScale = 1 + 0.2f * scale;
                Log.d("jay", "onDrawerSlide: [leftScale]="+leftScale);
                //zoom drawer
                ViewHelper.setScaleX(mMenu, leftScale);
                ViewHelper.setScaleY(mMenu, leftScale);
                ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
                //zoom window
                ViewHelper.setTranslationX(mContent, mMenu.getMeasuredWidth() * (1 - scale));
                ViewHelper.setPivotX(mContent, 0);
                ViewHelper.setPivotY(mContent, mContent.getMeasuredHeight() / 2);
                mContent.invalidate();
                ViewHelper.setScaleX(mContent, rightScale);
                ViewHelper.setScaleY(mContent, rightScale);


//                View mContent = mDrawerLayout.getChildAt(0);
//                View mMenu = drawerView;
//                float scale = 1-slideOffset;
//                float rightScale = 0.6f+scale*0.4f;  //1~0.6
//                float leftScrale = 0.5f+slideOffset*0.5f;
//                mMenu.setAlpha(leftScrale);
//                mMenu.setScaleX(leftScrale);
//                mMenu.setScaleY(leftScrale);
//                mContent.setPivotX(0);
//                mContent.setPivotY(mContent.getHeight()/2);
//                mContent.setScaleX(rightScale);
//                mContent.setScaleY(rightScale);
//                mContent.setTranslationX(mMenu.getWidth()*slideOffset);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
            }
        });
    }
    /**
     * 设置drawerLayout滑动边距
     * @param drawerLayout
     * @param activity
     * @param proportion
     */
    public  void setDrawerRightEdgeSize(DrawerLayout drawerLayout,Activity activity,float proportion){
        if(drawerLayout==null||activity==null){
            return;
        }
        try {
            Field field = drawerLayout.getClass().getDeclaredField("mLeftDragger");
            field.setAccessible(true);
            ViewDragHelper mLeftDragger = (ViewDragHelper) field.get(drawerLayout);
            Field field1 = mLeftDragger.getClass().getDeclaredField("mEdgeSize");
            field1.setAccessible(true);
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            field1.setInt(mLeftDragger, (int) (metrics.widthPixels*proportion));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
