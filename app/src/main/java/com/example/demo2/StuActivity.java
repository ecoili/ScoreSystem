package com.example.demo2;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class StuActivity extends AppCompatActivity {
    private TextView tvId, tvName, tvClass;
    private TextView[] tvScores = new TextView[6];
    private TextView tvSum;
    private Button btnLogout;
    private MyDataBase dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        dbHelper = new MyDataBase(this);

        // 初始化视图
        tvId = findViewById(R.id.tv_id);
        tvName = findViewById(R.id.tv_name);
        tvClass = findViewById(R.id.tv_class);

        // 初始化成绩视图
        tvScores[0] = findViewById(R.id.tv_chinese);
        tvScores[1] = findViewById(R.id.tv_math);
        tvScores[2] = findViewById(R.id.tv_english);
        tvScores[3] = findViewById(R.id.tv_physics);
        tvScores[4] = findViewById(R.id.tv_chemistry);
        tvScores[5] = findViewById(R.id.tv_biology);

        tvSum = findViewById(R.id.tv_sum);

        btnLogout = findViewById(R.id.btn_login); // 注意：这里使用了布局中的退出登录按钮

        // 获取传递过来的学生信息
        int id = getIntent().getIntExtra("id", 0);
        String name = getIntent().getStringExtra("name");
        String stuClass = getIntent().getStringExtra("stu_class");

        // 显示基本信息
        tvId.setText(String.valueOf(id));
        tvName.setText(name);
        tvClass.setText(stuClass);

        // 查询并显示成绩
        loadStudentScores(id);

        // 退出登录按钮点击事件
        btnLogout.setOnClickListener(v -> {
            finish(); // 关闭当前Activity，返回登录界面
        });
    }
    private void loadStudentScores(int studentId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询该学生的所有成绩
        Cursor cursor = db.query("scores",
                new String[]{"course_id", "score"},
                "stu_id = ?",
                new String[]{String.valueOf(studentId)},
                null, null, "course_id ASC");
        int sum = 0;
        // 显示成绩
        while (cursor.moveToNext()) {
            int courseId = cursor.getInt(cursor.getColumnIndexOrThrow("course_id"));
            int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
            sum += score;
            if (courseId >= 1 && courseId <= 6) {
                tvScores[courseId - 1].setText(String.valueOf(score));
            }
        }
        tvSum.setText(String.valueOf(sum));

        cursor.close();
        db.close();
    }
}
