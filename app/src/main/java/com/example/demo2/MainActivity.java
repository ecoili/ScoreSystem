package com.example.demo2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText etAccount, etPassword;
    private RadioButton rbStudent, rbTeacher;
    private Button btnLogin;
    private MyDataBase dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDataBase(this);

        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        rbStudent = findViewById(R.id.rb_student);
        rbTeacher = findViewById(R.id.rb_teacher);
        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (account.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入账号和密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rbStudent.isChecked()) {
                    loginAsStudent(account, password);
                } else {
                    loginAsTeacher(account, password);
                }

            }
        });
    }
    private void loginAsStudent(String account, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询学生信息
        Cursor cursor = db.query("students",
                new String[]{"id", "name", "stu_class"},
                "id = ? AND password = ?",
                new String[]{account, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            // 登录成功
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String stuClass = cursor.getString(cursor.getColumnIndexOrThrow("stu_class"));

            // 跳转到学生界面并传递数据
            Intent intent = new Intent(this, StuActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("stu_class", stuClass);
            startActivity(intent);
        } else {
            // 登录失败
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
    private void loginAsTeacher(String account, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询教师信息
        Cursor cursor = db.query("teachers",
                new String[]{"id", "name", "gender"},
                "id = ? AND password = ?",
                new String[]{account, password},
                null, null, null);

        if (cursor.moveToFirst()) {
            // 登录成功
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));

            // 跳转到教师界面并传递数据
            Intent intent = new Intent(this, TchActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("name", name);
            intent.putExtra("gender", gender);
            startActivity(intent);
        } else {
            // 登录失败
            Toast.makeText(this, "账号或密码错误", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
        db.close();
    }
}