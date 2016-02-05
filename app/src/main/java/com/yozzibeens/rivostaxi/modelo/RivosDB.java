package com.YozziBeens.rivostaxi.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by SavidSalazar
 */

public class RivosDB {
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static RivosDB instance;
    private static DaoMaster.DevOpenHelper mDatabaseHelper;
    private SQLiteDatabase mDatabase;
    private DaoSession daoSession;

    public static synchronized void initializeInstance() {
        if (instance == null) {
            instance = new RivosDB();
        }
    }

    public static synchronized RivosDB getInstance() {
        if (instance == null) {
            throw new IllegalStateException(RivosDB.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return instance;
    }

    public synchronized DaoSession getSession()
    {
        DaoMaster daoMaster = new DaoMaster(mDatabase);
        daoSession = daoMaster.newSession();
        return daoSession;
    }

    public synchronized DaoSession openDatabase(Context context) {
        if(mOpenCounter.incrementAndGet() == 1) {
            // Opening new database
            mDatabaseHelper = new DaoMaster.DevOpenHelper(context, "Rivos-db", null);
            mDatabase = mDatabaseHelper.getWritableDatabase();
            DaoMaster daoMaster = new DaoMaster(mDatabase);
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public synchronized void closeDatabase() {
        if(mOpenCounter.decrementAndGet() == 0) {
            // Closing database
            mDatabase.close();
            daoSession.clear();

        }
    }
}