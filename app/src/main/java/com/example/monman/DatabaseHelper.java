package com.example.monman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "thrivedb.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USER = "user";
    private static final String TABLE_CATATAN = "catatan";
    private static final String TABLE_KELUARGA = "keluarga";
    private static final String TABLE_GOALS = "goals";
    private static final String TABLE_GOALS_KELUARGA = "goals_keluarga";

    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NO_HP = "no_hp";
    private static final String COLUMN_KODE_KELUARGA_USER = "kode_keluarga_user";
    private static final String COLUMN_FAMILY_ROLE = "family_role";

    private static final String COLUMN_CATATAN_ID = "catatan_id";
    private static final String COLUMN_CATATAN_USER_ID = "catatan_user";
    private static final String COLUMN_CATATAN_KELUARGA_ID = "catatan_keluarga";
    private static final String COLUMN_CATATAN_AMOUNT = "total";
    private static final String COLUMN_CATATAN_TIPE = "tipe";
    private static final String COLUMN_CATATAN_DESKRIPSI = "deskripsi";
    private static final String COLUMN_CATATAN_DATE = "date";

    private static final String COLUMN_KELUARGA_ID = "keluarga_id";
    private static final String COLUMN_NAMA_KELUARGA = "nama_keluarga";

    private static final String COLUMN_GOALS_ID = "goals_id";
    private static final String COLUMN_GOALS_USER_ID = "user_id";
    private static final String COLUMN_GOALS_NAME = "goals_name";
    private static final String COLUMN_GOALS_AMOUNT = "amount";

    // Goals_keluarga table columns
    private static final String COLUMN_GOALS_KELUARGA_ID = "goals_keluarga_id";
    private static final String COLUMN_GOALS_KELUARGA_KELUARGA_ID = "keluarga_id";
    private static final String COLUMN_GOALS_KELUARGA_NAME = "goals_name";
    private static final String COLUMN_GOALS_KELUARGA_AMOUNT = "amount";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
