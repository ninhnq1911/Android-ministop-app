package hcmute.edu.vn.mssv18110332.activity;

import androidx.appcompat.app.AppCompatActivity;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.adapter.address.PurchaseAddressAdapter;
import hcmute.edu.vn.mssv18110332.adapter.delivery.DeliveryAdapter;
import hcmute.edu.vn.mssv18110332.adapter.product.ItemsPurchaseAdapter;
import hcmute.edu.vn.mssv18110332.adapter.purchase.PurchaseMethodAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ActivityPurchaseBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Orders;
import hcmute.edu.vn.mssv18110332.model.Payitem;
import hcmute.edu.vn.mssv18110332.model.Payment;
import hcmute.edu.vn.mssv18110332.model.Useraccount;
import hcmute.edu.vn.mssv18110332.DAO.*;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PurchaseActivity extends AppCompatActivity {

    DeliveryAdapter deliveryAdapter;
    PurchaseMethodAdapter purchaseAdapter;
    RecyclerView deliveryRecycler, purchaseMethodRecycler;
    PurchaseAddressAdapter addressAdapter;
    ItemsPurchaseAdapter itemAdapter;
    ActivityPurchaseBinding binding;
    RecyclerView itemRecyclerView;
    Intent intent;
    List<Cart> cartList;

    int price_total=0, delivery_fee=0, discounted_total=0, extra_discount=0,total=0, number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPurchaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Useraccount ua = AppUtils.getCurrentUser();
        if (ua.getAddress() == 0)
        {
            Toast.makeText(this, "Bạn chưa cập nhật địa chỉ", Toast.LENGTH_SHORT).show();
            this.finish();
        }

        addressAdapter = new PurchaseAddressAdapter(getContext(),binding.spinnerReceiveAddressPurchase.getId(),AddressDAO.get_by_user(AppUtils.getCurrentUserID()));
        binding.spinnerReceiveAddressPurchase.setAdapter(addressAdapter);
        binding.spinnerReceiveAddressPurchase.setSelection(addressAdapter.default_address);

        intent = getIntent();
        String cartListString = "";
        if (intent.getExtras()!=null)
             cartListString = (String) intent.getExtras().get("cartList");

        if (intent.getExtras()!=null)
        {
            if ( intent.getExtras().get("delivery")!=null)
            {
                int selected_address_position = (int) intent.getExtras().get("delivery");
                binding.spinnerReceiveAddressPurchase.setSelection(selected_address_position);
            }
        }

        Type listType = new TypeToken<List<Cart>>() {}.getType();
        Gson gson = new Gson();
        cartList = gson.fromJson(cartListString,listType);
        if (cartList == null  || cartList.isEmpty())
            cartList = CartDAO.get_selected(AppUtils.getCurrentUserID());

        itemRecyclerView = binding.getRoot().findViewById(R.id.recycler_item_purchase);
        itemAdapter = new ItemsPurchaseAdapter(getLayoutInflater());
        itemAdapter.setData(cartList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        itemRecyclerView.setLayoutManager(linearLayoutManager);
        itemRecyclerView.setAdapter(itemAdapter);

        deliveryRecycler = binding.getRoot().findViewById(R.id.spinner_delivery_method_purahse);
        LinearLayoutManager linearLayoutManager_ = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        deliveryAdapter = new DeliveryAdapter(getLayoutInflater());
        deliveryAdapter.setDeliveryList(DeliveryDAO.get_all());
        deliveryRecycler.setLayoutManager(linearLayoutManager_);
        deliveryRecycler.setAdapter(deliveryAdapter);

        purchaseMethodRecycler = binding.getRoot().findViewById(R.id.spinner_payment_mehod_purchase);
        LinearLayoutManager linearLayoutManager_2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        purchaseMethodRecycler.setLayoutManager(linearLayoutManager_2);
        purchaseAdapter = new PurchaseMethodAdapter(getLayoutInflater());
        purchaseAdapter.setPurchaseMethodList(PurchaseMethodDAO.get_all());
        purchaseMethodRecycler.setAdapter(purchaseAdapter);

        deliveryAdapter.setListener(new DeliveryAdapter.OnChangeListener() {
            @Override
            public void OnChange() {
                delivery_fee = deliveryAdapter.getSelected().getCost();
                if (extra_discount!=0)
                    extra_discount = Integer.min(delivery_fee,20000);
                binding.txtDeliveryFeePurchase.setText(AppUtils.getVietNamDongFormat(delivery_fee));
                binding.txtVoucherPurchase.setText("-" + AppUtils.getVietNamDongFormat(extra_discount));
                total = discounted_total + delivery_fee - extra_discount;
                binding.txtTotalPurchase.setText(AppUtils.getVietNamDongFormat(total));
                binding.txtTotalPurchaseOrder.setText(AppUtils.getVietNamDongFormat(total));
            }
        });

        itemAdapter.setAddListener(new ItemsPurchaseAdapter.AddItemClickListener() {
            @Override
            public void OnAdd(List<Cart> carts) {
                cartList = carts;
                updateUI();
            }
        });

        itemAdapter.setRemoveListener(new ItemsPurchaseAdapter.RemoveItemClickListener() {
            @Override
            public void OnRemove(List<Cart> carts) {
                cartList = carts;
                updateUI();
            }
        });

        binding.btnOrderPurcahse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Useraccount u = AppUtils.getCurrentUser();
                String address = AddressDAO.getFull(u.getAddress());
                // create a new payment
                Payment payment = new Payment();
                payment.setAmount(total);
                payment.setUid(u.getId());
                payment.setUname(u.getName());
                payment.setUaddress(address);
                payment.setUphone(u.getPhonenumber());
                payment.setMethod(purchaseAdapter.getSelectedMethod());
                PaymentDAO.insert(payment);
                payment.getPid();;
                // create a new payment item
                for (Cart c: cartList)
                {
                    Payitem item = new Payitem(payment);
                    item.setItem(c.getItem());
                    item.setCost(ItemsDAO.getCost(c.getItem()));
                    item.setSl(c.getSl());
                    PayitemDAO.insert(item);
                    CartDAO.delete(c);
                }
                Payitem de_item = new Payitem(payment);
                de_item.setServices("delivery");
                de_item.setCost(delivery_fee);
                de_item.setItem(deliveryAdapter.getSelected().getId());
                PayitemDAO.insert(de_item);
                // create a new orders
                Orders orders = new Orders();
                orders.setPid(payment.getPid());
                orders.setUid(u.getId());
                orders.setUname(u.getName());
                orders.setUaddress(address);
                orders.setUphone(u.getPhonenumber());
                int days = deliveryAdapter.getSelected().getTime() / 24;
                orders.setEnd_date(AppUtils.getNextDate(days));
                orders.setState("Created");
                orders.setDid(deliveryAdapter.getSelected().getId());
                OrdersDAO.insert(orders);
                Toast.makeText(PurchaseActivity.this, "Đặt hàng thành công bạn nhé!!!!!!!!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.putExtra("result","OK");
                setResult(Activity.RESULT_OK,i);
                sendConfirmEmail(u,orders,payment);
                finish();
            }
        });

        updateUI();
    }

    void sendConfirmEmail(Useraccount user, Orders orders, Payment payment)
    {
        String message = "Chào %name%!<br>".replace("%name%",user.getName());
        message += "Đơn hàng của bạn đã được gửi đến MiniStop vào lúc %time%<br>".replace("%id%",orders.getId()+"").replace("%time%",orders.getStart_date());
        message += "Người nhận: %name%<br>".replace("%name%",orders.getUname());
        message += "địa chỉ nhận hàng: %phone% <br>                 %address%<br>".replace("%phone%",orders.getUphone()).replace("%address%",orders.getUaddress());
        message += "Phương thức thanh toán: %method%<br>".replace("%method%",PurchaseMethodDAO.get_by_id(payment.getMethod()).getName());
        message += "Đơn hàng của bạn được vận chuyển bởi %delivery%.<br>".replace("%delivery%",DeliveryDAO.get_by_id(orders.getDid()).getName());
        message += "Với tổng giá trị đơn hàng là: %total%<br>".replace("%total%", AppUtils.getVietNamDongFormat(payment.getAmount()));
        message += "Bạn vui lòng mở ứng dụng MiniStop và xem phần đơn hàng / chi tiết đơn hàng để có thên thông tin bạn nhé!<br>";
        message += "Ministop sẽ cập nhật đến bạn những thông tin mới nhất về đơn hàng trong thời gian sớn nhất!<br>";
        message += "Cảm ơn và hân hạnh được phục vụ bạn!<br>";
        message += "Thân ái,<br>";
        message += "Trung tâm chăm sóc khách hàng MiniStop!<br>";

        AppUtils.sendEmail(getContext(),message,user.getEmail());
    }

    void updateUI()
    {
        price_total = CartDAO.getTotal_Cost_Price(cartList);
        binding.txtTotalItemCostPurchase.setText(AppUtils.getVietNamDongFormat(price_total));
        delivery_fee = deliveryAdapter.getSelected().getCost();
        binding.txtDeliveryFeePurchase.setText(AppUtils.getVietNamDongFormat(delivery_fee));
        discounted_total = CartDAO.getTotal_Cost(cartList);
        binding.txtTotalDiscountPurchase.setText("-" + AppUtils.getVietNamDongFormat(price_total - discounted_total));
        extra_discount = 0;
        number = 0;
        for (Cart c: cartList) number += c.getSl();
        String s = "(%no% sản phẩm)".replace("%no%", number+"");
        binding.txtNumberOfItemPurchase.setText(s);
        if ( number >= 10 || discounted_total >= 150000)
            extra_discount = Integer.min(delivery_fee,20000);
        if (extra_discount != 0){
            binding.txtVoucherPurchase.setText("-" + AppUtils.getVietNamDongFormat(extra_discount));
            binding.txtVoucherPurchase.setVisibility(View.VISIBLE);
        }
        else
            binding.txtVoucherPurchase.setVisibility(View.GONE);
        total = discounted_total + delivery_fee - extra_discount;
        binding.txtTotalPurchase.setText(AppUtils.getVietNamDongFormat(total));
        binding.txtTotalPurchaseOrder.setText(AppUtils.getVietNamDongFormat(total));

    }

    Context getContext(){return PurchaseActivity.this;}
}