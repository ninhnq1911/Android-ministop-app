package hcmute.edu.vn.mssv18110332.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.shopingcart.OrderedCartAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ActivityOrdersDetailBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityRegisterBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityUpdateInfoBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.ConfirmDialog;
import hcmute.edu.vn.mssv18110332.helper.Gmail;
import hcmute.edu.vn.mssv18110332.helper.ProgressDialog;
import hcmute.edu.vn.mssv18110332.helper.Session;
import hcmute.edu.vn.mssv18110332.model.Address;
import hcmute.edu.vn.mssv18110332.model.Delivery;
import hcmute.edu.vn.mssv18110332.model.Orders;
import hcmute.edu.vn.mssv18110332.model.Payitem;
import hcmute.edu.vn.mssv18110332.model.Payment;
import hcmute.edu.vn.mssv18110332.model.PurchaseMethod;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.DAO.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class OrdersDetailActivity extends AppCompatActivity {
    ActivityOrdersDetailBinding binding;
    Orders orders;
    Delivery delivery;
    Useraccount user;
    Address address;
    Payment payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrdersDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        user = AppUtils.getCurrentUser();
        Intent intent = getIntent();
        if (intent.getExtras()!=null)
        {
            int orders_id = (int) intent.getExtras().get("orders");
            orders = OrdersDAO.get_by_id(orders_id);
        }
        if (orders == null) finish();
        payment = PaymentDAO.get_by_id(orders.getPid());
        PurchaseMethod pm = PurchaseMethodDAO.get_by_id(payment.getMethod());
        List<Payitem> payitems = PayitemDAO.get_by_payment(payment.getPid());
        RecyclerView recycler = binding.getRoot().findViewById(R.id.recycler_item_ordered_orders_detail);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);
        recycler.setLayoutManager(linearLayoutManager);
        OrderedCartAdapter adapter = new OrderedCartAdapter(LayoutInflater.from(getContext()));
        adapter.setPayitems(payitems);
        recycler.setAdapter(adapter);
        binding.txtIdOrdersDetail.setText(orders.getId()+"");
        binding.txtCustomerOrdersCard.setText(user.getFullname());
        address = AddressDAO.get_by_id(user.getAddress());
        binding.txtAddressOrdersDetail.setText(AddressDAO.getFull(address));
        delivery = DeliveryDAO.get_by_id(orders.getDid());
        binding.txtDeliveryOrdersDetail.setText(delivery.getName());
        binding.txtStartDateOrdersDetail.setText(orders.getStart_date());
        binding.txtEndDateOrdersDetail.setText(orders.getEnd_date());
        binding.txtNameAddressOrdersDetail.setText(address.getName());
        binding.txtStatusOrdersDetail.setText(AppUtils.convertToStatus(orders.getState()));
        binding.txtUphoneOrdersDetail.setText(user.getPhonenumber());
        binding.txtTotalOrdersDetail.setText(AppUtils.getVietNamDongFormat(payment.getAmount()));
        binding.txtPurchaseMethodOrdersDetail.setText(pm.getName());
        Log.d("ORDERS DETAIL",""+orders.getPid() + "-" + payment.getMethod() + "-" + pm.getId() + "-"+ pm.getName());
        if (orders.getState().equals("Canceled"))
            binding.btnCancelOrdersDetail.setVisibility(View.INVISIBLE);

        binding.btnCancelOrdersDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog confirmDialog = new ConfirmDialog(getContext());
                confirmDialog.setConfirmCLickListener(new ConfirmDialog.onConfirmCLickListener() {
                    @Override
                    public void onClick(boolean result) {
                        orders.setState("Canceled");
                        OrdersDAO.update(orders);
                        binding.txtStatusOrdersDetail.setText(AppUtils.convertToStatus(orders.getState()));
                        binding.btnCancelOrdersDetail.setVisibility(View.INVISIBLE);
                    }
                });
                confirmDialog.showDialog(Gravity.CENTER,
                        "xác nhận hủy đơn hàng",
                        "Bạn có chắc muốn hủy đơn hàng này không? " +
                                "Hãy chắc chắn rằng bạn thật sự muốn hủy nó nhé!");
            }
        });
    }

    Context getContext() {return OrdersDetailActivity.this;}
}