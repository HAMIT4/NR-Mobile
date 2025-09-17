package com.hamit.nrmobile.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.hamit.nrmobile.CategoryFragments.GeneralFragment;

import java.util.List;

public class CategoryPageAdapter extends FragmentStateAdapter {
    private final List<String> categories;

    public CategoryPageAdapter(@NonNull FragmentActivity fragmentActivity, List<String> categories) {
        super(fragmentActivity);
        this.categories = categories;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a fragment for each category
        String category = categories.get(position);
        return GeneralFragment.newInstance(category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}

