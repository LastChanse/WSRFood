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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpScreenActivity extends AppCompatActivity {

    EditText Email; // Поле с почтой
    EditText Password; // Поле с паролем
    EditText FullName; // Поле с полным именем
    EditText Phone; // Поле с номером телефона

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);

        // Привязки
        Email = findViewById(R.id.new_email); // Привязываемся к полю на экране
        Password = findViewById(R.id.new_password); // Привязываемся к полю на экране
        FullName = findViewById(R.id.full_name); // Привязываемся к полю на экране
        Phone = findViewById(R.id.phone); // Привязываемся к полю на экране
    }

    public void register(View view) {
        if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty() || FullName.getText().toString().isEmpty() || Phone.getText().toString().isEmpty()) {
            сreateDialog(this, "Все проя должны быть заполнены.");
        } else {
            if (!checkEmail(Email.getText().toString())) {
                сreateDialog(this, "Почта не соответствует паттерну \"name@domenname.ru\".");
            } else {
                tryRegister();
            }
        }
    }

    public void cancel(View view) {
        startActivity(new Intent(SignUpScreenActivity.this, SignInScreenActivity.class)); // переходим на экран входа
    }

    public void tryRegister() {
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String fullName = FullName.getText().toString();
        String login = fullName.split(" ")[0];

        String baseUrl = "https://food.madskill.ru/auth/register";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, baseUrl,
                response -> {
                    Intent intent = new Intent(SignUpScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                },
                error -> сreateDialog(this, "Регистрация не удалась:\n" + error.getMessage())
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("login", login);
                return params;
            }
        };

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
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel(); // Отмена диалогового окна
                    }
                });
        builder.create().show();
    }
}