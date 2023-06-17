package tr.xyz.matrix;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SpaceInt implements MatrixInt {
	
	private final List<int[]> rows = new ArrayList<>();
	private final int[]       size;
	private final Randy       rand = new Randy();
	
	public SpaceInt() {
		
		size = new int[]{0, 0};
	}
	
	SpaceInt(int row, int col) {
		
		this(row, col, false);
	}
	
	SpaceInt(int row, int col, int min, int max) {
		
		for (int i = 0; i < row; i++)
		     rows.add(rand.randIntArray(col, min, max));
		
		this.size = new int[]{row, col};
	}
	
	SpaceInt(int row, int col, boolean random) {
		
		if (random) {
			for (int i = 0; i < row; i++)
			     rows.add(rand.randIntArray(col, 0, 100));
		}
		else {
			for (int i = 0; i < row; i++)
			     rows.add(new int[col]);
		}
		
		this.size = new int[]{row, col};
	}
	
	SpaceInt(int col, int @NotNull ... ints) {
		
		for (int i = 0; i < ints.length; i++) {
			
			int[] row = new int[col];
			//noinspection ManualArrayCopy
			for (int j = 0; j < col; j++)
			     row[j] = ints[i * col + j];
			
			rows.add(row);
		}
		
		size = new int[]{ints.length, col};
	}
	
	SpaceInt(int @NotNull [] array, int col) {
		
		for (int i = 0; i < array.length; i++) {
			
			int[] row = new int[col];
			
			//noinspection ManualArrayCopy
			for (int j = 0; j < col; j++) {
				
				row[j] = array[i * col + j];
			}
			
			rows.add(row);
		}
		
		size = new int[]{array.length / col, col};
	}
	
	SpaceInt(int @NotNull [][] matrix) {
		
		//noinspection ManualArrayToCollectionCopy
		for (int[] row : matrix)
			//noinspection UseBulkOperation
			rows.add(row);
		
		this.size = new int[]{matrix.length, matrix[0].length};
	}
	
	@Override
	public int getColSize() {
		
		return size[1];
	}
	
	@Override
	public int getRowSize() {
		
		return size[0];
	}
	
	@Override
	public int get(int row, int col) {
		
		return rows.get(row)[col];
	}
	
	@Override
	public MatrixInt set(int row, int col, int value) {
		
		rows.get(row)[col] = value;
		return this;
	}
	
	@Override
	public int[] getRow(int row) {
		
		return rows.get(row);
	}
	
	@Override
	public int[] getCol(int col) {
		
		int[] colArray = new int[size[0]];
		
		for (int row = 0; row < rows.size(); row++)
		     colArray[row] = rows.get(row)[col];
		
		return colArray;
	}
	
	@Override
	public MatrixInt setCol(int col, int @NotNull ... ints) {
		
		if (ints.length != size[0])
			throw new IllegalArgumentException("Size mismatch. Expected " + size[0] + ", got " + ints.length);
		
		for (int row = 0; row < rows.size(); row++)
		     rows.get(row)[col] = ints[row];
		
		return this;
	}
	
	@Override
	public MatrixInt setRow(int row, int @NotNull ... ints) {
		
		checkColSize(ints.length);
		rows.set(row, ints);
		return this;
	}
	
	@Override
	public MatrixInt addRow(int @NotNull ... ints) {
		
		checkColSize(ints.length);
		rows.add(ints);
		size[0] = size[0] + 1;
		size[1] = ints.length;
		return this;
	}
	
	@Override
	public MatrixInt insertRow(int row, int @NotNull ... ints) {
		
		checkColSize(ints.length);
		rows.add(row, ints);
		return this;
	}
	
	@Override
	public MatrixInt removeRow(int row) {
		
		rows.remove(row);
		return this;
	}
	
	@Override
	public MatrixInt plus(@NotNull MatrixInt matrix) {
		
		MatrixInt sum = MatrixInt.zero(size[0], size[1]);
		
		for (int row = 0; row < rows.size(); row++) {
			
			int[] row1 = getRow(row);
			int[] row2 = matrix.getRow(row);
			
			for (int col = 0; col < row1.length; col++)
			     sum.set(row, col, row1[col] + row2[col]);
		}
		
		return sum;
	}
	
	@Override
	public MatrixInt minus(@NotNull MatrixInt matrix) {
		
		MatrixInt result = MatrixInt.zero(size[0], size[1]);
		
		for (int row = 0; row < rows.size(); row++) {
			
			int[] row1 = getRow(row);
			int[] row2 = matrix.getRow(row);
			
			for (int col = 0; col < row1.length; col++)
			     result.set(row, col, row1[col] - row2[col]);
		}
		
		return result;
	}
	
	@Override
	public MatrixInt star(int scale) {
		
		MatrixInt result = MatrixInt.zero(size[0], size[1]);
		
		for (int row = 0; row < rows.size(); row++) {
			
			int[] row1 = getRow(row);
			
			for (int col = 0; col < row1.length; col++)
			     result.set(row, col, row1[col] * scale);
		}
		
		return result;
	}
	
	private void checkColSize(int cols) {
		
		if (size[1] != cols)
			throw new IllegalArgumentException("Size mismatch. Expected " + size[1] + ", got " + cols);
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		
		for (int[] row : rows)
			sb.append(Arrays.toString(row)).append("\n");
		
		return sb.toString().trim();
	}
	
	public static void main(String[] args) {
		
		var m  = MatrixInt.random(3, 4, 1, 10);
		var m1 = MatrixInt.random(3, 4, 1, 10);
		
		System.out.println("=============== m ===============");
		System.out.println(m);
		System.out.println("=============== col ===============");
		System.out.println(Arrays.toString(m.getCol(2)));
		System.out.println("=============== col ===============");
		System.out.println(m.setCol(2, 1, 2, 3));
		System.out.println("=============== m1 ===============");
		System.out.println(m1);
		System.out.println("=============== sum ===============");
		System.out.println(m.plus(m1));
	}
	
}
