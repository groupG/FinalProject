package model;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Eigenes TableModel, welches erweitert wurde um CachedRowSets verwenden zu
 * koennen.
 *
 * @author borecki
 *
 */
public class OutputTableModel implements TableModel {

	private CachedRowSet row_set;
	private ResultSetMetaData meta_data;
	private int num_columns, num_rows;

	public ResultSet getResultSet() {
		return this.row_set;
	}

	/**
	 * Konstruktor fuer ein neues OutputTable-Model.
	 *
	 * @param cached_row_set
	 * @throws SQLException
	 */
	public OutputTableModel(CachedRowSet cached_row_set) throws SQLException {
		if (this.row_set != null) this.row_set.release();
		this.row_set = cached_row_set;
		this.meta_data = this.row_set.getMetaData();
		this.num_columns = meta_data.getColumnCount();

		this.row_set.beforeFirst();
		this.num_rows = 0;
		while (this.row_set.next()) {
			this.num_rows++;
		}
		this.row_set.beforeFirst();
	}

	/**
	 * Beendet das aktuelle Statement.
	 */
	public void close() {
		try {
			this.row_set.getStatement().close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected void finalize() {
		close();
	}

	@Override
	public int getRowCount() {
		return this.num_rows;
	}

	@Override
	public int getColumnCount() {
		return this.num_columns;
	}

	@Override
	public String getColumnName(int columnIndex) {
		try {
			return this.meta_data.getColumnLabel(columnIndex + 1);
		} catch (SQLException e) {
			return e.toString();
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return String.class;
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		try {
			this.row_set.absolute(rowIndex + 1);
			Object obj = this.row_set.getObject(columnIndex + 1);

			if (obj == null) {
				return null;
			} else {
				return obj.toString();
			}
		} catch (SQLException e) {
			return e.toString();
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
	}

	@Override
	public void addTableModelListener(TableModelListener l) {
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
	}
}

