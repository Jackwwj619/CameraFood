package com.caloriesnap.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.caloriesnap.data.local.entity.FoodEntity;
import com.caloriesnap.data.local.entity.RecentFoodEntity;
import java.lang.Class;
import java.lang.Exception;
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
public final class RecentFoodDao_Impl implements RecentFoodDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RecentFoodEntity> __insertionAdapterOfRecentFoodEntity;

  public RecentFoodDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRecentFoodEntity = new EntityInsertionAdapter<RecentFoodEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `recent_foods` (`foodId`,`usedAt`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final RecentFoodEntity entity) {
        statement.bindLong(1, entity.getFoodId());
        statement.bindLong(2, entity.getUsedAt());
      }
    };
  }

  @Override
  public Object insert(final RecentFoodEntity recent,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRecentFoodEntity.insert(recent);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<FoodEntity>> getRecent() {
    final String _sql = "SELECT f.* FROM foods f INNER JOIN recent_foods r ON f.id = r.foodId ORDER BY r.usedAt DESC LIMIT 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"foods",
        "recent_foods"}, new Callable<List<FoodEntity>>() {
      @Override
      @NonNull
      public List<FoodEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfCalories = CursorUtil.getColumnIndexOrThrow(_cursor, "calories");
          final int _cursorIndexOfProtein = CursorUtil.getColumnIndexOrThrow(_cursor, "protein");
          final int _cursorIndexOfCarbs = CursorUtil.getColumnIndexOrThrow(_cursor, "carbs");
          final int _cursorIndexOfFat = CursorUtil.getColumnIndexOrThrow(_cursor, "fat");
          final int _cursorIndexOfIsCustom = CursorUtil.getColumnIndexOrThrow(_cursor, "isCustom");
          final int _cursorIndexOfIsFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "isFavorite");
          final List<FoodEntity> _result = new ArrayList<FoodEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final FoodEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final float _tmpCalories;
            _tmpCalories = _cursor.getFloat(_cursorIndexOfCalories);
            final float _tmpProtein;
            _tmpProtein = _cursor.getFloat(_cursorIndexOfProtein);
            final float _tmpCarbs;
            _tmpCarbs = _cursor.getFloat(_cursorIndexOfCarbs);
            final float _tmpFat;
            _tmpFat = _cursor.getFloat(_cursorIndexOfFat);
            final boolean _tmpIsCustom;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsCustom);
            _tmpIsCustom = _tmp != 0;
            final boolean _tmpIsFavorite;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsFavorite);
            _tmpIsFavorite = _tmp_1 != 0;
            _item = new FoodEntity(_tmpId,_tmpName,_tmpCalories,_tmpProtein,_tmpCarbs,_tmpFat,_tmpIsCustom,_tmpIsFavorite);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
