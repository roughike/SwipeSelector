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
                SwipeItem selectedSize = sizeSelector.getSelectedItem();
                SwipeItem selectedToppings = toppingSelector.getSelectedItem();
                SwipeItem selectedDelivery = deliverySelector.getSelectedItem();

                // You would probably send these to your server for validation,
                // like: "http://example.com/api?size=" + selectedSize.value
                // etc, but we'll just display a toast.

                String toastMessage = "";

                if (selectedSize.getId() != R.id.select_a_size) {
                    toastMessage += "Size: " + selectedSize.getTitle();
                } else {
                    toastMessage += "No size selected.";
                }

                if (selectedToppings.getId() != R.id.select_a_topping) {
                    toastMessage += "\nToppings: " + selectedToppings.getTitle();
                } else {
                    toastMessage += "\nNo toppings selected.";
                }

                if (selectedDelivery.getId() != R.id.select_delivery_method) {
                    toastMessage += "\nDelivery: " + selectedDelivery.getTitle();
                } else {
                    toastMessage += "\nNo delivery method selected.";
                }

                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
