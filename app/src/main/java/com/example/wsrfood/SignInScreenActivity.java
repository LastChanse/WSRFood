package com.example.wsrfood;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignInScreenActivity extends AppCompatActivity {

    TextView fogotPassword; // Текст "Забыли пароль?"
    EditText Email; // Поле с почтой
    EditText Password; // Поле с паролем

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_screen);

        // Привязки
        fogotPassword = findViewById(R.id.fogot_password); // Привязываемся к тексту на экране
        Email = findViewById(R.id.email); // Привязываемся к полю на экране
        Password = findViewById(R.id.password); // Привязываемся к полю на экране

        fogotPassword.setOnClickListener(new View.OnClickListener() { // Если нажали на текст "Забыли пароль?"
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInScreenActivity.this, SignUpScreenActivity.class)); // переходим на экран регистрации
            }
        });
    }

    public void login(View view) { // Если нажали на кнопку Вход
        if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
            сreateDialog(this, "Все проя должны быть заполнены.");
        } else {
            if (!checkEmail(Email.getText().toString())) {
                сreateDialog(this, "Почта не соответствует паттерну \"name@domenname.ru\".");
            } else {
                tryLogin();
            }
        }
    }

    public void tryLogin() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String baseUrl = "https://food.madskill.ru/auth/login";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl, json,
                response -> {
                    startActivity(new Intent(this, MainActivity.class));
                },
                error -> сreateDialog(this, "Не удалось авторизироваться:\n" + error.getMessage()));
        requestQueue.add(request);
    }

    private boolean checkEmail(String email) { // Процерка на соответствие паттерну "name@domenname.ru",
        // где имя и домен второго уровня могут состоять только из маленьких букв и цифр,
        // домен верхнего уровня - только из маленьких букв. Длина домена верхнего уровня
        // - не более 3х символов).
        if (email.matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,3})$")) {
            return true;
        } else {
            return false;
        }
    }

    public void сreateDialog(Activity activity, String msg) { // Диалоговое окно с текстом ошибки
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle("Error") // Заголовок
                .setMessage(msg) // Текст ошибки
                .setPositiveButton("OK", new DialogInterface.OnClickListener() { // Кнопка согласия
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); // Отмена диалогового окна
                    }
                });
        builder.create().show();
    }
}