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

    Context context;
    LayoutInflater  layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.on_boarding_screen_image1,
            R.drawable.on_boarding_screen_image2,
    };

    int headings[] = {
            R.string.on_boarding_screen_text1,
            R.string.on_boarding_screen_text2,
    };

    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE); // Задание сервиса инфлейтора
        View view = layoutInflater.inflate(R.layout.slides_layout, container, false); // Создание экрана из инфлированного экрана slides_layout

        // Привязки
        ImageView imageView = view.findViewById(R.id.slider_image);
        TextView heading = view.findViewById(R.id.slider_heading);

        // Подстановка данных на экран по текущей позиции
        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);

        container.addView(view); // Добавляем экран в контейнер

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
