package com.example.demo2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbManager {
    private MyDataBase mdb;
    public DbManager(Context context){
        mdb = new MyDataBase(context);
    }
    // 添加成绩
    public String addScore(Scores score){
        SQLiteDatabase db = mdb.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stu_id",score.getStuID());
        values.put("stu_name",score.getStuName());
        values.put("course_id",score.getCorID());
        values.put("course_name",score.getCorName());
        values.put("score",score.getScore());
        long insert = db.insert("scores", null, values);
        if(insert == -1) return "fail";
        db.close();
        return "success";
    }
    // 删除成绩
    public String deleteScore(int stuId, int courseId) {
        SQLiteDatabase db = mdb.getWritableDatabase();
        String whereClause = "stu_id = ? AND course_id = ?";
        String[] whereArgs = {String.valueOf(stuId), String.valueOf(courseId)};

        int deletedRows = db.delete("scores", whereClause, whereArgs);
        db.close();

        return deletedRows > 0 ? "success" : "fail";
    }
    // 修改成绩
    public String updateScore(int stuId, int courseId, int newScore) {
        SQLiteDatabase db = mdb.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("score", newScore);

        String whereClause = "stu_id = ? AND course_id = ?";
        String[] whereArgs = {String.valueOf(stuId), String.valueOf(courseId)};

        int updatedRows = db.update("scores", values, whereClause, whereArgs);
        db.close();

        return updatedRows > 0 ? "success" : "fail";
    }

}
