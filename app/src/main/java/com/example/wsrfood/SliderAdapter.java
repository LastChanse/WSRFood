package com.example.wsrfood;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context; // Контекст (this или НазваниеКлассаАктивности.this)
    LayoutInflater  layoutInflater; // Сборщик представлений из макетов

    public SliderAdapter(Context context) { // Конструктор слайдера принимает контекст при создании
        this.context = context;
    }


    // Данные слайдера (2 слайда поэтому в каждом массиве по 2 ячейки)
    int images[] = { // Изображения на слайдах
            R.drawable.on_boarding_screen_image1,
            R.drawable.on_boarding_screen_image2,
    };

    int headings[] = { // Текст заголовков на слайдах
            R.string.on_boarding_screen_text1,
            R.string.on_boarding_screen_text2,
    };

    @Override
    public int getCount() { // Получение количеста слайдов
        return headings.length; // Возвращает количество ячеек в массиве заголовков
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) { // Проверка создано ли представление от объекта
        return view == (ConstraintLayout) object; // Всегда возвращает true
    } // Описание: При true отображает все страницы или же слайды слайдера, а при false не отображает

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) { // Создание экземпляра

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE); // Задание полученного из контекста сервиса инфлейтора
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false); // Создание экрана из макета slides_layout

        // Привязки
        ImageView imageView = view.findViewById(R.id.slider_image); // Привязка к изображению из макета slides_layout
        TextView heading = view.findViewById(R.id.slider_heading); // Привязка к заголовку из макета slides_layout

        // Подстановка данных на экран по текущей позиции
        imageView.setImageResource(images[position]); // Подстановка изображений из данных слайдера
        heading.setText(headings[position]); // Подстановка заголовков из данных слайдера

        container.addView(view); // Добавляем экран в контейнер

        return view; // Возвращаем собранное представление из макета slides_layout
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) { // Удаление экземпляра
        container.removeView((ConstraintLayout) object); // Удаление объекта представления в контейнере
    }
}
