package com.YozziBeens.rivostaxi.modelo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.YozziBeens.rivostaxi.modelo.Lugar;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table LUGAR.
*/
public class LugarDao extends AbstractDao<Lugar, Long> {

    public static final String TABLENAME = "LUGAR";

    /**
     * Properties of entity Lugar.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property LugarID = new Property(0, Long.class, "lugarID", true, "LUGAR_ID");
        public final static Property Nombre = new Property(1, String.class, "nombre", false, "NOMBRE");
        public final static Property Latitud = new Property(2, double.class, "latitud", false, "LATITUD");
        public final static Property Longitud = new Property(3, double.class, "longitud", false, "LONGITUD");
        public final static Property Imagen = new Property(4, String.class, "imagen", false, "IMAGEN");
        public final static Property Direccion = new Property(5, String.class, "direccion", false, "DIRECCION");
    };


    public LugarDao(DaoConfig config) {
        super(config);
    }
    
    public LugarDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'LUGAR' (" + //
                "'LUGAR_ID' INTEGER PRIMARY KEY ," + // 0: lugarID
                "'NOMBRE' TEXT NOT NULL ," + // 1: nombre
                "'LATITUD' REAL NOT NULL ," + // 2: latitud
                "'LONGITUD' REAL NOT NULL ," + // 3: longitud
                "'IMAGEN' TEXT NOT NULL ," + // 4: imagen
                "'DIRECCION' TEXT NOT NULL );"); // 5: direccion
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'LUGAR'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Lugar entity) {
        stmt.clearBindings();
 
        Long lugarID = entity.getLugarID();
        if (lugarID != null) {
            stmt.bindLong(1, lugarID);
        }
        stmt.bindString(2, entity.getNombre());
        stmt.bindDouble(3, entity.getLatitud());
        stmt.bindDouble(4, entity.getLongitud());
        stmt.bindString(5, entity.getImagen());
        stmt.bindString(6, entity.getDireccion());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Lugar readEntity(Cursor cursor, int offset) {
        Lugar entity = new Lugar( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // lugarID
            cursor.getString(offset + 1), // nombre
            cursor.getDouble(offset + 2), // latitud
            cursor.getDouble(offset + 3), // longitud
            cursor.getString(offset + 4), // imagen
            cursor.getString(offset + 5) // direccion
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Lugar entity, int offset) {
        entity.setLugarID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNombre(cursor.getString(offset + 1));
        entity.setLatitud(cursor.getDouble(offset + 2));
        entity.setLongitud(cursor.getDouble(offset + 3));
        entity.setImagen(cursor.getString(offset + 4));
        entity.setDireccion(cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Lugar entity, long rowId) {
        entity.setLugarID(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Lugar entity) {
        if(entity != null) {
            return entity.getLugarID();
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