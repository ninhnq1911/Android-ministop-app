package hcmute.edu.vn.mssv18110332.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.CartDAO;
import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ActivityHomeBinding;
import hcmute.edu.vn.mssv18110332.databinding.ActivityItemDetailBinding;
import hcmute.edu.vn.mssv18110332.databinding.ItemCategoryCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class ItemDetailActivity extends AppCompatActivity {

    ActivityItemDetailBinding binding;
    Intent intent;
    Items item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        intent = getIntent();
        Gson gson = new Gson();
        item = gson.fromJson((String) intent.getExtras().get("item"),Items.class);
        if (item == null) item = ItemsDAO.get_by_id(20);

        Useraccount user = AppUtils.getCurrentUser();

        Glide.with(this).load(item.getImage()).into(binding.imgItemDetail);
        if (ItemsDAO.isFavorite(AppUtils.getCurrentUser(),item))
        {
            binding.imgFavoriteDetail.setImageResource(R.drawable.outline_favorite_white_24dp);
        }
        binding.imgFavoriteDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ItemsDAO.isFavorite(user, item)){
                    ItemsDAO.unFavorite(user,item);
                    binding.imgFavoriteDetail.setImageResource(R.drawable.outline_favorite_border_white_24dp);
                }
                else {
                    binding.imgFavoriteDetail.setImageResource(R.drawable.outline_favorite_white_24dp);
                    ItemsDAO.setFavorite(user,item);
                }
            }
        });
        binding.txtNameItemDetail.setText(item.getName());
        binding.txtDiscountedItemDetail.setText(AppUtils.getVietNamDongFormat(item.getDiscounted()));
        if (item.getDiscounted()!=item.getPrice())
            binding.txtPriceItemDetail.setText(AppUtils.getVietNamDongFormat(item.getPrice()));
        else
            binding.txtPriceItemDetail.setVisibility(View.INVISIBLE);
        binding.txtDeliveryDetail.setText(item.getDelivery());
        binding.txtPromotionDetail.setText(" " + item.getPromotion());
        binding.txtExpItemDetail.setText("Hạn sử dụng: " + item.getExp());
        binding.txtMfgItemDetail.setText("Ngày sản xuất: " + item.getMfg());
        binding.txtProviderItemDetail.setText("Hãng sản xuất: " + item.getProvider());
        binding.txtRatingItemDetail.setText("(3.5)");

        binding.btnAddToCardDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart cart = new Cart();
                cart.setItem(item.getId());
                if (CartDAO.get_by_user_item(cart.getUser(),cart.getItem())!=null)
                    Toast.makeText(ItemDetailActivity.this, "Sản phẩm đã có trong giỏ hàng", Toast.LENGTH_SHORT).show();
                else {
                    CartDAO.insert(cart);
                    Toast.makeText(ItemDetailActivity.this, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnBuyNowDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Cart> cartList = new ArrayList<>();
                Cart c = new Cart();
                c.setItem(item.getId());
                c.setUser(user.getId());
                c.setCheck(1);
                c.setSl(1);
                c.setId(0);
                //CartDAO.insert(c);
                cartList.add(c);
                String cs = CartDAO.get_list_cart_string(cartList);
                Intent i = new Intent(ItemDetailActivity.this,PurchaseActivity.class);
                i.putExtra("cartList",cs);
                startActivity(i);
            }
        });
    }

    Context getContext(){return ItemDetailActivity.this;}

}