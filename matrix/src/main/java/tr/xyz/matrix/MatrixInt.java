package tr.xyz.matrix;


import org.jetbrains.annotations.NotNull;


/**
 * Defines the matrix of integers.
 */
public interface MatrixInt {
	
	int getColSize();
	
	int getRowSize();
	
	/**
	 * Return the value of the specified location.
	 *
	 * @param row row index
	 * @param col column index
	 * @return the value
	 */
	int get(int row, int col);
	
	/**
	 * Set the value of the specified location.
	 *
	 * @param row   row index
	 * @param col   column index
	 * @param value the value
	 * @return this matrix
	 */
	MatrixInt set(int row, int col, int value);
	
	/**
	 * Returns the vector of the specified row.
	 *
	 * @param row the row index
	 * @return the vector
	 */
	int[] getRow(int row);
	
	/**
	 * Returns the vector of the specified column.
	 *
	 * @param col the column index
	 * @return the vector
	 */
	int[] getCol(int col);
	
	/**
	 * Set the vector of the specified row.
	 *
	 * @param col  the column index
	 * @param ints the vector
	 * @return this matrix
	 */
	MatrixInt setCol(int col, int @NotNull ... ints);
	
	/**
	 * Set the vector of the specified row.
	 *
	 * @param row  the row index
	 * @param ints the vector
	 * @return this matrix
	 */
	MatrixInt setRow(int row, int... ints);
	
	/**
	 * Adds a row vector at the end to the matrix.
	 *
	 * @param ints the vector to add
	 * @return this matrix
	 */
	MatrixInt addRow(int... ints);
	
	/**
	 * Inserts a row vector at the specified index.
	 *
	 * @param row  the row index
	 * @param ints the vector
	 * @return this matrix
	 */
	MatrixInt insertRow(int row, int... ints);
	
	/**
	 * Removes the row at the specified index.
	 *
	 * @param row the row index
	 * @return this matrix
	 */
	MatrixInt removeRow(int row);
	
	/**
	 * Returns the sum of the matrix.
	 *
	 * @param matrix the matrix to sum
	 * @return the sum of the matrix
	 */
	MatrixInt plus(@NotNull MatrixInt matrix);
	
	/**
	 * Returns the difference of the matrix.
	 *
	 * @param matrix the matrix to subtract
	 * @return the difference of the matrix
	 */
	MatrixInt minus(@NotNull MatrixInt matrix);
	
	/**
	 * Returns a new matrix with the product of the matrix
	 *
	 * @param scale the scale
	 * @return the product of the matrix
	 */
	MatrixInt star(int scale);
	
	/**
	 * Creates a zero matrix.
	 *
	 * @param rows the number of rows
	 * @param cols the number of columns
	 * @return the zero {@link MatrixInt}
	 */
	@NotNull
	static MatrixInt zero(int rows, int cols) {
		
		return new SpaceInt(rows, cols);
	}
	
	/**
	 * Creates a random matrix.
	 *
	 * @param rows the number of rows
	 * @param cols the number of columns
	 * @return the random matrix
	 */
	@NotNull
	static MatrixInt random(int rows, int cols) {
		
		return new SpaceInt(rows, cols, 0, 100);
	}
	
	/**
	 * Creates a random matrix.
	 *
	 * @param rows the number of rows
	 * @param cols the number of columns
	 * @param min  the minimum value
	 * @param max  the maximum value
	 * @return the random matrix
	 */
	@NotNull
	static MatrixInt random(int rows, int cols, int min, int max) {
		
		return new SpaceInt(rows, cols, min, max);
	}
	
	/**
	 * Creates a matrix.
	 * Given vector is divided given cols,
	 * and each vector is added as a row.
	 *
	 * @param cols the number of columns
	 * @param ints the values
	 * @return the matrix
	 */
	@NotNull
	static MatrixInt matrix(int cols, int @NotNull ... ints) {
		
		return new SpaceInt(cols, ints);
	}
	
	/**
	 * Creates empty matrix.
	 *
	 * @return the empty matrix
	 */
	@NotNull
	static MatrixInt matrix() {
		
		return new SpaceInt();
	}
	
}
	
