package com.caloriesnap.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caloriesnap.data.local.entity.FoodRecordEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class FoodRecordDao_Impl implements FoodRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<FoodRecordEntity> __insertionAdapterOfFoodRecordEntity;

  private final EntityInsertionAdapter<FoodRecordEntity> __insertionAdapterOfFoodRecordEntity_1;

  private final EntityDeletionOrUpdateAdapter<FoodRecordEntity> __deletionAdapterOfFoodRecordEntity;

  private final EntityDeletionOrUpdateAdapter<FoodRecordEntity> __updateAdapterOfFoodRecordEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteById;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public FoodRecordDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfFoodRecordEntity = new EntityInsertionAdapter<FoodRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `food_records` (`id`,`date`,`mealType`,`foodName`,`weight`,`calories`,`protein`,`carbs`,`fat`,`imageUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getMealType());
        statement.bindString(4, entity.getFoodName());
        statement.bindDouble(5, entity.getWeight());
        statement.bindDouble(6, entity.getCalories());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getCarbs());
        statement.bindDouble(9, entity.getFat());
        if (entity.getImageUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getImageUri());
        }
      }
    };
    this.__insertionAdapterOfFoodRecordEntity_1 = new EntityInsertionAdapter<FoodRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `food_records` (`id`,`date`,`mealType`,`foodName`,`weight`,`calories`,`protein`,`carbs`,`fat`,`imageUri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getMealType());
        statement.bindString(4, entity.getFoodName());
        statement.bindDouble(5, entity.getWeight());
        statement.bindDouble(6, entity.getCalories());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getCarbs());
        statement.bindDouble(9, entity.getFat());
        if (entity.getImageUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getImageUri());
        }
      }
    };
    this.__deletionAdapterOfFoodRecordEntity = new EntityDeletionOrUpdateAdapter<FoodRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `food_records` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodRecordEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__updateAdapterOfFoodRecordEntity = new EntityDeletionOrUpdateAdapter<FoodRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `food_records` SET `id` = ?,`date` = ?,`mealType` = ?,`foodName` = ?,`weight` = ?,`calories` = ?,`protein` = ?,`carbs` = ?,`fat` = ?,`imageUri` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final FoodRecordEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getDate());
        statement.bindString(3, entity.getMealType());
        statement.bindString(4, entity.getFoodName());
        statement.bindDouble(5, entity.getWeight());
        statement.bindDouble(6, entity.getCalories());
        statement.bindDouble(7, entity.getProtein());
        statement.bindDouble(8, entity.getCarbs());
        statement.bindDouble(9, entity.getFat());
        if (entity.getImageUri() == null) {
          statement.bindNull(10);
        } else {
          statement.bindString(10, entity.getImageUri());
        }
        statement.bindLong(11, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM food_records WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM food_records";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final FoodRecordEntity record,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfFoodRecordEntity.insertAndReturnId(record);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertAll(final List<FoodRecordEntity> records,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfFoodRecordEntity_1.insert(records);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object delete(final FoodRecordEntity record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfFoodRecordEntity.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final FoodRecordEntity record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfFoodRecordEntity.handle(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteById(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteAll(final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteAll.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FoodRecordEntity>> getByDate(final String date) {
    final String _sql = "SELECT * FROM food_records WHERE date = ? ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_records"}, new Callable<List<FoodRecordEntity>>() {
      @Override
      @NonNull
      public List<FoodRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<FoodRecordEntity> _result = new ArrayList<FoodRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final float _tmpWeight;
            _tmpWeight = _cursor.getFloat(_cursorIndexOfWeight);
            final float _tmpCalories;
            _tmpCalories = _cursor.getFloat(_cursorIndexOfCalories);
            final float _tmpProtein;
            _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
            final float _tmpCarbs;
            _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
            final float _tmpFat;
            _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new FoodRecordEntity(_tmpId,_tmpDate,_tmpMealType,_tmpFoodName,_tmpWeight,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByDateSync(final String date,
      final Continuation<? super List<FoodRecordEntity>> $completion) {
    final String _sql = "SELECT * FROM food_records WHERE date = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, date);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FoodRecordEntity>>() {
      @Override
      @NonNull
      public List<FoodRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<FoodRecordEntity> _result = new ArrayList<FoodRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final float _tmpWeight;
            _tmpWeight = _cursor.getFloat(_cursorIndexOfWeight);
            final float _tmpCalories;
            _tmpCalories = _cursor.getFloat(_cursorIndexOfCalories);
            final float _tmpProtein;
            _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
            final float _tmpCarbs;
            _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
            final float _tmpFat;
            _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new FoodRecordEntity(_tmpId,_tmpDate,_tmpMealType,_tmpFoodName,_tmpWeight,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FoodRecordEntity>> getByDateRange(final String start, final String end) {
    final String _sql = "SELECT * FROM food_records WHERE date BETWEEN ? AND ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, start);
    _argIndex = 2;
    _statement.bindString(_argIndex, end);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"food_records"}, new Callable<List<FoodRecordEntity>>() {
      @Override
      @NonNull
      public List<FoodRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<FoodRecordEntity> _result = new ArrayList<FoodRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final float _tmpWeight;
            _tmpWeight = _cursor.getFloat(_cursorIndexOfWeight);
            final float _tmpCalories;
            _tmpCalories = _cursor.getFloat(_cursorIndexOfCalories);
            final float _tmpProtein;
            _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
            final float _tmpCarbs;
            _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
            final float _tmpFat;
            _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new FoodRecordEntity(_tmpId,_tmpDate,_tmpMealType,_tmpFoodName,_tmpWeight,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getAll(final Continuation<? super List<FoodRecordEntity>> $completion) {
    final String _sql = "SELECT * FROM food_records";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<FoodRecordEntity>>() {
      @Override
      @NonNull
      public List<FoodRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDate = CursorUtil.getColumnIndexOrThrow(_cursor, "date");
          final int _cursorIndexOfMealType = CursorUtil.getColumnIndexOrThrow(_cursor, "mealType");
          final int _cursorIndexOfFoodName = CursorUtil.getColumnIndexOrThrow(_cursor, "foodName");
          final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfImageUri = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUri");
          final List<FoodRecordEntity> _result = new ArrayList<FoodRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodRecordEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDate;
            _tmpDate = _cursor.getString(_cursorIndexOfDate);
            final String _tmpMealType;
            _tmpMealType = _cursor.getString(_cursorIndexOfMealType);
            final String _tmpFoodName;
            _tmpFoodName = _cursor.getString(_cursorIndexOfFoodName);
            final float _tmpWeight;
            _tmpWeight = _cursor.getFloat(_cursorIndexOfWeight);
            final float _tmpCalories;
            _tmpCalories = _cursor.getFloat(_cursorIndexOfCalories);
            final float _tmpProtein;
            _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
            final float _tmpCarbs;
            _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
            final float _tmpFat;
            _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
            final String _tmpImageUri;
            if (_cursor.isNull(_cursorIndexOfImageUri)) {
              _tmpImageUri = null;
            } else {
              _tmpImageUri = _cursor.getString(_cursorIndexOfImageUri);
            }
            _item = new FoodRecordEntity(_tmpId,_tmpDate,_tmpMealType,_tmpFoodName,_tmpWeight,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpImageUri);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
