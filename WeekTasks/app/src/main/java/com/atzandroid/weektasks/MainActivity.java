package com.atzandroid.weektasks;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static final int NONE = -1, SAT = 0, SUN = 1, MON = 2, TUE = 3, WED = 4, THU = 5, FRI = 6;
    private static int[] days_buttons_ids = {R.id.day_sat, R.id.day_sun, R.id.day_mon, R.id.day_tue, R.id.day_wed, R.id.day_thu, R.id.day_fri};
    private static Button[] day_btns;
    private static final String[] days = {"Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri"};
    private static int today = NONE;
    private static int selected_day = NONE;
    private static final String TODAY = "Today";

    LinearLayout menu_layout;
    ImageView options_menu_btn;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//            getWindow().setStatusBarColor(getResources().getColor(R.color.tasky_dark));
        initDayBtns();
        setToday();
        setDayButtonListeners();
        initOptionsMenu();
    }

    private void initOptionsMenu(){
        menu_layout = findViewById(R.id.options_menu_layout);
        options_menu_btn = findViewById(R.id.options_menu_btn);
        menu_layout.setVisibility(View.GONE);
        options_menu_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_layout.getVisibility() == View.GONE)
                    openMenu();
                else
                    closeMenu();
            }
        });
    }

    private void initDayBtns() {
        day_btns = new Button[7];
        for (int day=SAT; day<= FRI; day++){
            day_btns[day] = findViewById(days_buttons_ids[day]);
        }
    }

    private void setToday(){
//        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//        if (day == 7)
//            day = 0;
        int day = MON; //just for testing
        if (today != NONE)
            day_btns[today].setText(days[day]);
        day_btns[day].setText(TODAY);
        select_button(day);
        today = day;
        updateFragment(today);
    }

    private void setDayButtonListeners(){
        Button btn;
        for(int day=SAT; day<=FRI; day++){
            btn = day_btns[day];
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt((String) v.getTag());
                    if(temp != selected_day) {
                        select_button(temp);
                        updateFragment(temp);
                    }
                }
            });
        }
    }

    private void updateFragment(int day) {
        transaction = getSupportFragmentManager().beginTransaction();
        if(day == today) {
            transaction.replace(R.id.day_activities_fragment, new TodayFragment());
            transaction.commit();
        }
        else if(day < today){
            transaction.replace(R.id.day_activities_fragment, new PrevDayFragment());
            transaction.commit();
        }
        else{
            transaction.replace(R.id.day_activities_fragment, new NextDayFragment());
            transaction.commit();
        }
    }

    private void select_button(int index){
        if (selected_day != NONE)
            deselect_button(selected_day);
        Button btn = day_btns[index];
        btn.setBackgroundResource(R.drawable.day_button_selected);
        btn.setTextColor(getResources().getColor(R.color.white));
        selected_day = index;
    }

    private void deselect_button(int index){
        Button btn = day_btns[index];
        btn.setBackgroundResource(R.drawable.day_button_unselected);
        btn.setTextColor(getResources().getColor(R.color.black));
    }

    private void openMenu(){
        final View view = (View) menu_layout;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -3000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                options_menu_btn.setScaleY(-1);
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.setDuration(10);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(
                objectAnimator,
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(250)
        );
        animatorSet.start();
    }

    private void closeMenu(){
        AnimatorSet animatorSet = new AnimatorSet();
        final View view = (View) menu_layout;
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "translationY", -3000);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
                options_menu_btn.setScaleY(1);
            }
            @Override
            public void onAnimationCancel(Animator animation) {
            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        objectAnimator.setDuration(250);

        animatorSet.playSequentially(
                ObjectAnimator.ofFloat(view, "translationY", 0f).setDuration(10),
                objectAnimator
        );
        animatorSet.start();
    }

}
