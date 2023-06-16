package com.example.slot_analysis_v2

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
            SQL_CREATE_ENTRIES
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデートの判別
        db.execSQL(
            SQL_DELETE_ENTRIES
        )
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // データーベースのバージョン
        private const val DATABASE_VERSION = 1

        // データーベース名
        private const val DATABASE_NAME = "slot.db"
        private const val TABLE_NAME = "result"
        private const val _ID = "id"
        private const val COLUMN_NAME_1= "date"
        private const val COLUMN_NAME_2 = "start"
        private const val COLUMN_NAME_3 = "end"
        private const val COLUMN_NAME_4 = "total"
        private const val COLUMN_NAME_5 = "koyaku1"
        private const val COLUMN_NAME_6 = "koyaku2"
        private const val COLUMN_NAME_7 = "bb"
        private const val COLUMN_NAME_8 = "rb"
        private const val COLUMN_NAME_9 = "cherrybb"
        private const val COLUMN_NAME_10 = "cherryrb"
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_1 + " TEXT," +
                COLUMN_NAME_2 + " TEXT," +
                COLUMN_NAME_3 + " TEXT," +
                COLUMN_NAME_4 + " INTEGER," +
                COLUMN_NAME_5 + " INTEGER," +
                COLUMN_NAME_6 + " INTEGER," +
                COLUMN_NAME_7 + " INTEGER," +
                COLUMN_NAME_8 + " INTEGER," +
                COLUMN_NAME_9 + " INTEGER," +
                COLUMN_NAME_10 + " INTEGER)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}