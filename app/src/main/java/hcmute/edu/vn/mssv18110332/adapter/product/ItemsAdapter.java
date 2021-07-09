package hcmute.edu.vn.mssv18110332.adapter.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110332.databinding.ItemProductCardBinding;
import hcmute.edu.vn.mssv18110332.helper.AppUtils;
import hcmute.edu.vn.mssv18110332.model.Items;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> implements Filterable {

    List<Items> itemsList;
    List<Items> itemsListOld;
    LayoutInflater inflater;
    Context context;
    ItemOnClickListener listener;

    public boolean isEmpty()
    {
        return (itemsList == null || itemsList.size() == 0);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();
                List<Items> listItemResult = new ArrayList<>();
                listItemResult.clear();
                if (query.isEmpty())
                    listItemResult = itemsListOld;
                else
                    for (Items i: itemsListOld)
                        if (i.getName().toLowerCase().contains(query.toLowerCase()))
                            listItemResult.add(i);
                FilterResults filterResults = new FilterResults();
                filterResults.values = listItemResult;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                itemsList = (List<Items>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ItemOnClickListener
    {
        void onClick(Items i);
    }
    public ItemsAdapter(LayoutInflater inflater)
    {
        this.inflater = inflater;
    }

    public void setData(List<Items> list)
    {
        this.itemsList = list;
        this.itemsListOld = list;
        notifyDataSetChanged();
    }

    public void setListener(ItemOnClickListener listener)
    {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        context = parent.getContext();
        final ItemProductCardBinding binding;
        binding = ItemProductCardBinding.inflate(inflater);
        return new ItemsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ItemsViewHolder holder, int position) {
        Items item = itemsList.get(position);
        if (item == null) return;
        Glide.with(context).load(item.getImage().toString()).into(holder.binding.imgItemItemCard);
        if (listener!=null)
        {
            holder.binding.imgItemItemCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClick(item);
                }
            });
        }
        holder.binding.txtNameItemCard.setText(item.getName());
        if (item.getDiscounted()!=item.getPrice())
            holder.binding.txtPriceItemCard.setText(AppUtils.getVietNamDongFormat((int)item.getPrice()));
        else
            holder.binding.txtPriceItemCard.setVisibility(View.INVISIBLE);
        holder.binding.txtDiscountedItemCard.setText(AppUtils.getVietNamDongFormat((int)item.getDiscounted()));
        holder.binding.ratingbarItemCard.setRating(3.5f);
        holder.binding.txtRatingItemCard.setText("3.5");
    }

    @Override
    public int getItemCount() {
        if (itemsList != null)
            return itemsList.size();
        return 0;
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductCardBinding binding;
        public ItemsViewHolder(final ItemProductCardBinding bin) {
            super(bin.getRoot());
            this.binding = bin;
        }
    }
}
