package com.example.wsrfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toolbar;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(); // Массив блюд
    RecyclerView recyclerView; // Представление массива блюд, которое будет отображаться на экране
    ArrayList<String> listSearch; // Список названий блюд включающих в себя поисковой запрос
    ListView listSearchView; // Представление списка названий блюд схожих с поисковым запросом
    ArrayAdapter<String> list_adapter; // Адаптер текстового массива, для адаптации массива listSearch в listSearchView
    EditText searchRequest; // Поисковая строка
    Toolbar toolbar; // Тело поисковой строки с списком listSearchView
    RadioGroup radioGroupCategories; // Группа кнопок по категориям
    RadioButton currentRadioButton; // Кнопка текущей категории
    /* ВНИМАНИЕ! Для работы FlexboxLayoutManager, необходимо в файл build.gradle
     для модуля app добавить строчку:
     implementation 'com.google.android.flexbox:flexbox:3.0.0'*/
    FlexboxLayoutManager layoutManager; // Менеджер макетов для настройки макетов
    MenuItemAdapter item_adapter; // Адаптер для адаптации массива menuItems в recyclerView
    // Анимация для появления поиска
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Привязки
        listSearchView = (ListView) findViewById(R.id.search_list); // Привязка к представлению поискового списка на экране
        searchRequest = (EditText) findViewById(R.id.search_bar_edit_text); // Привязка к строке поиска на экране
        radioGroupCategories = (RadioGroup) findViewById(R.id.categories_button_view); // Привязка к группе кнопок по категориям на экране
        recyclerView = findViewById(R.id.list); // Привязываемся к представлению списка блюд на экране
        toolbar = (Toolbar) findViewById(R.id.search_bar); // Привязка к представлению тела поисковой строки

        // Адаптеры
        /* Создаются в месте где они задаются представлению:
         *  адаптер = new адаптео(...);
         *  представление.setAdapret(адаптер); */
        /* Адаптер для связи стандартного макета элемента списка simple_list_item_1 с массивом listSearch. */
        list_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listSearch);
        /* Адаптер для связи макета элементов меню с массивом listSearch */
        item_adapter = new MenuItemAdapter(this, menuItems);

        // Макеты
        // Настройка макета layoutManager для выравнивания блюд по центру
        layoutManager = new FlexboxLayoutManager(this); // Создаём новый макет
        layoutManager.setJustifyContent(JustifyContent.CENTER); // Выравнивание содержимого по центру
        layoutManager.setAlignItems(AlignItems.CENTER); // Вырванивание элементов по центру

        // Применение макетов к представлениям
        recyclerView.setLayoutManager(layoutManager); // Задаём созданный ранее менеджер макетов для выравнивания блюд по центру

        // Обновление списка блюж
        recyclerView.setAdapter(item_adapter); // Задаём созданный адаптер представлению списка блюд

        // Файлы и их конфигурация
        File internalStorageDir = getFilesDir(); // Получаем путь для извлечения локальных данных

        // Предварительные настройки, процедуры и функции
        foods(findViewById(android.R.id.content).getRootView()); // Загружаем данные по категории радио кнопки пища

        // Слушатели
        /* Слушатель изменения текста поискового запроса */
        searchRequest.addTextChangedListener(new TextWatcher() { // Если поисковой запрос меняется
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { // До изменения
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { // При изменении
                updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories); // Обновление представления списка блюд в поиске
                // Изменение массива поискового списка при измменении поискового запроса
                searchInMenuItemAndUpdateSearchList(menuItems, searchRequest.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { // После изменения
            }
        });

        // Прослушиватель выбора элемента из поискового списка
        listSearchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) { // При нажатии
                // Заменить текст поискового запроса текстом выбранного элемента поисковой строки
                searchRequest.setText(listSearchView.getItemAtPosition(position).toString());

            }
        });
    }

    // Созданные методы

    /*    >>Работа с данными<<    */

    // Установка тестовых данных в массив блюд
    private void setInitialData() {
        menuItems.add(new MenuItem("foods", LaunchScreenActivity.version, 0, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("drinks", "1Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("snacks", "2Egg and cucmber...", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("sauce", "3Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "4Egg and cucmber...", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("sauce", "5Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "6Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "7Ultra tomato", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "8Pizza", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem("foods", "9Pizza ultra mix", 1200, R.drawable.dish_default_icon));
    }

    /*    >>Работа с поиском и категориями<<    */
    /*    >>Вспомогательные<<    */

    // Обновление представления списка блюд в поиске
    /* Принимает на вход массив блюд, группу кнопок категорий */
    private void updateSearchListOnCategoriesInMenuItem(ArrayList<MenuItem> menuItems, RadioGroup radioGroupCategories) {
        currentRadioButton = (RadioButton) findViewById(radioGroupCategories.getCheckedRadioButtonId()); // Получаем кнопку текущей категории
        changeMenuItemOnCategory( //
                currentRadioButton.getText().toString().toLowerCase());
        listSearch = new ArrayList<>();
        for (int j = 0; j < menuItems.size(); j++) {
            listSearch.add(menuItems.get(j).getNameDish());
        }
        updateListView(); // Обновление списка поисковых вариантов запроса
    }

    // Изменение меню блюд по категориям
    /* Принимает в себя название категории */
    private void changeMenuItemOnCategory(String category) {
        menuItems = new ArrayList<MenuItem>(); // Обнуляем данные массива блюд
        setInitialData(); // Загружаем все данные
        int i = 0; // создаём переменную индекса
        while (i < menuItems.size()) { // Проходимся по массиву блюд созданным индексом
            if (!menuItems.get(i).getCategory().equals(category)) { // Если блюдо полученное по его индексу не состоит в полученной категории
                menuItems.remove(i); // Удаляем блюдо из массива со здвигом индексов влево
            } else { // Иначе
                i++; // Переходим к следующему индексу
            }
        }
        updateMenuView(); // Обновляем список блюд
    }

    // Поиск в блюдах и обновление поискового списка
    /* Принимает в себя список блюд, поисковой запрос */
    private void searchInMenuItemAndUpdateSearchList(ArrayList<MenuItem> menuItems, String searchRequest) {
        int j = 0; // Создаём переменную индекса поискового списка
        for (int k = 0; k < menuItems.size(); k++) { // Проходимся по массиву блюд
            // Если блюдо не содержит в себе поискового запроса, то
            if (!menuItems.get(k).getNameDish().toLowerCase().contains(searchRequest.toLowerCase())) {
                listSearch.remove(j); // Удалить блюдо со смещением индексов
            } else { // Иначе
                j++; // Переходим к следующему индексу поискового писка
            }
        }
        updateMenuView(); // Обновляем список блюд
    }

    /*    >>Категории<<    */
    public void foods(View view) { // При нажатии на кнопку категории пища
        changeMenuItemOnCategory("foods"); // Обновляем и загружаем блюда введённой категории
        updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories); // Обновляем поисковой список
    }

    public void drinks(View view) { // При нажатии на кнопку категории напитки
        changeMenuItemOnCategory("drinks"); // Обновляем и загружаем блюда введённой категории
        updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories);
    }

    public void snacks(View view) { // При нажатии на кнопку категории закуски
        changeMenuItemOnCategory("snacks"); // Обновляем и загружаем блюда введённой категории
        updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories); // Обновляем поисковой список
    }

    public void sauce(View view) { // При нажатии на кнопку категории соусы
        changeMenuItemOnCategory("sauce"); // Обновляем и загружаем блюда введённой категории
        updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories); // Обновляем поисковой список
    }

    /*    >>Поиск<<    */
    public void openSearchBar(View view) { // При нажатии кнопки открытия поиска
        searchRequest.setText(""); // Обнуляем поисковой запрос при открытии поисковой строки
        // Если до этого был произведён поиск в этой же категории, то список стоит обновить
        updateSearchListOnCategoriesInMenuItem(menuItems, radioGroupCategories); // Обновляем поисковой список
        toolbar.setVisibility(View.VISIBLE); // Делаем поисковую строку видимой
        searchAnimationOpen(); // Анимация открытия после появления тела поиска
    }

    public void closeSearchBar(View view) { // При нажатии кнопки закрытия поиска
        searchAnimationClose(); // Анимация закрытия до появления тела поиска
        // Создаём задержку с помощью анимации не производящей никакие изменения
        animation = AnimationUtils.loadAnimation(this, R.anim.no_anim_duration); // Загружаем анимацию
        toolbar.startAnimation(animation); // Запускаем анимацию для тела поиска, чтобы он не исчезал до запершения анимации поисковой строки
        // После запершении пустой анимации независимо от анимации закрытия, тело поиска исчезнет
        toolbar.setVisibility(View.GONE); // Убираем полностью представление поисковой строки с экрана
    }

    public void search(View view) { // При нажатии кнопки поиска
        int i = 0; // Создаём переменную индекса
        while (i < menuItems.size()) { // Проходимся индексом по массиву блюд
            // Если название блюда независимо от регистра НЕ содержит в себе запрос, удаляем его из текущего списка блюд
            if (!menuItems.get(i).getNameDish().toLowerCase().contains(searchRequest.getText().toString().toLowerCase())) {
                menuItems.remove(i); //Удаление по индексу со смещением
            } else { // Иначе
                i++; // Идём по следующему индексу массива
            }
        }
        updateMenuView(); // Обновляем список блюд
        closeSearchBar(view); // Закрываем тело поиска
    }

    // Для обновления списка блюд
    private void updateMenuView() {
        /* Адаптер для связи макета элементов меню с массивом listSearch */
        item_adapter = new MenuItemAdapter(this, menuItems);
        recyclerView.setAdapter(item_adapter); // Задаём созданный адаптер представлению списка блюд
    }

    // Для обновления поискового списка
    private void updateListView() {
        /* Адаптер для связи стандартного макета элемента списка simple_list_item_1 с массивом listSearch */
        list_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, listSearch);
        listSearchView.setAdapter(list_adapter); // Задаём созданный адаптер представлению поискового списка
    }

    /*    >>Анимации<<    */
    // Анимация появления поиска
    private void searchAnimationOpen() {
        // Анимация поисковой строки
        ConstraintLayout search_bar = (ConstraintLayout) findViewById(R.id.searchConstraintLayout); // Привязываемся к строке поиска
        animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_animation); // Загружаем анимацию
        search_bar.startAnimation(animation); // Запускаем анимацию для строки поиска
        // Анимация поискового списка
        animation = AnimationUtils.loadAnimation(this, R.anim.search_list_animation); // Загружаем анимацию
        listSearchView.startAnimation(animation); // Запускаем анимацию для поискового списка

    }
    // Анимация исчезновения поиска
    private void searchAnimationClose() {
        ConstraintLayout search_bar = (ConstraintLayout) findViewById(R.id.searchConstraintLayout); // Привязываемся к строке поиска
        animation = AnimationUtils.loadAnimation(this, R.anim.search_bar_animation_reverve); // Загружаем анимацию
        search_bar.startAnimation(animation); // Запускаем анимацию для строки поиска
        // Анимация поискового списка
        animation = AnimationUtils.loadAnimation(this, R.anim.search_list_animation_reverce); // Загружаем анимацию
        listSearchView.startAnimation(animation); // Запускаем анимацию для поискового списка
    }
}