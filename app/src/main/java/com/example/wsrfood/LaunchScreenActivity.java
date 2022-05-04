package com.example.wsrfood;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

@SuppressLint("CustomSplashScreen")
public class LaunchScreenActivity extends AppCompatActivity {

    public final String[] resIs = {""}; // Переменная для хранения ответа от сервера

    public static String version = new String(""); // Переменная хранящая версии

    boolean versionExists = false; // Переменная наличия версий

    File internalStorageDir;

    FileReader reader; // Создаём поток вывода для чтения из файла

    File versions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Разворачиваем страницу на весь экран
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launch_screen);

        internalStorageDir = getFilesDir(); // Получаем путь для хранения локальных данных

        versions = new File(internalStorageDir, "versions.csv"); // Создаём файл для хранения локальных данных

            try { // Настраиваем поток вывода для чтения версий
                reader = new FileReader(internalStorageDir + "/versions.csv");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        versionExists = ifLocalVersionsExists();

        ImageView img = findViewById(R.id.loading); // Привязываемся к изображению загрузки на экране
        // создаем анимацию
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progresbar_animation);
        // запуск анимации для изображения загрузки
        img.startAnimation(animation);

        // Создаём поток для проверки сети интернет
        Thread thread = new Thread() { // Тело потока
            public void run() { // Выполнение задач потока
                Thread.currentThread().setName("Check connection"); // Название потока
                int check = 0; // Проверка 0 отсутствия интернета
                while (check != 3) { // Пока проверка не будет равна 3 т е пока не удасться подключится к серверу
                    while (check == 0) { // Проверка отсутствия подключения к интернету
                        if (!isOnline(LaunchScreenActivity.this)) { // Если нет подключения к сети
                            img.clearAnimation(); // Отключение анимации
                            check = 1; // Переход к проверке наличия интернета
                            makeMessageOnScreen("Ошибка подключения к интернету");
                            if (versionExists) { //  Если существуют локальные данные
                                makeMessageOnScreen("Загрузка локальных данных");
                                // Переход на приветственный экран
                                startActivity(new Intent(LaunchScreenActivity.this, OnBoardingScreenActivity.class));
                            }
                        } else {
                            // Если интернет есть, то попытаться подключится к серверку
                            try { // Попытка подключения к серверку
                                if (tryGetVersion()) { // Если удалось получить ответ
                                    check = 3; // Выход из цикла

                                    versionExists = ifLocalVersionsExists();

                                    // Переход на приветственный экран
                                    startActivity(new Intent(LaunchScreenActivity.this, OnBoardingScreenActivity.class));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    while (check == 1) { // Проверка подключения к интернету
                        if (isOnline(LaunchScreenActivity.this)) {  // Если есть подключение к сети
                            img.startAnimation(animation); // Включение анимации
                            check = 0; // Переход к проверке отсутствия интернета
                        }
                    }
                }
            }

            // Функция проверки подключения к сети
            public boolean isOnline(Context context) {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // Создаём менеджер подключения записываем в него службы подключения
                NetworkInfo netInfo = cm.getActiveNetworkInfo(); // Подключаем и записываем информацию о сети
                if (netInfo != null && netInfo.isConnectedOrConnecting()) { // Если данные получены и есть подключение к сети
                    Log.println(Log.INFO, "INFO", "Connection"); // Записываем в логи состояние сети
                    return true; // Подключение к сети удалось
                } // Иначе
                Log.println(Log.INFO, "INFO", "Have not cnonnection"); // Записываем в логи состояние сети
                return false; // Подключение к сети не удалось
            }

            // Функция подключения к серверу
            public boolean tryGetVersion() throws JSONException {

                String url = "https://food.madskill.ru/dishes/version";

                RequestQueue requestQueue = Volley.newRequestQueue(LaunchScreenActivity.this);

                StringRequest request = new StringRequest(Request.Method.GET, url,
                        result -> resIs[0] = result,
                        error -> resIs[0] = error.getMessage()
                ); // Инструкция по выполнению в случае ошибок или успешного получения ответа
                requestQueue.add(request); // Отправка запроса

                if (resIs[0].equals("") && resIs[0].substring(0, 0).equals("j")) { // Когда сервер ответил или произошла ошибка
                    makeMessageOnScreen("Ошибка подключения к серверу"); // Выводим сообщение на экран
                }
                for (int i = 0; i < 10; i++) { // Пока сервер не ответит в течении 10 секунд
                    Log.println(Log.INFO, "INFO", "Waiting request."); // Данная строка распугивает баги
                    if (!resIs[0].equals("") && !resIs[0].substring(0, 1).equals("j")) { // Когда сервер ответил без ошибок
                        Log.println(Log.INFO, "INFO", resIs[0]); // Выводим полученный результат в логи
                        // Если локальные версии меню не совпадают с глобальными, то обновляем версии
                        if (!resIs[0].equals(version)) {
                            try { // Запись полученного результата в файл
                                // Создаём поток вывода файла
                                FileOutputStream fos = new FileOutputStream(versions);
                                // Записываем строку в файл
                                fos.write(resIs[0].getBytes());
                                // Закрываем поток вывода файла
                                fos.close();
                            } catch (Exception e) {
                            }
                        }
                        return true; // Подключение успешно
                    } else {
                        try {
                            Thread.sleep(1000); // Задержка в 1 секунда
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                makeMessageOnScreen("Ошибка подключения к серверу"); // Выводим сообщение на экран
                return false; // Если сервер не ответил, то отправляем новый запрос
            }

            // Процедура вывода сообщений на экран в основном потоке
            private void makeMessageOnScreen(String msg) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(LaunchScreenActivity.this, msg, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        };
        thread.start(); // Запуск потока
    }

    // Если есть локально сохранённые версии возвращает булевую истинну, иначе лож
    public boolean ifLocalVersionsExists() {
        if (updateLocalVersion()) { // Если файл существует
            if (!(versions.equals("") || version.isEmpty())) { // Если переменная версий не пустая
                return true; // Возвращаем истинну
            } else { // Иначе
                return false; // Возвращаем лож
            }
        } else { // Если файла не существует возвращаем лож
            return false;
        }
    }

    // Обновление переменной с версиями
    public boolean updateLocalVersion() {
        if (versions.exists()) { // Если файл существует
            try { // Чтение файла
                // читаем построчно
                Scanner reader_text = new Scanner(reader); // Создаём сканер
                version = reader_text.nextLine(); // Записываем первую строку файла в переменную версий
            } catch (Exception e) { // В случае ошибок
                e.printStackTrace(); // Вывод ошибок в консоль
            }
            return true;
        } else {
            return false;
        }
    }
}