 package com.example.desi_marketplace;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class RegisterRetailActivity extends AppCompatActivity {


        String productType="",productName,enterpriseName,phone,description,social;
    boolean delivery,payment;

        EditText editText_phone,editText_enterprise,editText_product,editText_description,editText_social;
        Button button_next;
        ListView listView;
        ScrollView scrollView;
        ConstraintLayout constraintLayout;
        TextView textView_productType;
        CheckBox checkBox_delivery,checkBox_payment;

        SharedPreferences preferences;
        SharedPreferences.Editor editor;

        FirebaseFirestore firestore;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_register_retail);
            getSupportActionBar().hide();

            editText_phone=findViewById(R.id.editText_phone);
            editText_enterprise=findViewById(R.id.editText_enterprise);
            editText_product=findViewById(R.id.editText_product);
            editText_description=findViewById(R.id.editText_description);
            button_next=findViewById(R.id.button_next);
            listView=findViewById(R.id.listView);
            scrollView=findViewById(R.id.scrollView);
            constraintLayout=findViewById(R.id.constraintLayout);
            editText_social=findViewById(R.id.editText_social);
            textView_productType=findViewById(R.id.textView_productType);
            checkBox_delivery=findViewById(R.id.checkBox_delivery);
            checkBox_payment=findViewById(R.id.checkBox_payment);

            firestore=FirebaseFirestore.getInstance();

            preferences=getSharedPreferences("LOGIN_INFO",MODE_PRIVATE);
            editor=preferences.edit();

            String list[]={"GeneralStore","Garments","BeautyProducts","Footwear","WatchStore","ToyShop"};
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    productType=adapterView.getItemAtPosition(i).toString();
                    textView_productType.setText(productType);
                    Toast.makeText(RegisterRetailActivity.this,productType , Toast.LENGTH_SHORT).show();
                }
            });

            listView.setNestedScrollingEnabled(true);

        }
        public void onClickNext(View v)
        {
            enterpriseName=editText_enterprise.getText().toString();
            phone=editText_phone.getText().toString();
            description=editText_description.getText().toString();
            social=editText_social.getText().toString();
            payment=checkBox_payment.isChecked();
            delivery=checkBox_delivery.isChecked();

            if(description.isEmpty())
                description="No product description provided";
            if(phone.length()<10)
                Toast.makeText(this, "Enter your valid phone no. by which people will contact you", Toast.LENGTH_SHORT).show();
            else if(enterpriseName.isEmpty())
                Toast.makeText(this, "Enter name of your shop", Toast.LENGTH_SHORT).show();
            else if(productType.isEmpty())
                Toast.makeText(this,"Enter your shop type",Toast.LENGTH_SHORT).show();
            else
            {
                editor.putString("Uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                editor.commit();
                String firstname=preferences.getString("Firstname","");
                String lastname=preferences.getString("Lastname","");
                String email=preferences.getString("email","");

                HashMap<String,Object> map=new HashMap<>();
                map.put("ShopType",productType);
                map.put("Phone",phone);
                map.put("ProductDescription",description);
                map.put("Social",social);
                //map.put("Rating",0);
                map.put("Firstname",firstname);
                map.put("Lastname",lastname);
                map.put("email",email);
                map.put("ShopName",enterpriseName);


                //map.put("TotalRatings",0);

                editor.remove("Firstname");
                editor.remove("Lastname");

                Log.d("Hashmap","Hashmap created");

                HashMap<String,Object> map3=new HashMap<>();
                map3.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),"Retailer");
                firestore.collection("Users").document("UserType").set(map3,SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(RegisterRetailActivity.this, "Welcome!!!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(RegisterRetailActivity.this, "Unable to link to database", Toast.LENGTH_SHORT).show();
                            }
                        });

                HashMap<String,Object> map2=new HashMap<>();
                map2.put(FirebaseAuth.getInstance().getCurrentUser().getUid(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                firestore.collection("ShopType").document(productType).set(map2,SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                    Toast.makeText(RegisterRetailActivity.this, "Welcome!!!", Toast.LENGTH_SHORT).show();
                                else
                                    Toast.makeText(RegisterRetailActivity.this, "Unable to link to database", Toast.LENGTH_SHORT).show();
                            }
                        });

                firestore.collection("Retailer").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterRetailActivity.this, "Welcome!!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterRetailActivity.this,MapsActivity.class));
                                }
                                else
                                    Toast.makeText(RegisterRetailActivity.this, "Unable to link to database", Toast.LENGTH_SHORT).show();
                            }
                        });

                HashMap<String,Object> map4=new HashMap<>();
                map4.put("Uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                map4.put("Rating",0);
                map4.put("TotalRatings",0);
                map4.put("PaymentMode",payment);
                map4.put("Delivery",delivery);
                firestore.collection("EnterpriseType").document("Type").collection(productType).document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map4, SetOptions.merge())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterRetailActivity.this, "Welcome!!!", Toast.LENGTH_SHORT).show();
                                    //startActivity(new Intent(RegisterRetailActivity.this,MapsActivity.class));
                                }
                                else
                                    Toast.makeText(RegisterRetailActivity.this, "Unable to link to database", Toast.LENGTH_SHORT).show();
                            }
                        });
                firestore.collection("Retailer").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("Favourites", FieldValue.arrayUnion("Sample"));
                firestore.collection("Retailer").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update("Chats", FieldValue.arrayUnion("Sample"));
                Intent goToMap=new Intent(RegisterRetailActivity.this,MapsActivity.class);
                goToMap.putExtra("Type",productType);
                startActivity(goToMap);
            }


        }
    }
