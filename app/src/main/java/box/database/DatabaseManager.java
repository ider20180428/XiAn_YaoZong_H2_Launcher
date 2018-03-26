package box.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("deprecation")
public class DatabaseManager {
	private static DBHelper helper;
	private static SQLiteDatabase db;
	private static DatabaseManager dbManager;
	private Context context;

	private DatabaseManager(Context context) {
		helper = DBHelper.getInstance(context);
		db = helper.getWritableDatabase();
		// db =
		// SQLiteDatabase.openOrCreateDatabase(this.getFilesDir().toString()
		// + DB_NAME, null);
		this.context = context;
	}

	public static DatabaseManager getInstance(Context context) {
		if (dbManager == null) {
			dbManager = new DatabaseManager(context);
		}
		return dbManager;
	}


	public void insert(String pkgName, String type) {

		if (!db.isDbLockedByCurrentThread() && !db.isDbLockedByOtherThreads()) {

			db.beginTransaction();

			List<String> list = findApplicationByType(type);
			if (!list.contains(pkgName)) {
				db.execSQL(
						"insert into application (pkgName,type)values(?,?)",
						new Object[] { pkgName, type });
				db.setTransactionSuccessful();
				db.endTransaction();
			}
		}
	}



	public void delete(String pkgName, String type) {
		if (!db.isDbLockedByCurrentThread() && !db.isDbLockedByOtherThreads()) {
			db.beginTransaction();
			db.delete("application", "pkgName=? and type=?",
					new String[] { pkgName, type });
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}

	public List<String> findApplicationByType(String type) {

		List<String> list = new ArrayList<String>();
		if (!db.isDbLockedByCurrentThread() && !db.isDbLockedByOtherThreads()) {
			Cursor cursor = db.rawQuery("select * from application where type=?", new String[] { type });
			while (cursor.moveToNext()) {
				String pkg = cursor.getString(1);
				list.add(pkg);
			}
		}
		return list;
	}

	public List<Map<String, String>> findPackagesByType(String type) {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		if (!db.isDbLockedByCurrentThread() && !db.isDbLockedByOtherThreads()) {
			Cursor cursor = db.rawQuery(
					"select * from application where type=?",
					new String[] { type });
			while (cursor.moveToNext()) {
				String pkg = cursor.getString(2);
				String label = cursor.getString(1);
				String tag = cursor.getString(3);
				Map<String, String> map = new HashMap<String, String>();
				map.put("label", label);
				map.put("pkg", pkg);
				map.put("selftag", tag);
				list.add(map);
			}
		}

		return list;
	}

}
