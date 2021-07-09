package hcmute.edu.vn.mssv18110332.adapter.shopingcart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.databinding.ItemProductOrdersCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.Orders;
import hcmute.edu.vn.mssv18110332.model.Payitem;

public class OrderedCartAdapter extends RecyclerView.Adapter<OrderedCartAdapter.OrderedCartViewHolder>{

    LayoutInflater inflater;

    public void setPayitems(List<Payitem> payitems) {
        this.payitems = new ArrayList<>();
        for (Payitem pi: payitems)
            if (!pi.getServices().equals("delivery"))
                this.payitems.add(pi);
        notifyDataSetChanged();
    }

    List<Payitem> payitems;
    Context context;

    public OrderedCartAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @NotNull
    @Override
    public OrderedCartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        if (context == null) context = parent.getContext();
        final ItemProductOrdersCardBinding binding = ItemProductOrdersCardBinding.inflate(inflater);
        OrderedCartViewHolder viewHolder = new OrderedCartViewHolder(binding);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (payitems != null) return payitems.size();
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OrderedCartViewHolder holder, int position) {
        Payitem c = payitems.get(position);
        Items i = ItemsDAO.get_by_id(c.getItem());
        Glide.with(context).load(i.getImage()).into(holder.binding.imgItemOrdersCard);
        holder.binding.txtNameItemOrdersCard.setText(i.getName());
        holder.binding.txtQuantityOrdersCard.setText(c.getSl()+"");
        holder.binding.txtDiscountedOrdersCard.setText(AppUtils.getVietNamDongFormat(i.getDiscounted()));
        int total = i.getDiscounted() * c.getSl();
        holder.binding.txtTotalCostOrdersCard.setText(AppUtils.getVietNamDongFormat(total));
        holder.binding.btnReviewOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Go to review page", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class OrderedCartViewHolder extends RecyclerView.ViewHolder{
        private final ItemProductOrdersCardBinding binding;
        public OrderedCartViewHolder(final ItemProductOrdersCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


