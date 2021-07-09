package hcmute.edu.vn.mssv18110332.adapter.orders;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.PayitemDAO;
import hcmute.edu.vn.mssv18110332.DAO.PaymentDAO;
import hcmute.edu.vn.mssv18110332.adapter.shopingcart.OrderedCartAdapter;
import hcmute.edu.vn.mssv18110332.databinding.ItemOdersCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Orders;
import hcmute.edu.vn.mssv18110332.model.Payitem;
import hcmute.edu.vn.mssv18110332.model.Payment;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OdersViewHolder>{

    public void setmList(List<Orders> mList) {
        this.ordersList = mList;
        notifyDataSetChanged();
    }

    public boolean isEmpty(){return (ordersList == null || ordersList.isEmpty());}

    public void setListener(ViewOrdersDetailOnClickListener listener) {
        this.listener = listener;
    }

    ViewOrdersDetailOnClickListener listener;

    public interface ViewOrdersDetailOnClickListener{
        void OnClick(Orders orders);
    }

    List<Orders> ordersList;

    public OrdersAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    LayoutInflater inflater;
    Context mContext;

    @NonNull
    @NotNull
    @Override
    public OdersViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        if (mContext == null) mContext = parent.getContext();
        final ItemOdersCardBinding binding = ItemOdersCardBinding.inflate(inflater);
        OdersViewHolder viewHolder = new OdersViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OdersViewHolder holder, int position) {
        Orders orders = ordersList.get(position);
        Payment payment = PaymentDAO.get_by_id(orders.getPid());
        List<Payitem> payitems = PayitemDAO.get_by_payment(payment.getPid());
        RecyclerView recycler = holder.binding.getRoot().findViewById(holder.binding.recyclerItemOrderedOrdersCard.getId());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,true);
        recycler.setLayoutManager(linearLayoutManager);
        OrderedCartAdapter adapter = new OrderedCartAdapter(LayoutInflater.from(mContext));
        adapter.setPayitems(payitems);
        recycler.setAdapter(adapter);
        holder.binding.txtIdOrdersCard.setText(orders.getId()+"");
        holder.binding.txtStartDateOrdersCard.setText(orders.getStart_date());
        holder.binding.txtEndDateOrdersCard.setText(orders.getEnd_date());
        holder.binding.txtStatusOrdersCard.setText(AppUtils.convertToStatus(orders.getState()));
        holder.binding.btnViewDetailOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null)
                    listener.OnClick(orders);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ordersList!=null) return ordersList.size();
        return 0;
    }

    public class OdersViewHolder extends RecyclerView.ViewHolder{
        final ItemOdersCardBinding binding;
        public OdersViewHolder(final ItemOdersCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
