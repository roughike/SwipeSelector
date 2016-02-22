package com.example.swipeselector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.iroughapps.swipeselector.SwipeItem;
import com.iroughapps.swipeselector.SwipeSelector;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SwipeSelector sizeSelector = (SwipeSelector) findViewById(R.id.sizeSelector);
        sizeSelector.setFragmentManager(getSupportFragmentManager());
        sizeSelector.setItems(
                new SwipeItem(-1, R.string.app_name, R.string.app_name),
                new SwipeItem(0, "Kids' size", "For the small appetite. Can be shared by four toddlers."),
                new SwipeItem(1, "Normal", "Our most popular size. Ideal for kids before their growth spurt."),
                new SwipeItem(2, "Large", "This is two times the normal size. Suits well for the hangover after a bachelor party."),
                new SwipeItem(3, "Huge", "Suitable for families. Also perfect for a bigger appetite if your name happens to be Furious Pete.")
        );
    }
}
