package box.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	private static String NAME = "app.db";
	private static DBHelper helper = null;

	private DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	private DBHelper(Context context) {
		super(context, NAME, null, 1);
	}
	
	public static DBHelper getInstance(Context context) {
		if(helper == null) {
			synchronized (DBHelper.class) {
				if(helper == null) {
					DBHelper tmp = new DBHelper(context);
					helper = tmp;
				}
			}
		}
		return helper;
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("BoxLauncher", "21121221");
		db.execSQL("create table if not exists city"
				+ "(_id Integer primary key autoincrement, name varchar(20))");
		
		db.execSQL("create table if not exists application"
				+ "(_id Integer primary key autoincrement, pkgName text, type var(10))");
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
