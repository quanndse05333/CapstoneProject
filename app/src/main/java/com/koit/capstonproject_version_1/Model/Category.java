package com.koit.capstonproject_version_1.Model;

import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ICategory;
import com.koit.capstonproject_version_1.Controller.Interface.ListProductInterface;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Category implements Serializable {
    private String categoryId, categoryName;
    private DataSnapshot dataRoot;
    DatabaseReference nodeRoot;

    public Category(String categoryId, String categoryName) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Category() {
        nodeRoot = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public String toString() {
        return categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public void getListCategory(final ICategory iCategory) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataRoot = dataSnapshot;
                LayDanhSachCategory(dataSnapshot, iCategory);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        if (dataRoot != null) {
            LayDanhSachCategory(dataRoot, iCategory);
        } else {
            nodeRoot.addListenerForSingleValueEvent(valueEventListener);
        }

    }

    private void LayDanhSachCategory(DataSnapshot dataSnapshot, ICategory iCategory) {
        DataSnapshot dataSnapshotCategory = dataSnapshot.child("Categories").child("0399271212");
        //Lấy danh sách san pham
        for (DataSnapshot valueCategory : dataSnapshotCategory.getChildren()) {
            Category category = valueCategory.getValue(Category.class);
            category.setCategoryId(category.getCategoryId());

            iCategory.getCategory(category);
        }
    }
}
