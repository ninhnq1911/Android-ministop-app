package hcmute.edu.vn.mssv18110332.adapter.category;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110332.R;
import hcmute.edu.vn.mssv18110332.databinding.ItemCategoryCardBinding;
import hcmute.edu.vn.mssv18110332.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    List<Category> categoryList;
    LayoutInflater inflater;
    Context context;
    CategoryOnClickListener listener;
    List<Integer> selectedCategory;

    public CategoryAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
        selectedCategory = new ArrayList<Integer>();
    }

    public interface CategoryOnClickListener{
        void OnClick(List<Integer> list);
    }

    public void setData(List<Category> list)
    {
        this.categoryList = list;
        notifyDataSetChanged();
    }

    public void setListener(CategoryOnClickListener listener)
    {
        this.listener = listener;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (inflater == null) inflater = LayoutInflater.from(parent.getContext());
        final ItemCategoryCardBinding binding = ItemCategoryCardBinding.inflate(inflater);
        CategoryViewHolder view = new CategoryViewHolder(binding);
        context = parent.getContext();
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        holder.binding.imgItemCategoryCard.setImageResource(R.drawable.category_nuoc_giai_khat);
        holder.binding.txtNameCategoryCard.setText(category.getName());
        Glide.with(context).load(category.getImage()).into(holder.binding.imgItemCategoryCard);
        holder.binding.layoutCategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedCategory.contains(category.getId()))
                {
                    selectedCategory.remove((Integer)category.getId());
                    holder.binding.layoutCategoryCard.setBackgroundResource(R.drawable.input_txt_background_trang_boder_circle);
                }
                else
                {
                    selectedCategory.add(category.getId());
                    holder.binding.layoutCategoryCard.setBackgroundResource(R.drawable.input_txt_background_vang_boder_circle);
                }
                if (listener!=null)
                    listener.OnClick(selectedCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (categoryList != null)
            return categoryList.size();
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
        final ItemCategoryCardBinding binding;
        public CategoryViewHolder(final ItemCategoryCardBinding bin) {
            super(bin.getRoot());
            this.binding = bin;
        }
    }
    
}
