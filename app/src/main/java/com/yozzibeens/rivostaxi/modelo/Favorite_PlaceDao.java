package com.YozziBeens.rivostaxi.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.YozziBeens.rivostaxi.modelo.Favorite_Place;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table FAVORITE__PLACE.
*/
public class Favorite_PlaceDao extends AbstractDao<Favorite_Place, Long> {

    public static final String TABLENAME = "FAVORITE__PLACE";

    /**
     * Properties of entity Favorite_Place.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Place_Favorite_Id = new Property(1, String.class, "Place_Favorite_Id", false, "PLACE__FAVORITE__ID");
        public final static Property Name = new Property(2, String.class, "Name", false, "NAME");
        public final static Property Description = new Property(3, String.class, "Description", false, "DESCRIPTION");
        public final static Property Latitude = new Property(4, String.class, "Latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(5, String.class, "Longitude", false, "LONGITUDE");
    };


    public Favorite_PlaceDao(DaoConfig config) {
        super(config);
    }
    
    public Favorite_PlaceDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'FAVORITE__PLACE' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PLACE__FAVORITE__ID' TEXT," + // 1: Place_Favorite_Id
                "'NAME' TEXT," + // 2: Name
                "'DESCRIPTION' TEXT," + // 3: Description
                "'LATITUDE' TEXT," + // 4: Latitude
                "'LONGITUDE' TEXT);"); // 5: Longitude
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'FAVORITE__PLACE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Favorite_Place entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String Place_Favorite_Id = entity.getPlace_Favorite_Id();
        if (Place_Favorite_Id != null) {
            stmt.bindString(2, Place_Favorite_Id);
        }
 
        String Name = entity.getName();
        if (Name != null) {
            stmt.bindString(3, Name);
        }
 
        String Description = entity.getDescription();
        if (Description != null) {
            stmt.bindString(4, Description);
        }
 
        String Latitude = entity.getLatitude();
        if (Latitude != null) {
            stmt.bindString(5, Latitude);
        }
 
        String Longitude = entity.getLongitude();
        if (Longitude != null) {
            stmt.bindString(6, Longitude);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Favorite_Place readEntity(Cursor cursor, int offset) {
        Favorite_Place entity = new Favorite_Place( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // Place_Favorite_Id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // Name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // Description
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // Latitude
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // Longitude
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Favorite_Place entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPlace_Favorite_Id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDescription(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setLatitude(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setLongitude(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Favorite_Place entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Favorite_Place entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
