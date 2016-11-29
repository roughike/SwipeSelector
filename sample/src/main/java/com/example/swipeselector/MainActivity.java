package com.example.swipeselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeSelector sizeSelector = (SwipeSelector) findViewById(R.id.sizeSelector);
        final SwipeSelector toppingSelector = (SwipeSelector) findViewById(R.id.toppingSelector);
        final SwipeSelector deliverySelector = (SwipeSelector) findViewById(R.id.deliverySelector);

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // You would probably send these to your server for validation,
                // like: "http://example.com/api?size=" + selectedSize.getValue()
                // etc, but we'll just display a toast.

                String toastMessage = "";

                if (sizeSelector.hasSelection()) {
                    SwipeItem selectedSize = sizeSelector.getSelectedItem();
                    toastMessage += "Size: " + selectedSize.getTitle();
                } else {
                    toastMessage += "No size selected.";
                }

                if (toppingSelector.hasSelection()) {
                    SwipeItem selectedToppings = toppingSelector.getSelectedItem();
                    toastMessage += "\nToppings: " + selectedToppings.getTitle();
                } else {
                    toastMessage += "\nNo toppings selected.";
                }

                if (deliverySelector.hasSelection()) {
                    SwipeItem selectedDelivery = deliverySelector.getSelectedItem();
                    toastMessage += "\nDelivery: " + selectedDelivery.getTitle();
                } else {
                    toastMessage += "\nNo delivery method selected.";
                }

                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
