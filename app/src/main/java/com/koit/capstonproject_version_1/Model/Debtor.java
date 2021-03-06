package com.koit.capstonproject_version_1.Model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.koit.capstonproject_version_1.Controller.Interface.ICustomer;
import com.koit.capstonproject_version_1.dao.UserDAO;

import java.io.Serializable;

public class Debtor implements Serializable {
    private String debtorId, address, dateOfBirth, email, fullName, phoneNumber;
    private boolean gender;
    private DatabaseReference nodeRoot;
    private DataSnapshot dataRoot;


    public Debtor(String debtorId, String address, String dateOfBirth, String email, String fullName, String phoneNumber, boolean gender) {
        this.debtorId = debtorId;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
    }

    public Debtor() {
    }

    public String getDebtorId() {
        return debtorId;
    }

    public void setDebtorId(String debtorId) {
        this.debtorId = debtorId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Debtor{" +
                "debtorId='" + debtorId + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", gender=" + gender +
                ", nodeRoot=" + nodeRoot +
                ", dataRoot=" + dataRoot +
                '}';
    }

    public void getListDebtor(final ICustomer iCustomer) {
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getListDebtor(dataSnapshot, iCustomer);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        nodeRoot = FirebaseDatabase.getInstance().getReference();
        nodeRoot.keepSynced(true);
        nodeRoot.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getListDebtor(DataSnapshot dataSnapshot, ICustomer iCustomer) {
        DataSnapshot dataSnapshotDebtor = dataSnapshot.child("Debtors").child(UserDAO.getInstance().getUserID());
        if (dataSnapshotDebtor != null) {
            for (DataSnapshot valueCustomer : dataSnapshotDebtor.getChildren()) {
                Debtor debtor = valueCustomer.getValue(Debtor.class);
                debtor.setDebtorId(valueCustomer.getKey());
                iCustomer.getCustomer(debtor);
            }
        }
    }

    public void addDebtorToFirebase(Debtor debtor) {
        String userId = UserDAO.getInstance().getUserID();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("Debtors").child(userId).child(debtor.getDebtorId());
        debtor.setDebtorId(null);
        databaseReference.setValue(debtor);
//        databaseReference.keepSynced(true);
    }
}
