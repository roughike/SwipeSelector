package com.example.swipeselector;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

public class MainActivity extends AppCompatActivity {
    /**
     * Size options
     */
    private static final int SIZE_KIDS = 0;
    private static final int SIZE_NORMAL = 1;
    private static final int SIZE_LARGE = 2;
    private static final int SIZE_HUGE = 3;

    /**
     * Topping options
     */
    private static final int TOPPINGS_EMILY = 0;
    private static final int TOPPINGS_BOB = 1;
    private static final int TOPPINGS_HANS = 2;
    private static final int TOPPINGS_ANDREI = 3;

    /**
     * Delivery options
     */
    private static final int DELIVERY_NONE = 0;
    private static final int DELIVERY_YES = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SwipeSelector sizeSelector = (SwipeSelector) findViewById(R.id.sizeSelector);
        sizeSelector.setItems(
                new SwipeItem(-1, "Select a size", "Start by swiping left."),
                new SwipeItem(SIZE_KIDS, "Kids' size", "For the small appetite. Can be shared by four toddlers."),
                new SwipeItem(SIZE_NORMAL, "Normal", "Our most popular size. Ideal for kids before their growth spurt."),
                new SwipeItem(SIZE_LARGE, "Large", "This is two times the normal size. Suits well for the hangover " +
                        "after a bachelor party."),
                new SwipeItem(SIZE_HUGE, "Huge", "Suitable for families. Also perfect for a bigger appetite if your " +
                        "name happens to be Furious Pete.")
        );

        final SwipeSelector toppingSelector = (SwipeSelector) findViewById(R.id.toppingSelector);
        toppingSelector.setItems(
                new SwipeItem(-1, "Select toppings", "Start by swiping left."),
                new SwipeItem(TOPPINGS_EMILY, "Aunt Emily's", "Strawberries, potatoes and cucumber. Just what Aunt " +
                        "Emily found in her backyard."),
                new SwipeItem(TOPPINGS_BOB, "Uncle Bob's Special", "Ranch dressing, bacon, kebab and double pepperoni. " +
                        "And also some lettuce, because lettuce is healthy."),
                new SwipeItem(TOPPINGS_HANS, "Hans' Meat Monster", "Ham, sauerbraten, salami and bratwurst. Hans likes " +
                        "his meat."),
                new SwipeItem(TOPPINGS_ANDREI, "Andreis' Russian Style", "Whole pickles and sour cream. Prijat" +
                        "novo appetita!")
        );

        final SwipeSelector deliverySelector = (SwipeSelector) findViewById(R.id.deliverySelector);
        deliverySelector.setItems(
                new SwipeItem(-1, "Select delivery method", "Start by swiping left."),
                new SwipeItem(DELIVERY_NONE, "No delivery", "Come to our lovely restaurant and pick up the pizza yourself."),
                new SwipeItem(DELIVERY_YES, "Delivery", "Our minimum-wage delivery boy will bring you the pizza by his own " +
                        "scooter using his own gas money.")
        );

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

                if ((Integer) selectedSize.value != -1) {
                    toastMessage += "Size: " + selectedSize.title;
                } else {
                    toastMessage += "No size selected.";
                }

                if ((Integer) selectedToppings.value != -1) {
                    toastMessage += "\nToppings: " + selectedToppings.title;
                } else {
                    toastMessage += "\nNo toppings selected.";
                }

                if ((Integer) selectedDelivery.value != -1) {
                    toastMessage += "\nDelivery: " + selectedDelivery.title;
                } else {
                    toastMessage += "\nNo delivery method selected.";
                }

                Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_LONG).show();
            }
        });
    }
}
