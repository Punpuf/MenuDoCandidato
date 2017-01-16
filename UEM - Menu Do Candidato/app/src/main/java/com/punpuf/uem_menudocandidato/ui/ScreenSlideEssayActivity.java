package com.punpuf.uem_menudocandidato.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.punpuf.uem_menudocandidato.R;
import com.punpuf.uem_menudocandidato.data.Contract;
import com.punpuf.uem_menudocandidato.model.Exam;
import com.punpuf.uem_menudocandidato.model.Miscellaneous;

import java.io.File;
import java.io.FileOutputStream;

public class ScreenSlideEssayActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 2;

    private GestureDetectorCompat gestureDetector;
    private File cachePath;

    private boolean windowIsFullscreen;
    private boolean menuClicked;

    private ViewPager viewPager;
    private ImageButton navLeftBtn;
    private ImageButton navRightBtn;

    private Exam exam;
    private Miscellaneous miscellaneous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide_essay);

        //makes status bar translucent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        //listener for changes in UI visibility (fullscreen/ not)
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                windowIsFullscreen = !((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0);
            }
        });

        //initializes the view pager
        viewPager = (ViewPager) findViewById(R.id.screen_slide_view_pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        //sets current item to the img selected in the Home Activity
        if(getIntent().getStringExtra(Intent.EXTRA_TEXT).equals("2")){
            viewPager.setCurrentItem(1, false);
        }

        gestureDetector = new GestureDetectorCompat(this, new CustomGestureListener());
        windowIsFullscreen = false;

        //on click listeners for nav btns
        navLeftBtn = (ImageButton) findViewById(R.id.screen_slide_left_nav_btn);
        navRightBtn = (ImageButton) findViewById(R.id.screen_slide_right_nav_btn);
        navLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });
        navRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        cachePath = new File(getCacheDir(), "images");
        cachePath.mkdirs();

        exam = new Exam(
                getContentResolver().query(
                        Contract.ExamEntry.CONTENT_URI,
                        Contract.ExamEntry.PROJECTION,
                        null,
                        null,
                        null
                )
        );
        miscellaneous = new Miscellaneous(
                getContentResolver().query(
                        Contract.MiscellaneousEntry.CONTENT_URI,
                        Contract.MiscellaneousEntry.PROJECTION,
                        null,
                        null,
                        null
                )
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.essay_menu, menu);
        return true;
    }

    //notifies gesture listener that the touch came from opening or closing the menu
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        menuClicked = true;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) return false;
        switch (item.getItemId()){
            case R.id.essay_menu_share:
                shareEssayImage();
                break;
            case R.id.essay_menu_print:
                printEssayImage();
                break;
        }
        return true;
    }

    /**
     * identifies all touch events on activity
     * @param event the motion event
     * @return default return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }

    /**
     * compresses bitmap from iv and stores them into cache dir
     * passes stored img location to intent and starts it
     */
    private void shareEssayImage(){
        new Thread(new Runnable(){
            @Override
            public void run() {

                try{
                    FileOutputStream outputStream = new FileOutputStream(cachePath + "/image.png");
                    Bitmap essayBitmap;
                    if (viewPager.getCurrentItem() == 0) essayBitmap = viewPager.getChildAt(0).findViewById(R.id.essay_fragment_iv).getDrawingCache();
                    else essayBitmap = viewPager.getChildAt(1).findViewById(R.id.essay_fragment_iv).getDrawingCache();
                    essayBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.close();

                    File imageFile = new File(cachePath, "image.png");
                    Uri content = FileProvider.getUriForFile(getApplicationContext(), "com.punpuf.uem_menudocandidato", imageFile);
                    if(content != null){
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(content, getContentResolver().getType(content));
                        intent.putExtra(Intent.EXTRA_STREAM, content);
                        startActivity(Intent.createChooser(intent, getString(R.string.essay_share_choose_app)));
                    }

                } catch (Exception e) { e.printStackTrace(); }

            }
        }).start();
    }

    //gets bitmap from current iv and prints it
    private void printEssayImage(){
        PrintHelper essayPrinter = new PrintHelper(this);
        essayPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        Bitmap essayBitmap;
        if(viewPager.getCurrentItem() == 0) essayBitmap = viewPager.getChildAt(0).findViewById(R.id.essay_fragment_iv).getDrawingCache();
        else essayBitmap = viewPager.getChildAt(1).findViewById(R.id.essay_fragment_iv).getDrawingCache();
        String printJobName = getString(R.string.essay_print_job_name, viewPager.getCurrentItem() + 1, exam.examName);
        essayPrinter.printBitmap(printJobName, essayBitmap);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        ScreenSlidePagerAdapter(FragmentManager manager){ super(manager); }

        @Override
        public Fragment getItem(int position) {
            EssayPageFragment essayPageFragment = new EssayPageFragment();
            essayPageFragment.setPosition(position + 1);
            return essayPageFragment;
        }

        @Override
        public int getCount() { return NUM_PAGES; }

        @Override
        public CharSequence getPageTitle(int position) {
            String genre;
            if(position == 0) genre = miscellaneous.miscellaneousComposingGenre1;
            else genre = miscellaneous.miscellaneousComposingGenre2;
            return getString(R.string.essay_view_pager_genre_title, genre);
        }
    }

    private class CustomGestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            //checks if the touch was on the menu or it's items
            //if true cancels further action

            if(menuClicked){
                menuClicked = false;
                return true;
            }
            View shareMenuItem = findViewById(R.id.essay_menu_share);
            View printMenuItem = findViewById(R.id.essay_menu_print);
            if(shareMenuItem != null && isPointInsideView(event.getX(), event.getY(), shareMenuItem) || printMenuItem != null && isPointInsideView(event.getX(), event.getY(), printMenuItem)) return  true;
            if(isPointInsideView(event.getX(), event.getY(), navLeftBtn) || isPointInsideView(event.getX(), event.getY(), navRightBtn)) return true;

            //handles UI changes when in multi window mode
            if(isInMultiWindowMode()){
                if(navLeftBtn.getVisibility() == View.GONE) showSystemUI();
                else hideSystemUI();
                return true;
            }

            //hides or shows UI depending of windows current state
            if(windowIsFullscreen) showSystemUI();
            else hideSystemUI();
            return true;
        }

        //checks if touch was within a certain view
        private boolean isPointInsideView(float x, float y, View view) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            int viewX = location[0];
            int viewY = location[1];

            return ((x > viewX && x < (viewX + view.getWidth())) &&
                    (y > viewY && y < (viewY + view.getHeight())));
        }


        private void hideSystemUI() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                                | View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
            else{
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                                | View.SYSTEM_UI_FLAG_FULLSCREEN); // hide status bar
            }

            navLeftBtn.setVisibility(View.GONE);
            navRightBtn.setVisibility(View.GONE);
        }

        private void showSystemUI() {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

            navLeftBtn.setVisibility(View.VISIBLE);
            navRightBtn.setVisibility(View.VISIBLE);
        }
    }

}
