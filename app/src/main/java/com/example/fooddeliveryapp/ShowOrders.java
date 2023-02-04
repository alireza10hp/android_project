package com.example.fooddeliveryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddeliveryapp.adapter.CustomerCartAdapter;
import com.example.fooddeliveryapp.model.CustomerCartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowOrders extends AppCompatActivity {
    RecyclerView rv_showAllFood;
    String restName = "";
    List<CustomerCartModel> mList = new ArrayList<>();
    CustomerCartAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_show_orders);

        rv_showAllFood = findViewById(R.id.rv_showAllOrders);
        rv_showAllFood.setHasFixedSize(true);
        rv_showAllFood.setLayoutManager(new LinearLayoutManager(ShowOrders.this));

        Intent intent = getIntent();
        restName = intent.getStringExtra("restName");

        getAllOrders();
    }

    private void getAllOrders() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = firebaseUser.getUid();
        if (firebaseUser.getUid() != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(RegisterActivity.ORDER);
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    mList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                                CustomerCartModel restaurantFood = dataSnapshot2.getValue(CustomerCartModel.class);
                                if (restaurantFood.getId().equals(userId)) {
                                    mList.add(restaurantFood);
                                }
                            }
                        }
                    }
                    mAdapter = new CustomerCartAdapter(ShowOrders.this, mList);
                    rv_showAllFood.setAdapter(mAdapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

}