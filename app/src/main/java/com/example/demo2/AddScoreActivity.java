package com.example.demo2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddScoreActivity extends AppCompatActivity {
    private Spinner spStudent, spCourse;
    private EditText etScore;
    private Button btnAdd;
    private Button btnBack;
    private MyDataBase dbHelper;
    private List<String> studentNames = new ArrayList<>();
    private List<Integer> studentIds = new ArrayList<>();
    private List<String> courseNames = new ArrayList<>();
    private List<Integer> courseIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        dbHelper = new MyDataBase(this);

        spStudent = findViewById(R.id.sp_student);
        spCourse = findViewById(R.id.sp_course);
        etScore = findViewById(R.id.et_score);
        btnAdd = findViewById(R.id.btn_add_score);
        btnBack = findViewById(R.id.btn_back);

        // 初始化学生和课程下拉框
        initStudentSpinner();
        initCourseSpinner();

        btnAdd.setOnClickListener(v -> addScore());
        btnBack.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }


    private void initStudentSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        studentNames.clear();
        studentIds.clear();

        // 查询所有学生
        Cursor cursor = db.query("students",
                new String[]{"id", "name"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            studentIds.add(cursor.getInt(0));
            studentNames.add(cursor.getString(1));
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, studentNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStudent.setAdapter(adapter);
    }

    private void initCourseSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        courseNames.clear();
        courseIds.clear();

        // 查询所有课程
        Cursor cursor = db.query("courses",
                new String[]{"id", "name"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            courseIds.add(cursor.getInt(0));
            courseNames.add(cursor.getString(1));
        }
        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourse.setAdapter(adapter);
    }

    private void addScore() {
        int studentPos = spStudent.getSelectedItemPosition();
        int coursePos = spCourse.getSelectedItemPosition();
        String scoreStr = etScore.getText().toString().trim();

        if (studentPos == -1 || coursePos == -1) {
            Toast.makeText(this, "请选择学生和课程", Toast.LENGTH_SHORT).show();
            return;
        }

        if (scoreStr.isEmpty()) {
            Toast.makeText(this, "请输入成绩", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int score = Integer.parseInt(scoreStr);
            if (score < 0 || score > 150) {
                Toast.makeText(this, "成绩必须在0-150之间", Toast.LENGTH_SHORT).show();
                return;
            }

            int stuId = studentIds.get(studentPos);
            String stuName = studentNames.get(studentPos);
            int courseId = courseIds.get(coursePos);
            String courseName = courseNames.get(coursePos);

            // 检查是否已存在该学生的这门课程成绩
            if (isScoreExist(stuId, courseId)) {
                Toast.makeText(this, "该学生这门课程的成绩已存在", Toast.LENGTH_SHORT).show();
                return;
            }

            // 添加成绩
            Scores newScore = new Scores(stuId, courseId, courseName, stuName, score);
            DbManager dbManager = new DbManager(this);
            String result = dbManager.addScore(newScore);

            if ("success".equals(result)) {
                Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isScoreExist(int stuId, int courseId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("scores",
                new String[]{"stu_id"},
                "stu_id = ? AND course_id = ?",
                new String[]{String.valueOf(stuId), String.valueOf(courseId)},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
}
