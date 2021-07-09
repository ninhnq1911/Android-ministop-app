package hcmute.edu.vn.mssv18110332.view.oders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.OrdersDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.activity.OrdersDetailActivity;
import hcmute.edu.vn.mssv18110332.adapter.orders.OrdersAdapter;
import hcmute.edu.vn.mssv18110332.databinding.FragmentOrdersBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Orders;

public class OrdersFragment extends Fragment {

    List<Orders> ordersList;
    OrdersAdapter adapter;
    RecyclerView ordersRecyclerView;
    FragmentOrdersBinding binding;
    OnBackPressedCallback callback;
    long pressBackTime;

    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrdersBinding.inflate(inflater,container,false);
        ordersRecyclerView = binding.getRoot().findViewById(binding.recyclerOrdersOrders.getId());
        ordersList = OrdersDAO.get_by_user(AppUtils.getCurrentUserID());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        ordersRecyclerView.setLayoutManager(linearLayoutManager);
        adapter = new OrdersAdapter(LayoutInflater.from(getContext()));
        adapter.setmList(ordersList);
        adapter.setListener(new OrdersAdapter.ViewOrdersDetailOnClickListener() {
            @Override
            public void OnClick(Orders orders) {
                Intent intent = new Intent(getActivity(), OrdersDetailActivity.class);
                intent.putExtra("orders",orders.getId());
                someActivityResultLauncher.launch(intent);
            }
        });
        ordersRecyclerView.setAdapter(adapter);
        // Inflate the layout for this fragment
        if (adapter.isEmpty())
        {
            ordersRecyclerView.setVisibility(View.GONE);
            binding.emptyLayoutViewOrders.setVisibility(View.VISIBLE);
        }
        return binding.getRoot();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        String r = (String) data.getExtras().get("result");
                        if (r.equals("OK")) {
                            ordersList = OrdersDAO.get_by_user(AppUtils.getCurrentUserID());
                            adapter.setmList(ordersList);
                        }
                    }
                    else if (result.getResultCode() == Activity.RESULT_CANCELED)
                    {
                        ordersList = OrdersDAO.get_by_user(AppUtils.getCurrentUserID());
                        adapter.setmList(ordersList);
                        AppUtils.showMessage(getContext(), "Action canceled");
                    }
                    else
                        AppUtils.showMessage(getContext(), "Action Failed");
                }
            });
}