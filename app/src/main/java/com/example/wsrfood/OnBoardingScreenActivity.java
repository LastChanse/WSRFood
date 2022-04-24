package com.example.wsrfood;

import androidx.annotation.AnimRes;
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

    // Слайдер
    ViewPager viewPager;

    // Точки
    LinearLayout dotsLayout;

    // Контент слайдера
    SliderAdapter sliderAdapter;

    // Текст
    TextView[] dots;

    // Кнопка входа
    Button SingIn;

    // Кнопка регистрации
    Button SingUp;

    // Анимация для появления кнопок
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Разворачиваем страницу на весь экран
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    public void signIn(View view) { // Если нажата кнопка пропустить, то
        startActivity(new Intent(this, MainActivity.class)); // переходим на экран входа
    }

    public void signUp(View view) { // Если нажата кнопка пропустить, то
        startActivity(new Intent(this, MainActivity.class)); // переходим на экран регистрации
    }

    // Процедура установки позиции точек
    private void addDots(int position) {

        dots = new TextView[2]; // Создаём массив из 2х точек
        dotsLayout.removeAllViews(); // Удаляем все точки на экране, чтобы оставить место для новых

        for (int i = 0; i < dots.length; i++) { // Проходимся по массиву точек и создаём изображения точек
            dots[i] = new TextView(this); // Создаём новое представление текста
            dots[i].setText(Html.fromHtml("&#8226;")); // задаём ему как текст кружок

            // Задаём размер текста в dp вместо px так как кружки будут изменятся с плотностью экрана
            dots[i].setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));

            dotsLayout.addView(dots[i]); // Добавляем их на экран
        }

        if (dots.length > 0) { // Если точки есть в массиве точек, то задаём им прозрачный цвет
            for (int i = 0; i < dots.length; i++) { // проходимся по всем точкам в массива
                if (i != position) { // Если текущая точка не совпадает с позицией слайда, то
                    // Задаём цвет точки как непрозрачный на 25% белый
                    dots[i].setTextColor(getResources().getColor(R.color.white_alfa_0_25));
                } else { // Иначе
                    // Задаём цвет точки как белый
                    dots[i].setTextColor(getResources().getColor(R.color.white));
                }
            }
        }

    }

    private void showButton(Button btn, @AnimRes int id) { // Процедура делает кнопку видимой и осязаемой с введённой анимацией
        if (btn.getVisibility() != View.VISIBLE) { // Анимация не проигрывается если кнопка УЖЕ видима
            animation = AnimationUtils.loadAnimation(this, id); // Загружаем анимацию
            btn.startAnimation(animation); // Запускаем анимацию для кнопки
        }
        btn.setClickable(true); // Делаем кнопку осязаемой
        btn.setVisibility(View.VISIBLE); // Делаем кнопку видимой
    }

    private void hideButton(Button btn, @AnimRes int id) { // Процедура делает кнопку невидимой и неосязаемой с введённой анимацией
        if (btn.getVisibility() == View.VISIBLE) { // Анимация не проигрывается если кнопка УЖЕ не видима
            animation = AnimationUtils.loadAnimation(this, id); // Загружаем анимацию
            btn.startAnimation(animation); // Запускаем анимацию для кнопки
        }
        btn.setClickable(false); // Делаем кнопку не осязаемой
        btn.setVisibility(View.INVISIBLE); // Делаем кнопку не видимой
    }

    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() { // Если страница изменилась

        // Если страница слайда прокручена (пролучает позицию с которой начинается прокрутка, степень прокрутки, степень прокрутки в пикселях)
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

        @Override
        public void onPageSelected(int position) { // Если выбран слайд
            addDots(position); // Рисуем точки с учётом текущей позиции
            if (position == 0) { // Если выбран первый слайд
                hideButton(SingIn, R.anim.left_anim_reverce); // Убираем кнопку входа
                hideButton(SingUp, R.anim.right_anim_reverce); // Убираем кнопку регистрации
            } else { // Если выбран другой слайд
                showButton(SingIn, R.anim.left_anim); // Добавляем кнопку входа
                showButton(SingUp, R.anim.right_anim); // Добавляем кнопку регистрации
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {} // При изменении состояния прокрутки
    };
}