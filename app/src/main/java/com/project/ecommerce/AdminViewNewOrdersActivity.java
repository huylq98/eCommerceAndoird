package com.project.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.project.ecommerce.model.AdminOrder;

public class AdminViewNewOrdersActivity extends AppCompatActivity {

    private RecyclerView ordersList;
    private DatabaseReference ordersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_new_orders);

        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        ordersList = findViewById(R.id.orders_list);
        ordersList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrder> options =
                new FirebaseRecyclerOptions.Builder<AdminOrder>()
                .setQuery(ordersRef, AdminOrder.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrder, AdminOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final AdminOrderViewHolder adminOrderViewHolder, final int i, @NonNull final AdminOrder adminOrder) {
                        adminOrderViewHolder.username.setText("Name: " + adminOrder.getFullName());
                        adminOrderViewHolder.userphone.setText("Phone: " + adminOrder.getPhoneNumber());
                        adminOrderViewHolder.useraddress.setText("Address: " + adminOrder.getAddress());
                        adminOrderViewHolder.dateTime.setText("Ordered at: " + adminOrder.getDate() + "   " + adminOrder.getTime());
                        adminOrderViewHolder.totalAmount.setText("Total: " + adminOrder.getTotalAmount());

                        adminOrderViewHolder.showOrdersBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String userID = getRef(i).getKey();
                                Intent intent = new Intent(AdminViewNewOrdersActivity.this, AdminOrderProductsActivity.class);
                                intent.putExtra("uid", userID);
                                startActivity(intent);
                            }
                        });

                        adminOrderViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                CharSequence options[] = new CharSequence[] {
                                        "Yes",
                                        "No"
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewNewOrdersActivity.this);
                                builder.setTitle("Have you shipped this order?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(which == 0) {
                                            String userID = getRef(i).getKey();
                                            removeOrder(userID);
                                        } else {
                                            finish();
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_layout, parent, false);
                        return new AdminOrderViewHolder(view);
                    }
                };
        ordersList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String userID) {
        ordersRef.child(userID).removeValue();
        FirebaseDatabase.getInstance().getReference()
                .child("Carts").child("Admin Invoice").child(userID).removeValue();
    }

    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {
        public TextView username, userphone, totalAmount, dateTime, useraddress;
        public Button showOrdersBtn;

        public AdminOrderViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.order_user_name);
            userphone = itemView.findViewById(R.id.order_phone_number);
            totalAmount = itemView.findViewById(R.id.order_total_price);
            dateTime = itemView.findViewById(R.id.order_date_time);
            useraddress = itemView.findViewById(R.id.order_address);
            showOrdersBtn = itemView.findViewById(R.id.show_all_products_btn);
        }
    }
}
