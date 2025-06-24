package com.example.demo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "school.db";
    private static final int DATABASE_VERSION = 1;
    // 学生表创建SQL
    private static final String CREATE_STUDS_TABLE =
            "CREATE TABLE students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "gender TEXT NOT NULL," +
                    "stu_class TEXT NOT NULL" +
                    ");";
    // 教师表
    private static final String CREATE_TCHS_TABLE =
            "CREATE TABLE teachers (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "password TEXT NOT NULL," +
                    "gender TEXT NOT NULL" +
                    ");";
    // 课程表
    private static final String CREATE_COURSES_TABLE =
            "CREATE TABLE courses (" +
                    "id INTEGER NOT NULL," +
                    "name TEXT NOT NULL" +
                    ");";
    // 成绩表创建SQL
    private static final String CREATE_SCORES_TABLE =
            "CREATE TABLE scores (" +
                    "stu_id INTEGER NOT NULL," +
                    "stu_name TEXT NOT NULL," +
                    "course_id INTEGER NOT NULL," +
                    "course_name TEXT NOT NULL," +
                    "score Integer NOT NULL" +
                    ");";

    public MyDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDS_TABLE);
        db.execSQL(CREATE_TCHS_TABLE);
        db.execSQL(CREATE_SCORES_TABLE);
        db.execSQL(CREATE_COURSES_TABLE);
        initStu(db);
        initTch(db);
        initCourse(db);
        initScore(db);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("DROP TABLE IF EXISTS teachers");
        db.execSQL("DROP TABLE IF EXISTS courses");
        db.execSQL("DROP TABLE IF EXISTS scores");
        onCreate(db);
    }

    private void initStu(SQLiteDatabase db){
        // 学生姓名数组
        String[] studentNames = {
                "陈昊", "林婉清", "王宇轩", "苏雨桐", "赵逸飞",
                "许诗瑶", "刘泽远", "周若琳", "吴俊朗", "郑雅琪"
        };

        // 性别数组（与姓名对应）
        String[] genders = {
                "男", "女", "男", "女", "男",
                "女", "男", "女", "男", "女"
        };

        // 密码统一初始化为123456（实际应用中应该加密存储）
        String defaultPassword = "123456";
        String stuClass = "高一1班";

        for (int i = 0; i < studentNames.length; i++) {
            ContentValues values = new ContentValues();

            // 生成5位ID，以25开头（25001-25010）
            values.put("id", 25001 + i);  // 25001, 25002,..., 25010

            values.put("name", studentNames[i]);
            values.put("password", defaultPassword);
            values.put("gender", genders[i]);
            values.put("stu_class", stuClass);

            db.insert("students", null, values);
        }
    }
    private void initTch(SQLiteDatabase db){
        String[] teacherNames = {"张丽", "李菱", "王伟"};
        String[] teacherPasswords = {"tch123", "tch456", "tch789"}; // 建议实际应用中加密存储
        String[] genders = {"女", "女", "男"};

        for (int i = 0; i < teacherNames.length; i++) {
            ContentValues values = new ContentValues();

            // 生成3位ID（001, 002, 003）
            values.put("id", i + 1); // 由于是AUTOINCREMENT，这行可以省略
            values.put("name", teacherNames[i]);
            values.put("password", teacherPasswords[i]);
            values.put("gender",genders[i]);
            db.insert("teachers", null, values);
    }
    }
    private void initCourse(SQLiteDatabase db){
        String[] courseName = {"语文", "数学", "英语", "物理", "化学", "生物"};
        for (int i = 0; i < 6; i++){
            ContentValues values = new ContentValues();
            values.put("id", i+1);
            values.put("name", courseName[i]);
            db.insert("courses", null, values);
        }
    }
    private void initScore(SQLiteDatabase db){
        // 课程数据（id和名称对应）
        int[] courseIds = {1, 2, 3, 4, 5, 6};
        String[] courseNames = {"语文", "数学", "英语", "物理", "化学", "生物"};

        // 10名学生ID（假设从25001到25010）
        int[] studentIds = {25001, 25002, 25003, 25004, 25005,
                25006, 25007, 25008, 25009, 25010};

        // 10名学生姓名（与ID对应）
        String[] studentNames = {
                "陈昊", "林婉清", "王宇轩", "苏雨桐", "赵逸飞",
                "许诗瑶", "刘泽远", "周若琳", "吴俊朗", "郑雅琪"
        };

        // 开始事务以提高性能
        db.beginTransaction();
        try {
            // 为每名学生生成6门课程成绩
            for (int i = 0; i < studentIds.length; i++) {
                for (int j = 0; j < courseIds.length; j++) {
                    ContentValues values = new ContentValues();
                    values.put("stu_id", studentIds[i]);
                    values.put("stu_name", studentNames[i]);
                    values.put("course_id", courseIds[j]);
                    values.put("course_name", courseNames[j]);

                    // 生成随机成绩
                    int score;
                    if(courseIds[j]<=3){
                        score = (int)(90 + Math.random() * 60);
                    }
                    else score = (int)(60 + Math.random() * 40);
                    values.put("score", score);
                    db.insert("scores", null, values);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}
