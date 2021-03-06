package com.koit.capstonproject_version_1.dao;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.koit.capstonproject_version_1.Controller.Interface.IProduct;
import com.koit.capstonproject_version_1.Model.Product;
import com.koit.capstonproject_version_1.Model.SuggestedProduct;

public class CreateProductDAO {
    private static CreateProductDAO mInstance;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    public CreateProductDAO() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public static CreateProductDAO getInstance() {
        if (mInstance == null) {
            mInstance = new CreateProductDAO();
        }
        return mInstance;
    }

    public void getSuggestedProduct(final String barcode, final IProduct iProduct) {
        databaseReference.child("SuggestedProducts").child(barcode).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SuggestedProduct product = dataSnapshot.getValue(SuggestedProduct.class);
                iProduct.getSuggestedProduct(product);
                if(product != null){
                    Log.i("suggestedProduct", product.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("TAG", "Failed to read value.", databaseError.toException());
            }
        });
    }

    public void isExistBarcode(final String barcode, final IProduct iProduct) {
        databaseReference.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean exists = false;
                for (DataSnapshot child : snapshot.getChildren()) {
                    Product product = child.getValue(Product.class);
                    if (product != null) {
                        if (product.getBarcode().equals(barcode)) {
                            exists = true;
                            break;
                        }
                    }
                }
                if (exists) {
                    // This product already exists in firebase.
                    iProduct.isExistBarcode(true);

                } else {
                    // This product doesn't exists in firebase.
                    iProduct.isExistBarcode(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("TAG", "Failed to read value.", error.toException());
            }
        });
    }

    public void addProductInFirebase(Product product) {
        product.setUnits(null);
        String userId = UserDAO.getInstance().getUserID();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = databaseReference.child("Products").child(userId).child(product.getProductId());
        databaseReference.setValue(product);
//        databaseReference.keepSynced(true);
    }

    public void addImageProduct(Uri uri, String imgName) {
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        if(uri != null){
            image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Log.i("saveImageProduct", "onSuccess: Upload Image URI is " + uri.toString());
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.i("saveImageProduct", "save failed");
                }
            });
        }

    }

    public void deleteImageProduct( String imgName) {
//        final StorageReference image = storageReference
        final StorageReference image = storageReference.child("ProductPictures/" + imgName);
        image.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}
