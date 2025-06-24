package com.example.demo2;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TchActivity extends AppCompatActivity {
    private RecyclerView rvScores;
    private EditText etSearch;
    private Spinner spCourseFilter;
    private Button btnAddScore;
    private MyDataBase dbHelper;
    private ScoreAdapter scoreAdapter;
    private List<ScoreItem> scoreList = new ArrayList<>();
    private List<String> courseNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        dbHelper = new MyDataBase(this);

        // 初始化视图
        rvScores = findViewById(R.id.rv_scores);
        etSearch = findViewById(R.id.et_search);
        spCourseFilter = findViewById(R.id.sp_course_filter);
        btnAddScore = findViewById(R.id.btn_add_score);

        // 设置RecyclerView
        rvScores.setLayoutManager(new LinearLayoutManager(this));
        scoreAdapter = new ScoreAdapter(this, scoreList);
        rvScores.setAdapter(scoreAdapter);

        // 初始化课程筛选下拉框
        initCourseSpinner();

        // 加载所有成绩数据
        loadAllScores();

        // 设置搜索监听
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = etSearch.getText().toString().trim();
            if (!keyword.isEmpty()) {
                searchScoresByName(keyword);
            } else {
                loadAllScores();
            }
            return true;
        });

        // 设置课程筛选监听
        spCourseFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) { // 第一个是"全部课程"
                    String courseName = courseNames.get(position - 1);
                    filterScoresByCourse(courseName);
                } else {
                    loadAllScores();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // 设置添加成绩按钮点击事件
        btnAddScore.setOnClickListener(v -> {
            Intent intent = new Intent(TchActivity.this, AddScoreActivity.class);
            startActivityForResult(intent, 1);
        });

        // 设置列表项点击事件
        scoreAdapter.setOnItemClickListener(position -> {
            ScoreItem item = scoreList.get(position);
            showEditDeleteDialog(item);
        });
    }

    private void initCourseSpinner() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        courseNames.clear();

        // 查询所有课程
        Cursor cursor = db.query("courses",
                new String[]{"name"},
                null, null, null, null, null);

        while (cursor.moveToNext()) {
            courseNames.add(cursor.getString(0));
        }
        cursor.close();
        db.close();

        // 添加"全部课程"选项
        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("全部课程");
        spinnerItems.addAll(courseNames);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCourseFilter.setAdapter(adapter);
    }

    private void loadAllScores() {
        scoreList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 查询所有成绩，关联学生表获取性别和班级信息
        String query = "SELECT s.stu_id, s.stu_name, st.gender, st.stu_class, " +
                "s.course_name, s.score " +
                "FROM scores s " +
                "JOIN students st ON s.stu_id = st.id " +
                "ORDER BY s.stu_id, s.course_id";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            ScoreItem item = new ScoreItem();
            item.setStuId(cursor.getInt(0));
            item.setStuName(cursor.getString(1));
            item.setGender(cursor.getString(2));
            item.setStuClass(cursor.getString(3));
            item.setCourseName(cursor.getString(4));
            item.setScore(cursor.getInt(5));

            scoreList.add(item);
        }

        cursor.close();
        db.close();
        scoreAdapter.notifyDataSetChanged();
    }

    private void searchScoresByName(String name) {
        scoreList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.stu_id, s.stu_name, st.gender, st.stu_class, " +
                "s.course_name, s.score " +
                "FROM scores s " +
                "JOIN students st ON s.stu_id = st.id " +
                "WHERE s.stu_name LIKE ? " +
                "ORDER BY s.course_id";

        Cursor cursor = db.rawQuery(query, new String[]{"%" + name + "%"});

        while (cursor.moveToNext()) {
            ScoreItem item = new ScoreItem();
            item.setStuId(cursor.getInt(0));
            item.setStuName(cursor.getString(1));
            item.setGender(cursor.getString(2));
            item.setStuClass(cursor.getString(3));
            item.setCourseName(cursor.getString(4));
            item.setScore(cursor.getInt(5));

            scoreList.add(item);
        }

        cursor.close();
        db.close();
        scoreAdapter.notifyDataSetChanged();
    }

    private void filterScoresByCourse(String courseName) {
        scoreList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT s.stu_id, s.stu_name, st.gender, st.stu_class, " +
                "s.course_name, s.score " +
                "FROM scores s " +
                "JOIN students st ON s.stu_id = st.id " +
                "WHERE s.course_name = ? " +
                "ORDER BY s.stu_id";

        Cursor cursor = db.rawQuery(query, new String[]{courseName});

        while (cursor.moveToNext()) {
            ScoreItem item = new ScoreItem();
            item.setStuId(cursor.getInt(0));
            item.setStuName(cursor.getString(1));
            item.setGender(cursor.getString(2));
            item.setStuClass(cursor.getString(3));
            item.setCourseName(cursor.getString(4));
            item.setScore(cursor.getInt(5));

            scoreList.add(item);
        }

        cursor.close();
        db.close();
        scoreAdapter.notifyDataSetChanged();
    }

    private void showEditDeleteDialog(ScoreItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("操作选项");
        builder.setMessage("选择要执行的操作");

        builder.setPositiveButton("编辑", (dialog, which) -> {
            showEditScoreDialog(item);
        });

        builder.setNegativeButton("删除", (dialog, which) -> {
            deleteScore(item);
        });

        builder.setNeutralButton("取消", null);
        builder.show();
    }

    private void showEditScoreDialog(ScoreItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑成绩");

        View view = getLayoutInflater().inflate(R.layout.dialog_edit_score, null);
        EditText etScore = view.findViewById(R.id.et_score);
        etScore.setText(String.valueOf(item.getScore()));

        builder.setView(view);
        builder.setPositiveButton("保存", (dialog, which) -> {
            String newScoreStr = etScore.getText().toString().trim();
            if (!newScoreStr.isEmpty()) {
                try {
                    int newScore = Integer.parseInt(newScoreStr);
                    if (newScore >= 0 && newScore <= 150) {
                        updateScore(item.getStuId(), getCourseId(item.getCourseName()), newScore);
                    } else {
                        Toast.makeText(this, "成绩必须在0-150之间", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "请输入有效的数字", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void deleteScore(ScoreItem item) {
        DbManager dbManager = new DbManager(this);
        String result = dbManager.deleteScore(item.getStuId(), getCourseId(item.getCourseName()));

        if ("success".equals(result)) {
            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
            loadAllScores(); // 刷新列表
        } else {
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateScore(int stuId, int courseId, int newScore) {
        DbManager dbManager = new DbManager(this);
        String result = dbManager.updateScore(stuId, courseId, newScore);

        if ("success".equals(result)) {
            Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
            loadAllScores(); // 刷新列表
        } else {
            Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show();
        }
    }

    private int getCourseId(String courseName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("courses",
                new String[]{"id"},
                "name = ?",
                new String[]{courseName},
                null, null, null);

        int courseId = -1;
        if (cursor.moveToFirst()) {
            courseId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return courseId;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadAllScores(); // 添加成绩后刷新列表
        }
    }
}