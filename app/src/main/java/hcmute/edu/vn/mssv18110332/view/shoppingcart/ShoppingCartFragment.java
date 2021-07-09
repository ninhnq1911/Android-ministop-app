package hcmute.edu.vn.mssv18110332.view.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hcmute.edu.vn.mssv18110332.DAO.AddressDAO;
import hcmute.edu.vn.mssv18110332.DAO.CartDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.activity.PurchaseActivity;
import hcmute.edu.vn.mssv18110332.adapter.address.FullAddressAdapter;
import hcmute.edu.vn.mssv18110332.adapter.shopingcart.CartAdapter;
import hcmute.edu.vn.mssv18110332.databinding.FragmentCartBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.helper.ConfirmDialog;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Useraccount;

public class ShoppingCartFragment extends Fragment {
    RecyclerView cartRecyclerView;
    CartAdapter cartAdapter;
    List<Cart> cartList;
    private FragmentCartBinding binding;
    Spinner listViewAddress;
    FullAddressAdapter addressAdapter;
    Useraccount user;
    OnBackPressedCallback callback;
    long pressBackTime;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callback =
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        if (System.currentTimeMillis() - pressBackTime < 2000)
                            getActivity().finish();
                        else
                            Toast.makeText(getContext(), "Nhấn back một lần nữa để thoát!", Toast.LENGTH_SHORT).show();
                        pressBackTime = System.currentTimeMillis();
                    }
                };
        getActivity().getOnBackPressedDispatcher().addCallback(this,callback);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Toolbar toolbar = ;
        //((AppCompatActivity)getActivity()).setSupportActionBar();

        user = AppUtils.getCurrentUser();
        cartRecyclerView = binding.getRoot().findViewById(R.id.recycle_item_cart);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        cartRecyclerView.setLayoutManager(linearLayoutManager);
        cartList = CartDAO.get_by_user(user.getId());
        cartAdapter = new CartAdapter(getLayoutInflater());
        cartAdapter.setCartList(cartList);
        binding.txtTotalCart.setText(AppUtils.getVietNamDongFormat(cartAdapter.getTotal_cost()));
        binding.checkAllCart.setChecked(cartAdapter.getTotal_checked() == cartList.size());
        cartAdapter.setListener(new CartAdapter.TotalCostChangeListener() {
            @Override
            public void OnChange(int total) {
                binding.txtTotalCart.setText(AppUtils.getVietNamDongFormat(total));
                cartAdapter.notifyDataSetChanged();
            }

            @Override
            public void OnChecked(boolean checked) {
                binding.checkAllCart.setChecked(checked);
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);
        listViewAddress = binding.getRoot().findViewById(R.id.list_address_cart);
        addressAdapter = new FullAddressAdapter(getContext(),listViewAddress.getId(), AddressDAO.get_by_user(AppUtils.getCurrentUserID()));
        listViewAddress.setAdapter(addressAdapter);

        binding.checkAllCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*CartDAO.setAllChecked();
                binding.txtTotalCart.setText(AppUtils.getVietNamDongFormat(CartDAO.getTotal_Cost()));
                cartAdapter.setCartList(CartDAO.get_by_user(AppUtils.getCurrentUserID()));*/
                if (binding.checkAllCart.isChecked())
                    cartAdapter.setALlChecked();
                else
                    cartAdapter.setALlUnChecked();
            }
        });

        binding.btnPayCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartAdapter.getTotal_checked()==0)
                {
                    Toast.makeText(getContext(), "Chọn sản phẩm bạn muốn thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent i = new Intent(getActivity(), PurchaseActivity.class);
                i.putExtra("delivery",binding.listAddressCart.getSelectedItemPosition());
                someActivityResultLauncher.launch(i);
            }
        });

        binding.btnDeleteFromCartCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartAdapter.getTotal_checked()==0) return;
                ConfirmDialog dialog = new ConfirmDialog(getContext());

                dialog.setConfirmCLickListener(new ConfirmDialog.onConfirmCLickListener() {
                    @Override
                    public void onClick(boolean result) {
                        cartAdapter.delete_from_cart();
                        if (cartAdapter.isEmpty())
                        {
                            cartRecyclerView.setVisibility(View.GONE);
                            binding.emptyLayoutViewCart.setVisibility(View.VISIBLE);
                        }
                    }
                });

                dialog.showDialog(Gravity.CENTER,"xác nhận",
                        "Bạn có thật sự muốn xóa: %sl% sản phẩm ra khỏi giỏ hàng hay không?"
                                .replace("%sl%",cartAdapter.getTotal_checked()+""));
            }
        });

        if (cartAdapter.isEmpty())
        {
            cartRecyclerView.setVisibility(View.GONE);
            binding.emptyLayoutViewCart.setVisibility(View.VISIBLE);
        }

        return root;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        String r = (String) data.getExtras().get("result");
                        if (r.equals("OK")) {
                            cartAdapter.setCartList(Objects.requireNonNull(CartDAO.get_by_user(user.getId())));
                            binding.txtTotalCart.setText(AppUtils.getVietNamDongFormat(cartAdapter.getTotal_cost()));
                            binding.checkAllCart.setChecked(cartAdapter.getTotal_checked() == cartList.size());
                            if (cartAdapter.isEmpty())
                            {
                                cartRecyclerView.setVisibility(View.GONE);
                                binding.emptyLayoutViewCart.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED)
                    {
                        AppUtils.showMessage(getContext(), "Action canceled");
                    }
                    else
                        AppUtils.showMessage(getContext(), "Action Failed");
                }
            });
}