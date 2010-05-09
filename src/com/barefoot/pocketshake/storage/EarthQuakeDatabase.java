package com.barefoot.pocketshake.storage;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.barefoot.pocketshake.R;

public class EarthQuakeDatabase extends SQLiteOpenHelper {
	 
	private static final String DATABASE_NAME = "EarthQuakes";
	private static final int DATABASE_VERSION = 1;
	private final Context mContext;

	
	public EarthQuakeDatabase(Context context) {
		 super(context, DATABASE_NAME, null, DATABASE_VERSION);
	     this.mContext = context;
	}

	private void executeManySqlStatements(SQLiteDatabase db, String[] sqls) {
		for(String eachSql : sqls) {
			if(eachSql.trim().length() > 0)
				db.execSQL(eachSql);
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String[] createSqls = mContext.getString(R.string.create_db_sql).split("\n");
		db.beginTransaction();
		try {
			executeManySqlStatements(db, createSqls);
			db.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e("Error creating tables and debug data", e.toString());
		}
		finally {
			db.endTransaction();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
