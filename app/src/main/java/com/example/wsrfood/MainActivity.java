package com.example.wsrfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInitialData();
        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        MenuItemAdapter adapter = new MenuItemAdapter(this, menuItems);
        recyclerView.setAdapter(adapter);
    }
    private void setInitialData(){

        menuItems.add(new MenuItem ("Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem ("Egg and cucmber...", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem ("Veggie tomato mix", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem ("Egg and cucmber...", 1200, R.drawable.dish_default_icon));
        menuItems.add(new MenuItem ("Veggie tomato mix", 1200, R.drawable.dish_default_icon));
    }
}