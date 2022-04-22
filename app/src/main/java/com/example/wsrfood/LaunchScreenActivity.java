package com.example.wsrfood;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LaunchScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);

        ImageView img = findViewById(R.id.loading);
        // создаем анимацию
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.progresbar_animation);
        // запуск анимации
        img.startAnimation(animation);

        // Создаём поток для проверки сети интернет
        Thread thread = new Thread() { // Тело потока
            public void run() { // Выполнение задач потока
                Thread.currentThread().setName("Check connection"); // Название потока
                int check = 0; // Проверка 0 отсутствия интернета
                while (check != 3) { // Пока проверка не будет равна 3 т е пока не удасться подключится к серверу
                    try { // Задержка в 2 секунды
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (check == 0) { // Проверка отсутствия подключения к интернету
                        if (!isOnline(LaunchScreenActivity.this)) {
                            img.clearAnimation(); // Отключение анимации
                            check = 1; // Переход к проверке наличия интернета
                        }
                        // Если интернет есть, то попытаться подключится к серверку
                        try { // Попытка подключения к серверку
                            if (tryGetVersion()) {
                                check = 3; // Если удалось подключится
                                Intent intent = new Intent(LaunchScreenActivity.this, OnBoardingScreenActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    while (check == 1) { // Проверка подключения к интернету
                        if (isOnline(LaunchScreenActivity.this)) {
                            img.startAnimation(animation); // Включение анимации
                            check = 0; // Переход к проверке отсутствия интернета
                        }
                    }
                }
            }
            // Функция проверки подключения к интернету
            public boolean isOnline(Context context) {
                ConnectivityManager cm =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    return true;
                }
                return false;
            }

            // ОПАМНЫЙ МАНЁВР!!!
            // Создаём вспомогательный класс который будет выступать ссылкой на полученный результат
            // Ссылку возможно использовать несколько раз, поэтому если 1 ответ от сервера будет удачный,
            // а другой нет, то записан будет тот, который удачно попал под проверку получения ответа
            // подобная лотерея не только тяжело фиксится при ошибках или изменениях, так ещё и делает
            // программу не стабильной!!! Но иного способа не найдено на данный момент
            class ResultAddress {
                // Переменные для усовий
                boolean returnedResult = false; // Получен результат запроса к серверу? (по умолчанию ответ не получен или были ошибки)
                boolean returnedRequest = false; // Сервер ответил?
            }

            // Процедура подключения к серверу
            public boolean tryGetVersion() throws JSONException {

                ResultAddress retRes = new ResultAddress(); // Создан объект класса адреса результатов куда будут сохранены результаты

                String url = "https://food.madskill.ru/dishes/version";

                RequestQueue requestQueue = Volley.newRequestQueue(LaunchScreenActivity.this);

                StringRequest request = new StringRequest(Request.Method.GET, url,
                        result -> Result(result.toString(), retRes),
                        error -> Error(error.getMessage(), retRes)
                ); // Инструкция по выполнению в случае ошибок или успешного получения ответа
                requestQueue.add(request); // Отправка запроса

                while (true) { // Пока сервер не ответит, ждать
                    Log.println(Log.INFO, "INFO", "Try"); // Данная строка распугивает баги
                    if (retRes.returnedRequest) { // Когда сервер ответил
                        Log.println(Log.INFO, "INFO", "Succes"); // Данная строка тоже распугивает баги я не шучу!
                        return retRes.returnedResult;
                    }
                }
            }
            // Процедура перехода к главному окну приложения если удалось подключится к серверу
            public void Result(String result, ResultAddress retRes) {
                Toast.makeText(LaunchScreenActivity.this, "Ответ от сервера получен.", Toast.LENGTH_LONG).show();
                retRes.returnedResult = true; // Сервер вернул результат
                retRes.returnedRequest = true; // Ответ получен
            }

            // Процедура завершения попытки получения запроса если сервер вернул ошибку или не удалось подключение
            public void Error(String errMsg, ResultAddress retRes) {
                retRes.returnedResult = false; // Сервер вернул ошибку
                retRes.returnedRequest = true; // Ответ получен
                Toast.makeText(LaunchScreenActivity.this, "Сервер не отвечает.", Toast.LENGTH_LONG).show();
                Log.println(Log.ERROR, "INFO", errMsg);
            }
        };
        thread.start(); // Запуск потока
    }
}