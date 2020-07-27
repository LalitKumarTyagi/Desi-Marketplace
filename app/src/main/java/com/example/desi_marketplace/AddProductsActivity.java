package com.example.seller2buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class AddProductsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);

        setContentView(R.layout.activity_add_products);
        getSupportActionBar().hide();

    }
}