package hcmute.edu.vn.mssv18110332.adapter.purchase;

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
import hcmute.edu.vn.mssv18110332.databinding.ItemPurchaseMethodCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Cart;
import hcmute.edu.vn.mssv18110332.model.Delivery;
import hcmute.edu.vn.mssv18110332.model.Items;
import hcmute.edu.vn.mssv18110332.model.PurchaseMethod;

public class PurchaseMethodAdapter extends RecyclerView.Adapter<PurchaseMethodAdapter.PurchaseMethodViewHolder>{
    LayoutInflater inflater;
    Context context;
    int checked = 0;

    public int getSelectedMethod()
    {
        return purchase_methodList.get(checked).getId();
    }

    public void setPurchaseMethodList(List<PurchaseMethod> purchase_methodList) {
        this.purchase_methodList = purchase_methodList;
        notifyDataSetChanged();
    }

    List<PurchaseMethod> purchase_methodList;

    public PurchaseMethodAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @NonNull
    @NotNull
    @Override
    public PurchaseMethodViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        if (context == null) context = parent.getContext();
        final ItemPurchaseMethodCardBinding binding = ItemPurchaseMethodCardBinding.inflate(inflater);
        PurchaseMethodViewHolder holder = new PurchaseMethodViewHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PurchaseMethodViewHolder holder, int position) {
        PurchaseMethod d = purchase_methodList.get(position);
        Glide.with(context).load(d.getIcon()).into(holder.binding.imgIconPurchaseMethodCard);
        holder.binding.checkPurchaseMethodCard.setChecked(position==checked);
        holder.binding.txtNamePurchaseMethodCard.setText(d.getName());
        holder.binding.txtInfoPurchaseMethodCardPurchase.setText(d.getInfo());
        holder.binding.checkPurchaseMethodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (purchase_methodList != null) return purchase_methodList.size();
        return 0;
    }

    public class PurchaseMethodViewHolder extends RecyclerView.ViewHolder{
        final ItemPurchaseMethodCardBinding binding;
        public PurchaseMethodViewHolder(final ItemPurchaseMethodCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
