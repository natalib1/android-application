//Displays the main page

package com.natali.natalishopapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.natali.natalishopapp.FirebaseStorage.FirebaseHandler;
import com.natali.natalishopapp.Objects.User;
import com.natali.natalishopapp.SharedPreference.SharedPref;
import com.natali.natalishopapp.Utils.Constants;
import com.natali.natalishopapp.Utils.Utils;

public class MainActivity extends AppCompatActivity {

    private String userId;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView ivb = (TextView) findViewById(R.id.bikini_imageView);
        TextView ivs = (TextView) findViewById(R.id.one_imageView);
        TextView ivr = (TextView) findViewById(R.id.brazilian_imageView);
        TextView ivd = (TextView) findViewById(R.id.dress_imageView);

        ivb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(Constants.CATEGORY, "bikini");
                startActivity(intent);
            }
        });

        ivs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(Constants.CATEGORY, "one-piece");
                startActivity(intent);
            }
        });

        ivr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(Constants.CATEGORY, "brazilian");
                startActivity(intent);
            }
        });

        ivd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra(Constants.CATEGORY, "dresses");
                startActivity(intent);
            }
        });

    }

    // Inflate the menu according to the user type; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        userId = SharedPref.shared().getUserId(MainActivity.this);
        if (userId != null) {
            userType = SharedPref.shared().getUserType(MainActivity.this);
        }

        if (userType == 1) {
            menu.getItem(1).setTitle(Utils.fixEmailStringBack(userId)); //user_id
            menu.getItem(2).setVisible(false); //sign_in
            menu.getItem(6).setVisible(false); //selling_history
            menu.getItem(7).setVisible(false); //product_management
        }else if (userType == 2){
            menu.getItem(1).setTitle(Utils.fixEmailStringBack(userId)); //user_id
            menu.getItem(2).setVisible(false); //sign_in
        }else {
            menu.getItem(1).setVisible(false); //user_id
            menu.getItem(3).setVisible(false); //log_off
            for (int i = 4; i < 8; i++)  //shopping_cart, purchases_history, selling_history, product_management
                menu.getItem(i).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_icon_shopping_cart:
                if (SharedPref.shared().getUserId(this) != null )
                {
                    Intent intent001 = new Intent(this, ShoppingcartActivity.class);
                    startActivity(intent001);
                }
                else{
                    Utils.showAlertOk(this,"Login","You must login!");
                }
                return true;
            case R.id.action_sign_in:
                Intent intent101 = new Intent(this, LoginActivity.class);
                startActivity(intent101);
                return true;
            case R.id.action_log_off:
                Utils.showAlertOkCancel(MainActivity.this, "Log-off", "Are you sure you want to log off?", new Utils.ShowAlert_CallBack() {
                            @Override
                            public void ShowAlert_UserClickedYes() {
                                SharedPref.shared().deleteAll(MainActivity.this);
                                Intent intent = getIntent();
                                overridePendingTransition(0, 0);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(intent);
                            }
                });
                return true;
            case R.id.action_shopping_cart:
                Intent intent103 = new Intent(this, ShoppingcartActivity.class);
                startActivity(intent103);
                return true;
            case R.id.action_purchases_history:
                Intent intent104 = new Intent(this, PurchaseshistoryActivity.class);
                startActivity(intent104);
                return true;
            case R.id.action_selling_history:
                Intent intent105 = new Intent(this, SellinghistoryActivity.class);
                startActivity(intent105);
                return true;
            case R.id.action_product_management:
                Intent intent106 = new Intent(this, ProductmanagementActivity.class);
                startActivity(intent106);
                return true;
            case R.id.action_about:
                Intent intent107 = new Intent(this, AboutActivity.class);
                startActivity(intent107);
        }

        return super.onOptionsItemSelected(item);
    }

}
