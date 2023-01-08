package com.tr.hsyn.registery;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Basic methods to use a SQLite database from everywhere
 */
public interface SimpleDatabase {
	
	/**
	 * @param table          the table to insert the row into
	 * @param nullColumnHack optional; may be {@code null}.
	 *                       SQL doesn't allow inserting a completely empty row without
	 *                       naming at least one column name.  If your provided {@code values} is
	 *                       empty, no column names are known and an empty row can't be inserted.
	 *                       If not set to null, the {@code nullColumnHack} parameter
	 *                       provides the name of nullable column name to explicitly insert a NULL into
	 *                       in the case where your {@code values} is empty.
	 * @param values         this map contains the initial column values for the
	 *                       row. The keys should be the column names and the values the
	 *                       column values
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	long insert(@NotNull String table, @Nullable String nullColumnHack, @NotNull Values values);
	
	/**
	 * Convenience method for updating rows in the database.
	 *
	 * @param table       the table to update in
	 * @param values      a map from column names to new column values. null is a
	 *                    valid value that will be translated to NULL.
	 * @param whereClause the optional WHERE clause to apply when updating.
	 *                    Passing null will update all rows.
	 * @param whereArgs   You may include ?s in the where clause, which
	 *                    will be replaced by the values from whereArgs. The values
	 *                    will be bound as Strings.
	 * @return the number of rows affected
	 */
	int update(@NotNull String table, @NotNull Values values, @NotNull String whereClause, @Nullable String[] whereArgs);
	
	/**
	 * Convenience method for deleting rows in the database.
	 *
	 * @param table       the table to delete from
	 * @param whereClause the optional WHERE clause to apply when deleting.
	 *                    Passing null will delete all rows.
	 * @param whereArgs   You may include ?s in the where clause, which
	 *                    will be replaced by the values from whereArgs. The values
	 *                    will be bound as Strings.
	 * @return the number of rows affected if a whereClause is passed in, 0
	 * 		otherwise. To remove all rows and get a count pass "1" as the
	 * 		whereClause.
	 */
	int delete(@NotNull String table, @NotNull String whereClause, @Nullable String[] whereArgs);
	
	/**
	 * Begins a transaction in EXCLUSIVE mode.
	 * <p>
	 * Transactions can be nested.
	 * When the outer transaction is ended all of
	 * the work done in that transaction and all of the nested transactions will be committed or
	 * rolled back. The changes will be rolled back if any transaction is ended without being
	 * marked as clean (by calling setTransactionSuccessful). Otherwise they will be committed.
	 * </p>
	 * <p>Here is the standard idiom for transactions:
	 *
	 * <pre>
	 *   db.beginTransaction();
	 *   try {
	 *     ...
	 *     db.setTransactionSuccessful();
	 *   } finally {
	 *     db.endTransaction();
	 *   }
	 * </pre>
	 */
	void beginTransaction();
	
	/**
	 * End a transaction. See beginTransaction for notes about how to use this and when transactions
	 * are committed and rolled back.
	 */
	void endTransaction();
	
	/**
	 * Marks the current transaction as successful. Do not do any more database work between
	 * calling this and calling endTransaction. Do as little non-database work as possible in that
	 * situation too. If any errors are encountered between this and endTransaction the transaction
	 * will still be committed.
	 *
	 * @throws IllegalStateException if the current thread is not in a transaction or the
	 *                               transaction is already marked as successful.
	 */
	void setTransactionSuccessful();
}
