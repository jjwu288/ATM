package com.home.atm;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();//完成Tag設定//
    private EditText edUserid;
    private EditText edPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSharedPreferences("atm",MODE_PRIVATE)//寫入喜好設定//
                .edit()
                .putInt("LEVEL",3)
                .putString("NAME","jj")
                .commit();//使用commit指令是因為要直接讀取下一行，如果不急著讀取下一行則用.apply()指令//

        int LEVEL = getSharedPreferences("atm",MODE_PRIVATE)//取得，因為是放入int值，所以就getInt//
                .getInt("LEVEL",0);//預設0是因為怕它找不到資料時，但目前是有資料//
        Log.d(TAG, "onCreate: " + LEVEL);//logd按下tab鍵，可快速產生log cat語法//
        edUserid = findViewById(R.id.eduserid);
        edPasswd = findViewById(R.id.passwd);
    }

    public void login(View view){
        final String userid = edUserid.getText().toString();
        final String passwd = edPasswd.getText().toString();
        FirebaseDatabase.getInstance().getReference("users").child(userid).child("password")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String pw = (String) dataSnapshot.getValue();
                        if(pw.equals(passwd)){
                            //save users id//
                            getSharedPreferences("atm",MODE_PRIVATE)
                                    .edit()//呼叫edit方法取得編輯器//
                                    .putString("USERID",userid) //儲存userid，在匿名類別使用區域變數，區域變數要變成FINAL才可使用(string前面再加上final)//
                                    .apply();
                            setResult(RESULT_OK);
                            finish();
                        }else {
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("登入結果")
                                    .setMessage("登入失敗")
                                    .setPositiveButton("ok",null)
                                    .show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
       /* if ( "jj".equals(userid) && "1234".equals(passwd)){
            setResult(RESULT_OK);
            finish(); 在還沒串入Firebase資料庫時的寫法(為登入時的假資料)
        }*/

    }

    public void quit(View view){

    }
}
