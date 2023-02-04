package com.example.fooddeliveryapp;

import static com.example.fooddeliveryapp.ShowRestaurantFood.RESTAURANT_USERS;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapp.adapter.ShowRestaurantAdapter;
import com.example.fooddeliveryapp.model.LocationModel;
import com.example.fooddeliveryapp.model.RestaurantModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private long pressedTime;

    private RecyclerView rv_showAllRestaurant;
    private ShowRestaurantAdapter mAdapter;
    private List<RestaurantModel> mList = new ArrayList<>();

    private ImageView iv_cart;
    private ImageView iv_showOrders;

    private String userName = "";
    private String longitude = "";
    private String latitude = "";

    @Override
    public void onBackPressed() {
        if (pressedTime + 2000 > System.currentTimeMillis()) {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
        } else {
            Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        rv_showAllRestaurant = findViewById(R.id.rv_showAllRestaurant);
        rv_showAllRestaurant.setLayoutManager(new LinearLayoutManager(DashboardActivity.this));
        rv_showAllRestaurant.setHasFixedSize(true);

        iv_cart = findViewById(R.id.iv_cart);
        iv_showOrders = findViewById(R.id.iv_showOrders);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        iv_showOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ShowOrders.class);
                startActivity(intent);
            }
        });

        iv_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CustomerCart.class));
            }
        });


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RegisterActivity.LOCATION_CUSTOMERS).child(userName);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    LocationModel model = snapshot.getValue(LocationModel.class);
                    longitude = model.getLongitude();
                    latitude = model.getLatitude();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        getAllRestaurant();
    }

    private void getAllRestaurant() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RESTAURANT_USERS);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        RestaurantModel restaurantModel = dataSnapshot.getValue(RestaurantModel.class);
                        mList.add(restaurantModel);
                    }
                    mAdapter = new ShowRestaurantAdapter(DashboardActivity.this, mList, userName);
                    rv_showAllRestaurant.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}