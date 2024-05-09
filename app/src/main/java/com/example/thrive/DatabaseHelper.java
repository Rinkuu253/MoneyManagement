package com.example.thrive;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.math.BigDecimal;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "thrivedb.db";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_USER = "user";
    private static final String TABLE_CATATAN = "catatan";
    private static final String TABLE_WALLET_TOTAL = "wallet_total";
    private static final String TABLE_KELUARGA = "keluarga";
    private static final String TABLE_CATATAN_KELUARGA = "catatan_keluarga";
    private static final String TABLE_WALLET_KELUARGA_TOTAL = "wallet_keluarga_total";
    private static final String TABLE_GOALS = "goals";
    private static final String TABLE_GOALS_KELUARGA = "goals_keluarga";

    // User table columns
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NO_HP = "no_hp";
    private static final String COLUMN_IN_FAMILY = "in_family";
    private static final String COLUMN_FAMILY_ROLE = "family_role";

    // Catatan table columns
    private static final String COLUMN_CATATAN_ID = "catatan_id";
    private static final String COLUMN_CATATAN_USER_ID = "catatan_user";
    private static final String COLUMN_CATATAN_AMOUNT = "total";
    private static final String COLUMN_CATATAN_TIPE = "tipe";
    private static final String COLUMN_CATATAN_DESKRIPSI = "deskripsi";
    private static final String COLUMN_CATATAN_DATE = "date";

    // Wallet_total table columns
    private static final String COLUMN_WALLET_TOTAL_ID = "wallet_id";
    private static final String COLUMN_WALLET_TOTAL_USER_ID = "user_id";
    private static final String COLUMN_TOTAL_PENGELUARAN = "total_pengeluaran";
    private static final String COLUMN_TOTAL_PENGHASILAN = "total_penghasilan";
    private static final String COLUMN_WALLET_TOTAL_DATE = "date";



    // Keluarga table columns
    private static final String COLUMN_KELUARGA_ID = "keluarga_id";
    private static final String COLUMN_KELUARGA_USER_ID = "user_id";
    private static final String COLUMN_NAMA_KELUARGA = "nama_keluarga";
    private static final String COLUMN_KODE_KELUARGA = "kode_keluarga";

    // Catatan_keluarga table columns
    private static final String COLUMN_CATATAN_KELUARGA_ID = "id_catatan_keluarga";
    private static final String COLUMN_CATATAN_KELUARGA_KEL_ID = "keluarga_id_cat";
    private static final String COLUMN_CATATAN_KELUARGA_USER_ID = "keluarga_id_user";
    private static final String COLUMN_CATATAN_KELUARGA_AMOUNT = "total_catatan";
    private static final String COLUMN_CATATAN_KELUARGA_TIPE = "tipe_catatan_keluarga";
    private static final String COLUMN_CATATAN_KELUARGA_DESKRIPSI = "deskripsi_catatan_keluarga";
    private static final String COLUMN_CATATAN_KELUARGA_DATE = "date";

    // Wallet_keluarga_total table columns
    private static final String COLUMN_WALLET_KELUARGA_TOTAL_ID = "id_wallet_keluarga";
    private static final String COLUMN_WALLET_KELUARGA_TOTAL_KELUARGA_ID = "id_keluarga";
    private static final String COLUMN_WALLET_KELUARGA_TOTAL_DEBIT = "total_debit";
    private static final String COLUMN_WALLET_KELUARGA_TOTAL_CREDIT = "total_credit";
    private static final String COLUMN_WALLET_KELUARGA_TOTAL_DATE = "date";

    // Goals table columns
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
                + COLUMN_IN_FAMILY + " INTEGER DEFAULT 0,"
                + COLUMN_FAMILY_ROLE + " TEXT "
                + ")";
        db.execSQL(CREATE_TABLE_USER);

        // Create catatan table
        String CREATE_TABLE_CATATAN = "CREATE TABLE IF NOT EXISTS " + TABLE_CATATAN + "("
                + COLUMN_CATATAN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATATAN_USER_ID + " TEXT,"
                + COLUMN_CATATAN_TIPE + " TEXT,"
                + COLUMN_CATATAN_AMOUNT + " INTEGER ,"
                + COLUMN_CATATAN_DESKRIPSI + " TEXT, "
                + COLUMN_CATATAN_DATE + " DATE"
                + ")";
        db.execSQL(CREATE_TABLE_CATATAN);

        // Create wallet_total table
        String CREATE_TABLE_WALLET_TOTAL = "CREATE TABLE IF NOT EXISTS " + TABLE_WALLET_TOTAL + "("
                + COLUMN_WALLET_TOTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WALLET_TOTAL_USER_ID + " TEXT,"
                + COLUMN_TOTAL_PENGHASILAN + " REAL,"
                + COLUMN_TOTAL_PENGELUARAN + " REAL,"
                + COLUMN_WALLET_TOTAL_DATE + " DATE"
                + ")";
        db.execSQL(CREATE_TABLE_WALLET_TOTAL);

        // Create keluarga table
        String CREATE_TABLE_KELUARGA = "CREATE TABLE IF NOT EXISTS " + TABLE_KELUARGA + "("
                + COLUMN_KELUARGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_KELUARGA_USER_ID + " TEXT,"
                + COLUMN_NAMA_KELUARGA + " TEXT,"
                + COLUMN_KODE_KELUARGA + " INTEGER"
                + ")";
        db.execSQL(CREATE_TABLE_KELUARGA);

        String CREATE_TABLE_CATATAN_KELUARGA = "CREATE TABLE IF NOT EXISTS " + TABLE_CATATAN_KELUARGA + "("
                + COLUMN_CATATAN_KELUARGA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CATATAN_KELUARGA_KEL_ID + " INTEGER,"
                + COLUMN_CATATAN_KELUARGA_USER_ID + " INTEGER,"
                + COLUMN_CATATAN_KELUARGA_AMOUNT + " REAL,"
                + COLUMN_CATATAN_KELUARGA_TIPE + " TEXT,"
                + COLUMN_CATATAN_KELUARGA_DESKRIPSI + " TEXT,"
                + COLUMN_CATATAN_KELUARGA_DATE + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE_CATATAN_KELUARGA);

        // Create wallet_keluarga_total table
        String CREATE_TABLE_WALLET_KELUARGA_TOTAL = "CREATE TABLE IF NOT EXISTS " + TABLE_WALLET_KELUARGA_TOTAL + "("
                + COLUMN_WALLET_KELUARGA_TOTAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_WALLET_KELUARGA_TOTAL_KELUARGA_ID + " TEXT,"
                + COLUMN_WALLET_KELUARGA_TOTAL_DEBIT + " REAL,"
                + COLUMN_WALLET_KELUARGA_TOTAL_CREDIT + " REAL,"
                + COLUMN_WALLET_KELUARGA_TOTAL_DATE + " DATE"
                + ")";
        db.execSQL(CREATE_TABLE_WALLET_KELUARGA_TOTAL);

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
        // Drop existing tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATATAN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET_TOTAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KELUARGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATATAN_KELUARGA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WALLET_KELUARGA_TOTAL);
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

    public Cursor getUserData() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_USER;

        return db.rawQuery(query, null);
    }

    public Cursor login(String nomorHp, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the raw SQL query
        String query = "SELECT " + COLUMN_USER_ID + ", " +
                COLUMN_USER_NAME + ", " +
                COLUMN_NO_HP + ", " +
                COLUMN_IN_FAMILY + ", " +
                COLUMN_FAMILY_ROLE +
                " FROM " + TABLE_USER +
                " WHERE " + COLUMN_NO_HP + " = ? AND " + COLUMN_PASSWORD + " = ?";

        // Define the selection arguments
        String[] selectionArgs = {nomorHp, password};

        return db.rawQuery(query, selectionArgs);

    }

    public int updateFamilyforUser(String id, String role, String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAMILY_ROLE, role);
        values.put(COLUMN_IN_FAMILY, familyId);
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long addCatatan(String walletId, String tipe, BigDecimal amount, String deskripsi, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATATAN_USER_ID, walletId);
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


    public Cursor getCatatanByMonthAndYear(String month, String year, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATATAN + " WHERE SUBSTR(" + COLUMN_CATATAN_DATE + ", 6, 2) = ? AND SUBSTR(" + COLUMN_CATATAN_DATE + ", 1, 4) = ? AND " + COLUMN_CATATAN_USER_ID + " = ?";
        String[] selectionArgs = {month, year, userId};
        return db.rawQuery(query, selectionArgs);
    }

    public Cursor getCatatanKeluargaUserAndKeluargaByMonthAndYear(String month, String year, String familyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT u." + COLUMN_USER_NAME + ", u." + COLUMN_FAMILY_ROLE + ", k." + COLUMN_NAMA_KELUARGA + ", k." + COLUMN_KODE_KELUARGA + ", " +
                "ck." + COLUMN_CATATAN_KELUARGA_ID + ", ck." + COLUMN_CATATAN_KELUARGA_AMOUNT + ", ck." + COLUMN_CATATAN_KELUARGA_TIPE + ", " +
                "ck." + COLUMN_CATATAN_KELUARGA_DESKRIPSI + ", ck." + COLUMN_CATATAN_KELUARGA_DATE +
                " FROM " + TABLE_CATATAN_KELUARGA + " AS ck " +
                "INNER JOIN " + TABLE_KELUARGA + " AS u ON ck." + COLUMN_CATATAN_KELUARGA_KEL_ID + " = u." + COLUMN_KELUARGA_ID + " " +
                "INNER JOIN " + TABLE_USER + " AS k ON ck." + COLUMN_IN_FAMILY + " = k." + COLUMN_KODE_KELUARGA + " " +
                "WHERE SUBSTR(ck." + COLUMN_CATATAN_KELUARGA_DATE + ", 6, 2) = ? " +
                "AND SUBSTR(ck." + COLUMN_CATATAN_KELUARGA_DATE + ", 1, 4) = ? " +
                "AND k." + COLUMN_KODE_KELUARGA + " = ?";

        String[] selectionArgs = {month, year, familyId};
        return db.rawQuery(query, selectionArgs);
    }
    public Cursor getCatatanKeluargaByMonthAndYearById(String month, String year, String userId, String famylyId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CATATAN_KELUARGA + " WHERE SUBSTR(" + COLUMN_CATATAN_DATE + ", 6, 2) = ? AND SUBSTR(" + COLUMN_CATATAN_DATE + ", 1, 4) = ? AND " + COLUMN_CATATAN_USER_ID + " = ?";
        String[] selectionArgs = {month, year, userId, famylyId};
        return db.rawQuery(query, selectionArgs);
    }

    public long addKeluarga(String userId, String namaKeluarga, String kodeKeluarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KELUARGA_USER_ID, userId);
        values.put(COLUMN_NAMA_KELUARGA, namaKeluarga);
        values.put(COLUMN_KODE_KELUARGA, kodeKeluarga);
        return db.insert(TABLE_KELUARGA, null, values);
    }

    public int updateUserFamily(String id, String inFamily, String roleKeluarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IN_FAMILY, inFamily);
        values.put(COLUMN_FAMILY_ROLE, roleKeluarga);
        return db.update(TABLE_USER, values, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(id)});
    }
    public Cursor getAllKeluarga() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_KELUARGA, null, null, null, null, null, null);
    }

    public void updateKeluarga(int keluargaId, String userId, String namaKeluarga, int kodeKeluarga) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_KELUARGA_USER_ID, userId);
        values.put(COLUMN_NAMA_KELUARGA, namaKeluarga);
        values.put(COLUMN_KODE_KELUARGA, kodeKeluarga);
        db.update(TABLE_KELUARGA, values, COLUMN_KELUARGA_ID + " = ?", new String[]{String.valueOf(keluargaId)});
        db.close();
    }

    public void deleteKeluarga(int keluargaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_KELUARGA, COLUMN_KELUARGA_ID + " = ?", new String[]{String.valueOf(keluargaId)});
        db.close();
    }

    // Create a new catatan keluarga
    public long addCatatanKeluarga(String familyId, String userId, String total, String tipe, String deskripsi, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATATAN_KELUARGA_KEL_ID, familyId);
        values.put(COLUMN_CATATAN_KELUARGA_USER_ID, userId);
        values.put(COLUMN_CATATAN_KELUARGA_AMOUNT, total);
        values.put(COLUMN_CATATAN_KELUARGA_TIPE, tipe);
        values.put(COLUMN_CATATAN_KELUARGA_DESKRIPSI, deskripsi);
        values.put(COLUMN_CATATAN_KELUARGA_DATE, date);
        return db.insert(TABLE_CATATAN_KELUARGA, null, values);
    }

}