// Create user table
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + "("
                + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_PASSWORD + " TEXT,"
                + COLUMN_NO_HP + " TEXT,"
                + COLUMN_KODE_KELUARGA_USER + " INTEGER DEFAULT 0,"
                + COLUMN_FAMILY_ROLE + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE_USER);

        String CREATE_TABLE_CATATAN = "CREATE TABLE IF NOT EXISTS " + TABLE_CATATAN + "("
                + COLUMN_CATATAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATATAN_USER_ID + " TEXT,"
                + COLUMN_CATATAN_KELUARGA_ID + " TEXT, "
                + COLUMN_CATATAN_TIPE + " TEXT,"
                + COLUMN_CATATAN_AMOUNT + " INTEGER ,"
                + COLUMN_CATATAN_DESKRIPSI + " TEXT, "
                + COLUMN_CATATAN_DATE + " DATE"
                + ")";
        db.execSQL(CREATE_TABLE_CATATAN);

        String CREATE_TABLE_KELUARGA = "CREATE TABLE IF NOT EXISTS " + TABLE_KELUARGA + "("
                + COLUMN_KELUARGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAMA_KELUARGA + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_KELUARGA);

        // Create goals table
        String CREATE_TABLE_GOALS = "CREATE TABLE IF NOT EXISTS " + TABLE_GOALS + "("
                + COLUMN_GOALS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GOALS_USER_ID + " TEXT,"
                + COLUMN_GOALS_NAME + " TEXT,"
                + COLUMN_GOALS_AMOUNT + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE_GOALS);

        // Create goals_keluarga table
        String CREATE_TABLE_GOALS_KELUARGA = "CREATE TABLE IF NOT EXISTS " + TABLE_GOALS_KELUARGA + "("
                + COLUMN_GOALS_KELUARGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GOALS_KELUARGA_KELUARGA_ID + " TEXT,"
                + COLUMN_GOALS_KELUARGA_NAME + " TEXT,"
                + COLUMN_GOALS_KELUARGA_AMOUNT + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE_GOALS_KELUARGA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATATAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KELUARGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GOALS_KELUARGA);

        // Recreate tables
        onCreate(db);
    }

    public long addUser( String username, String password, String email, String familyRole) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_NO_HP, email);
        values.put(COLUMN_FAMILY_ROLE, familyRole);
        return db.insert(TABLE_USER, null, values);
    }

    public Cursor getUserData(String username) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_USER_NAME + " = ?";

        // Parameters for the query
        String[] selectionArgs = {username};

        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getFamily(String kodeKeluarga) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_KELUARGA + " WHERE " + COLUMN_KELUARGA_ID + " = ?";

        // Parameters for the query
        String[] selectionArgs = {kodeKeluarga};

        return db.rawQuery(query, selectionArgs);
    }
    public Cursor getUserFamily(String kodeKeluarga) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_KODE_KELUARGA_USER + " = ?";

        // Parameters for the query
        String[] selectionArgs = {kodeKeluarga};

        return db.rawQuery(query, selectionArgs);
    }


    public Cursor login(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the raw SQL query
        String query = "SELECT " + COLUMN_USER_ID + ", " +
                COLUMN_USER_NAME + ", " +
                COLUMN_NO_HP + ", " +
                COLUMN_KODE_KELUARGA_USER + ", " +
                COLUMN_FAMILY_ROLE +
                " FROM " + TABLE_USER +
                " WHERE " + COLUMN_NO_HP + " = ? AND " + COLUMN_PASSWORD + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {username, password};

        return db.rawQuery(query, selectionArgs);

    }

    public int updateFamilyforUser(String id, String role, String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAMILY_ROLE, role);
        values.put(COLUMN_KODE_KELUARGA_USER, familyId);
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long addCatatan(String userId, String familyId, String tipe, BigDecimal amount, String deskripsi, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATATAN_USER_ID, userId);
        values.put(COLUMN_CATATAN_KELUARGA_ID, familyId);
        values.put(COLUMN_CATATAN_TIPE, tipe);
        values.put(COLUMN_CATATAN_AMOUNT, String.valueOf(amount));
        values.put(COLUMN_CATATAN_DESKRIPSI, deskripsi);
        values.put(COLUMN_CATATAN_DATE, date);
        return db.insert(TABLE_CATATAN, null, values);
    }

    public int updateCatatan(String id, String tipe, BigDecimal amount, String deskripsi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATATAN_TIPE, tipe);
        values.put(COLUMN_CATATAN_AMOUNT, String.valueOf(amount));
        values.put(COLUMN_CATATAN_DESKRIPSI, deskripsi);
        return db.update(TABLE_CATATAN, values, COLUMN_CATATAN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public boolean deleteCatatan(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CATATAN, COLUMN_CATATAN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0; // If rowsAffected is greater than 0, deletion was successful
    }


    public Cursor getCatatanByMonthAndYearKeluarga(String month, String year, String kodeKeluarga) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_ID + ", catatan." + COLUMN_CATATAN_USER_ID + ", " +
                "catatan." + COLUMN_CATATAN_KELUARGA_ID + ", catatan." + COLUMN_CATATAN_TIPE + ", " +
                "catatan." + COLUMN_CATATAN_AMOUNT + ", catatan." + COLUMN_CATATAN_DESKRIPSI + ", " +
                "catatan." + COLUMN_CATATAN_DATE + ", user." + COLUMN_USER_NAME + ", user." + COLUMN_FAMILY_ROLE + " " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 6, 2) = ? " +
                " AND SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 1, 4) = ? " +
                "AND user." + COLUMN_KODE_KELUARGA_USER + " = ? ";
        String[] selectionArgs = {month, year, kodeKeluarga};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getCatatanByMonthAndYear(String month, String year, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_ID + ", catatan." + COLUMN_CATATAN_USER_ID + ", " +
                "catatan." + COLUMN_CATATAN_KELUARGA_ID + ", catatan." + COLUMN_CATATAN_TIPE + ", " +
                "catatan." + COLUMN_CATATAN_AMOUNT + ", catatan." + COLUMN_CATATAN_DESKRIPSI + ", " +
                "catatan." + COLUMN_CATATAN_DATE + ", user." + COLUMN_USER_NAME + ", user." + COLUMN_FAMILY_ROLE + " " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 6, 2) = ? " +
                " AND SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 1, 4) = ? " +
                " AND catatan." + COLUMN_CATATAN_USER_ID + " = ? ";
        String[] selectionArgs = {month, year, userId};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getDataforGraph(String month, String year, String userId, String familyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_DESKRIPSI + ", SUM(catatan." + COLUMN_CATATAN_AMOUNT + ") AS total_amount " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 6, 2) = ? " +
                " AND SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 1, 4) = ? " +
                " AND catatan." + COLUMN_CATATAN_USER_ID + " = ? " +
                "GROUP BY catatan." + COLUMN_CATATAN_DESKRIPSI;
        String[] selectionArgs = {month, year, userId};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getDataforGraphFamily(String month, String year, String userId, String familyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_TIPE + ", SUM(catatan." + COLUMN_CATATAN_AMOUNT + ") AS total_amount " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 6, 2) = ? " +
                " AND SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 1, 4) = ? " +
                " AND user." + COLUMN_KODE_KELUARGA_USER + " = ? " +
                "GROUP BY catatan." + COLUMN_CATATAN_TIPE;
        String[] selectionArgs = {month, year, familyId};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getDataProfitLoss(String month, String year, String userId, String familyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_TIPE + ", SUM(catatan." + COLUMN_CATATAN_AMOUNT + ") AS total_amount " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 6, 2) = ? " +
                " AND SUBSTR(catatan." + COLUMN_CATATAN_DATE + ", 1, 4) = ? " +
                " AND catatan." + COLUMN_CATATAN_USER_ID + " = ? " +
                "GROUP BY catatan." + COLUMN_CATATAN_TIPE;
        String[] selectionArgs = {month, year, userId};
        return db.rawQuery(query, selectionArgs);
    }
    public Cursor getDataProfitLossPerWeek(String startDate, String endDate, String userId, String familyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT catatan." + COLUMN_CATATAN_DATE + ", catatan." + COLUMN_CATATAN_TIPE + ", " +
                "SUM(catatan." + COLUMN_CATATAN_AMOUNT + ") AS total_amount " +
                "FROM " + TABLE_CATATAN + " AS catatan " +
                "JOIN " + TABLE_USER + " AS user ON catatan." + COLUMN_CATATAN_USER_ID + " = user." + COLUMN_USER_ID +
                " WHERE catatan." + COLUMN_CATATAN_DATE + " BETWEEN ? AND ? " +
                " AND catatan." + COLUMN_CATATAN_USER_ID + " = ? " +
                "GROUP BY catatan." + COLUMN_CATATAN_DATE + ", catatan." + COLUMN_CATATAN_TIPE +
                " ORDER BY catatan." + COLUMN_CATATAN_DATE; // Optional: Order by date
        String[] selectionArgs = {startDate, endDate, userId};
        return db.rawQuery(query, selectionArgs);
    }

    public long addKeluarga(String namaKeluarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAMA_KELUARGA, namaKeluarga);
        return db.insert(TABLE_KELUARGA, null, values);
    }

    public int updateUserFamily(String id, String inFamily, String roleKeluarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KODE_KELUARGA_USER, inFamily);
        values.put(COLUMN_FAMILY_ROLE, roleKeluarga);
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
