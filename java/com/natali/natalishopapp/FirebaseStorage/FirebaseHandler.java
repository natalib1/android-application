//Manage the actions for the firebase

package com.natali.natalishopapp.FirebaseStorage;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.natali.natalishopapp.Objects.Product;
import com.natali.natalishopapp.Objects.User;
import com.natali.natalishopapp.SharedPreference.SharedPref;
import com.natali.natalishopapp.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FirebaseHandler {

    public static int counter = 0;
    public static int userType = 0;
    public static boolean flag = true;

    public interface SuccessCallBack {
        void SuccessCallBack(Boolean success);
    }

    public interface UserCallBack {
        void UserCallBack(User user);
    }

    public interface ProductCallBack {
        void ProductCallBack(Product prod);
    }

    public interface ImageUrlCallBack {
        void ImageUrlCallBack(String strImage);
    }

    public interface ProdsArrCallBack {
        void ProdsArrCallBack(ArrayList<Product> arrList);
    }

    public interface StringCallBack {
        void StringCallBack(String str);
    }

    //add user that sign-in
    public static void addUserToDataBase(final Context context, User user, final SuccessCallBack callBack) {
        user.setEmail(Utils.fixEmailString(user.getEmail()));

        Map<String, Object> dict = user.returnAsDictionary();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getEmail()).setValue(dict, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Add User To DataBase failed" + databaseError.getMessage());
                    callBack.SuccessCallBack(false);
                } else {
                    System.out.println("Add User To DataBase successfully");
                    callBack.SuccessCallBack(true);
                }
            }
        });
    }

    //get user by id (email)
    public static void getUser(String email, String password, final UserCallBack callBack) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        email = Utils.fixEmailString(email);

        mDatabase.child("Users").child(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    callBack.UserCallBack(user);
                } else {
                    callBack.UserCallBack(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.UserCallBack(null);
            }
        });
    }

    //seller add product to offered products for sell
    public static void addItemToDataBase(final Context context, final Product prod, final SuccessCallBack callBack) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String itemKey = mDatabase.child("Items").push().getKey();
        prod.setCode(itemKey);
        final Map<String, Object> dict = prod.returnAsDictionary();

        mDatabase.child("Items").child(itemKey).setValue(dict, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Add Item To DataBase failed" + databaseError.getMessage());
                    callBack.SuccessCallBack(false);
                } else {
                    System.out.println("Add Item To DataBase successfully");
                    callBack.SuccessCallBack(true);
                }
            }
        });
    }

    //seller edit product from his offered products for sell
    public static void editItemToDataBase(final Context context, final Product prod, final SuccessCallBack callBack) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        String code = prod.getCode();
        final Map<String, Object> dict = prod.returnAsDictionary();

        mDatabase.child("Items").child(code).setValue(dict, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Edit Item To DataBase failed" + databaseError.getMessage());
                    callBack.SuccessCallBack(false);
                } else {
                    System.out.println("Edit Item To DataBase successfully");
                    callBack.SuccessCallBack(true);
                }
            }
        });
    }

    //get product by code (fire base id givven at addItemToDataBase)
    public static void getItemByCode(String code, final ProductCallBack callBack) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Items").child(code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Product prod = snapshot.getValue(Product.class);
                    callBack.ProductCallBack(prod);
                } else {
                    callBack.ProductCallBack(null);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBack.ProductCallBack(null);
            }
        });
    }

    //get products array by category
    public static void getItemByCategory(String category , final ProdsArrCallBack callback) {
        final ArrayList<Product> arrProdList = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Items").orderByChild("category").equalTo(category);
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Product prod = dsp.getValue(Product.class);
                            arrProdList.add(prod);
                        }
                        callback.ProdsArrCallBack(arrProdList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.ProdsArrCallBack(arrProdList);
                    }
                });
    }

    //get products array by seller id (email)
    public static void getItemBySeller(String email , final ProdsArrCallBack callback) {
        final ArrayList<Product> arrProdList = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        Query query = mDatabase.child("Items").orderByChild("sellerId").equalTo(email);
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Product prod = dsp.getValue(Product.class);
                            arrProdList.add(prod);
                        }
                        callback.ProdsArrCallBack(arrProdList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.ProdsArrCallBack(arrProdList);
                    }
                });
    }

    //get products array of all products offer in the app in past and present
    public static void getAllItems(final ProdsArrCallBack callback) {
        final ArrayList<Product> arrProdList = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Items").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Product prod = dsp.getValue(Product.class);
                            arrProdList.add(prod);
                        }
                        callback.ProdsArrCallBack(arrProdList);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                        callback.ProdsArrCallBack(arrProdList);
                    }
                });
    }

    //add product to user's shopping cart
    public static void addItemToShoppingCart(final Context context, final Product prod, final SuccessCallBack callBack) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> dict = new HashMap<>();
        dict.put("productId", prod.getCode());

        String itemKey = mDatabase.child("Carts").push().getKey();
        dict.put("reservationId", itemKey);

        mDatabase.child("Carts").child(SharedPref.shared().getUserId(context)).child(itemKey).setValue(dict, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Add item to cart to  DataBase failed" + databaseError.getMessage());
                    callBack.SuccessCallBack(false);
                } else {
                    System.out.println("Add item to cart to DataBase successfully");
                    callBack.SuccessCallBack(true);
                }
            }
        });
    }

    //add product to user's purchases history
    public static void addUserPurchasesToDataBase(final Context context, String mClass, ArrayList<Product> products, final SuccessCallBack callBack) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        for (int i=0; i<products.size(); i++) {
            Product prod = products.get(i);

            String curDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            String itemKey = mDatabase.child("Purchases").push().getKey();
            prod.setReservationId(itemKey);
            prod.setReservationDate(curDate);

            final Map<String, Object> dict = prod.returnAsDictionary();

            mDatabase.child("Purchases").child(SharedPref.shared().getUserId(context)).child(itemKey).setValue(dict, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Add User purchase to DataBase failed" + databaseError.getMessage());
                        flag = false;
                    } else {
                        System.out.println("Add User purchase to DataBase successfully");
                    }
                }
            });
        }
        if (flag){
            System.out.println("Add User purchases to DataBase successfully");
            callBack.SuccessCallBack(true);
            if (mClass.equals("ShoppingcartActivity")){
                FirebaseHandler.removeAllItemsFromUserShoppingCart(SharedPref.shared().getUserId(context));
            }
        }else{
            System.out.println("Add User purchases to  DataBase failed");
            callBack.SuccessCallBack(false);
        }
    }

    //add product to seller's selling history
    public static void addSellerSellingToDataBase(final Context context, ArrayList<Product> products) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        for (int i=0; i<products.size(); i++) {
            Product prod = products.get(i);

            String curDate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            String itemKey = mDatabase.child("Sales").push().getKey();
            prod.setReservationId(itemKey);
            prod.setReservationDate(curDate);
            prod.setCostumerId(SharedPref.shared().getUserId(context));

            final Map<String, Object> dict = prod.returnAsDictionary();

            mDatabase.child("Sales").child(prod.getSellerId()).child(itemKey).setValue(dict, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        System.out.println("Add Seller sale to DataBase failed" + databaseError.getMessage());
                    } else {
                        System.out.println("Add Seller sale to DataBase successfully");
                    }
                }
            });
        }
    }

    //get user's purchases list
    public static void getUserPurchase(Context context, final ProdsArrCallBack callback) {
        final ArrayList<Product> arrProdList = new ArrayList<>();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Purchases").child(SharedPref.shared().getUserId(context)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Product prod = dsp.getValue(Product.class);
                            arrProdList.add(prod);
                            counter = counter + 1  ;
                            if (counter == dataSnapshot.getChildrenCount())
                            {
                                callback.ProdsArrCallBack(arrProdList);
                                counter = 0;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //get user's selling list
    public static void getUserSelling(Context context, final ProdsArrCallBack callback) {
        final ArrayList<Product> arrProdList = new ArrayList<>();

        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Sales").child(SharedPref.shared().getUserId(context)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            Product prod = dsp.getValue(Product.class);
                            arrProdList.add(prod);
                            counter = counter + 1;
                            if (counter == dataSnapshot.getChildrenCount()) {
                                callback.ProdsArrCallBack(arrProdList);
                                counter = 0;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //get user's shopping cart list
    public static void getUserCart(final Context context, final ProdsArrCallBack callback) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        final ArrayList<Product> arrProdList = new ArrayList<>();

        mDatabase.child("Carts").child(SharedPref.shared().getUserId(context)).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(final DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            final String str = dsp.child("productId").getValue(String.class);
                            getItemByCode(str, new ProductCallBack() {
                                @Override
                                public void ProductCallBack(Product prod) {
                                    if (prod == null) {
                                        FirebaseHandler.removeItemFromUserShoppingCart(SharedPref.shared().getUserId(context), str, new SuccessCallBack() {
                                            @Override
                                            public void SuccessCallBack(Boolean success) {
                                            }
                                        });
                                    }else{
                                        arrProdList.add(prod);
                                    }
                                    counter = counter + 1  ;
                                    if (counter == dataSnapshot.getChildrenCount())
                                    {
                                        callback.ProdsArrCallBack(arrProdList);
                                        counter = 0;
                                    }
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    //remove product from suggest products and from seller products management
    //the product don't remove from DB in order so show data in purchasesHistory and sellingHistory pages
    public static void removeItemFromSeller(final String code, final SuccessCallBack callback) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Items").child(code).removeValue();

        callback.SuccessCallBack(true);
    }

    //remove product from user's shopping cart
    public static void removeItemFromUserShoppingCart(String email, final String code, final SuccessCallBack callback) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Query query = mDatabase.child("Carts").child(email).orderByChild("productId").equalTo(code);
        query.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            String str = dsp.child("productId").getValue(String.class);
                            if (str.equals(code)){
                                String str1 = dsp.child("reservationId").getValue(String.class);
                                dataSnapshot.child(str1).getRef().removeValue();
                                callback.SuccessCallBack(true);
                                return;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.SuccessCallBack(false);
                    }
                });
    }

    //remove all products from user's shopping cart
    public static void removeAllItemsFromUserShoppingCart(String email) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("Carts").child(email).removeValue();
    }

    //storage- add image of product to storage
    public static void uploadItemImageByteArray(final Context context,String productId, byte[] imageData, final ImageUrlCallBack callBack){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("ProductsImages").child(productId);

        UploadTask uploadTask = storageRef.putBytes(imageData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Utils.showAlertOk(context,"Error upload","Could not upload product image");
                callBack.ImageUrlCallBack(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                //Utils.showAlertOk(context,"Success upload","Product image uploaded");
                callBack.ImageUrlCallBack(downloadUrl.toString());
            }
        });
    }

}