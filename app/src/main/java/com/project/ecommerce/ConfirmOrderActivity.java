package com.project.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ecommerce.prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmOrderActivity extends AppCompatActivity {

    private EditText etName, etPhone, etAddress;
    private Button confirmOrderBtn;
    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        setupComponent();

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInfo();
            }
        });
    }

    private void setupComponent() {
        totalAmount = getIntent().getStringExtra("Total Price");

        etName = findViewById(R.id.shipment_name);
        etPhone = findViewById(R.id.shipment_phone_number);
        etAddress = findViewById(R.id.shipment_address);
        confirmOrderBtn = findViewById(R.id.confirm_order_btn);

        etName.setText(Prevalent.currentOnlineUser.getName());
        etPhone.setText(Prevalent.currentOnlineUser.getPhone());
        etAddress.setText(Prevalent.currentOnlineUser.getAddress());
    }

    private void checkInfo() {
        if(TextUtils.isEmpty(etName.getText().toString().trim())){
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(etPhone.getText().toString().trim())){
            Toast.makeText(this, "Please enter your phone number.", Toast.LENGTH_SHORT).show();
        } else if(TextUtils.isEmpty(etAddress.getText().toString().trim())){
            Toast.makeText(this, "Please enter your address.", Toast.LENGTH_SHORT).show();
        } else {
            confirmOrder();
        }
    }

    private void confirmOrder() {
        String saveCurrentDate, saveCurrentTime;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime= currentTime.format(calForDate.getTime());

        final DatabaseReference cartsRef = FirebaseDatabase.getInstance().getReference().child("Carts").child("Users");
        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> ordersMap = new HashMap<>();
        ordersMap.put("totalAmount", totalAmount);
        ordersMap.put("fullName", etName.getText().toString().trim());
        ordersMap.put("phoneNumber", etPhone.getText().toString().trim());
        ordersMap.put("address", etAddress.getText().toString().trim());
        ordersMap.put("date", saveCurrentDate);
        ordersMap.put("time", saveCurrentTime);
        ordersMap.put("state", "Ordered");

        ordersRef.updateChildren(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    cartsRef.child(Prevalent.currentOnlineUser.getPhone()).removeValue();
                    Toast.makeText(ConfirmOrderActivity.this, "Order successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ConfirmOrderActivity.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
