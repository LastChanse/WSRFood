package com.example.wsrfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
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
    Button SingIn; // Кнопка входа
    Button SingUp; // Кнопка регистрации
    Animation animation; // Анимация для появления кнопок
    int currentPos; // Текущий слайд
    boolean flag = false; // Флаг определяющий порядок анимаций появления и исчезновения кнопок на последнем слайде

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // Разворачиваем страницу на весь экран
        setContentView(R.layout.activity_on_boarding_screen);

        // Привязки
        viewPager = findViewById(R.id.slider); // Получаем объект слайдера
        dotsLayout = findViewById(R.id.dots); // Получаем объект точек
        SingIn = findViewById(R.id.sign_in_btn); // Получаем объект кнопки входа
        SingUp = findViewById(R.id.sign_up_btn); // Получаем объект кнопки регистрации

        // Подключаем данные для слайдера
        sliderAdapter = new SliderAdapter(this); // Создаём объект слайдера для текущего представления
        viewPager.setAdapter(sliderAdapter); // Задаём созданный слайдер для текущего представления

        //Точки
        addDots(0); // делаем так чтобы светлой была первая точка, которая первая
        viewPager.addOnPageChangeListener(changeListener); // Задаём слушатель изменений страницы
    }

    public void signIn(View view) { // Если нажата кнопка пропустить, то переходим на другой экран
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void signUp(View view) {
        if (currentPos == 1) {
            currentPos = 0;
        } else {
            currentPos++;
        }
        viewPager.setCurrentItem(currentPos);
    } // Когда нажата кнопка далее, то переходим на следующий слайд

    // Процедура установки позиции точек
    private void addDots(int position) {

        dots = new TextView[2]; // Создаём массив из 2х точек
        dotsLayout.removeAllViews(); // Удаляем все точки на экране

        for (int i = 0; i < dots.length; i++) { // Проходимся по массиву точек и создаём их изображения
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));

            dotsLayout.addView(dots[i]); // Добавляем их на экран
        }

        if (dots.length > 0) { // Если точки есть в массиве точек, то задаём им прозрачный цвет
            for (int i = 0; i < dots.length; i++) {
                if (i != position) {
                    dots[i].setTextColor(getResources().getColor(R.color.white_alfa_0_25));
                } else {
                    dots[i].setTextColor(getResources().getColor(R.color.white));
                }
            }
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
                if (flag == true) {
                    animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.right_anim_reverce);
                    SingUp.setAnimation(animation);
                    animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.left_anim_reverce);
                    SingIn.setAnimation(animation);
                    flag = false;
                }
                SingUp.setVisibility(View.INVISIBLE);
                SingIn.setVisibility(View.INVISIBLE);
            } else {
                flag = true;
                animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.right_anim);
                SingUp.setAnimation(animation);
                animation = AnimationUtils.loadAnimation(OnBoardingScreenActivity.this, R.anim.left_anim);
                SingIn.setAnimation(animation);
                SingUp.setVisibility(View.VISIBLE);
                SingIn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}