package hcmute.edu.vn.mssv18110332.adapter.delivery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import hcmute.edu.vn.mssv18110332.DAO.ItemsDAO;
import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemDeliveryCardBinding;
import hcmute.edu.vn.mssv18110332.databinding.ItemProductPurchaseCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Delivery;
import hcmute.edu.vn.mssv18110332.model.Items;

public class DeliveryAdapter  extends RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>{

    LayoutInflater inflater;
    Context context;
    int checked = 0;

    public void setListener(OnChangeListener listener) {
        this.listener = listener;
    }

    OnChangeListener listener;

    public interface OnChangeListener{
        void OnChange();
    }

    public Delivery getSelected()
    {
        return deliveryList.get(checked);
    }

    public void setDeliveryList(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
        notifyDataSetChanged();
    }

    List<Delivery> deliveryList;

    public DeliveryAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @NotNull
    @Override
    public DeliveryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        if (context == null) context = parent.getContext();
        final ItemDeliveryCardBinding binding = ItemDeliveryCardBinding.inflate(inflater);
        DeliveryViewHolder holder = new DeliveryViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DeliveryViewHolder holder, int position) {
        Delivery d = deliveryList.get(position);
        Glide.with(context).load(d.getIcon()).into(holder.binding.imgIconDeliveryCard);
        holder.binding.checkDeliveryCard.setChecked(position==checked);
        holder.binding.txtNameDeliveryCard.setText(d.getName());
        holder.binding.txtCostDeliveryCard.setText(AppUtils.getVietNamDongFormat(d.getCost()));
        holder.binding.txtTimeDeliveryCard.setText("Giao hàng dự kiến trong " + d.getTime() + "h");
        holder.binding.checkDeliveryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = position;
                notifyDataSetChanged();
                if (listener!=null)
                    listener.OnChange();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (deliveryList != null) return deliveryList.size();
        return 0;
    }

    public class DeliveryViewHolder extends RecyclerView.ViewHolder{
    final ItemDeliveryCardBinding binding;
        public DeliveryViewHolder(final ItemDeliveryCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
