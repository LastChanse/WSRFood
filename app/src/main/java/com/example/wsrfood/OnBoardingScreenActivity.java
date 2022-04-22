package com.example.wsrfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OnBoardingScreenActivity extends AppCompatActivity {

    //Переменные
    ViewPager viewPager; // Слайдер
    LinearLayout dotsLayout; // Точки
    SliderAdapter sliderAdapter; // Контент слайдера
    TextView[] dots; // Текст
    Button letsGetStarted; // Кнопка на последнем слайде
    Animation animation; // Анимация для появления кнопок
    int currentPos; // Текущий слайд
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Разворачиваем страницу на весь экран
        setContentView(R.layout.activity_on_boarding_screen);

        // Привязки
        viewPager = findViewById(R.id.slider); // Получаем объект слайдера
        dotsLayout = findViewById(R.id.dots); // Получаем объект точек
        letsGetStarted = findViewById(R.id.get_started_btn); // Получаем объект последней кнопки

        // Подключаем данные для слайдера
        sliderAdapter = new SliderAdapter(this); // Создаём объект слайдера для текущего представления
        viewPager.setAdapter(sliderAdapter); // Задаём созданный слайдер для текущего представления

        //Точки
        addDots(0); // делаем так чтобы светлой была первая точка, которая первая
        viewPager.addOnPageChangeListener(changeListener); // Задаём слушатель изменений страницы
    }

    public void skip(View view) { // Если нажата кнопка пропустить, то переходим на другой экран
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void next(View view) {
        if (currentPos == 4) {
            currentPos = 0;
        } else {
            currentPos++;
        }
        viewPager.setCurrentItem(currentPos);
    } // Когда нажата кнопка далее, то переходим на следующий слайд

    // Процедура установки позиции точек
    private void addDots(int position) {

        dots = new TextView[4]; // Создаём массив из 4х точек
        dotsLayout.removeAllViews(); // Удаляем все точки на экране

        for (int i = 0; i < dots.length; i++) { // Проходимся по массиву точек и создаём их изображения
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]); // Добавляем их на экран
        }

        if (dots.length > 0) { // Если точки есть в массиве точек, то задаём им прозрачный цвет
            dots[position].setTextColor(getResources().getColor(R.color.no_white_25));
        }

    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() { // Если страница изменилась
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos=position;
            if (position == 0) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 1) {
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else if (position == 2) {
                if (flag == true) {
                    animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.bottom_anim_reverce);
                    letsGetStarted.setAnimation(animation);
                    flag = false;
                }
                letsGetStarted.setVisibility(View.INVISIBLE);
            } else {
                flag = true;
                animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.bottom_anim);
                letsGetStarted.setAnimation(animation);
                letsGetStarted.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}