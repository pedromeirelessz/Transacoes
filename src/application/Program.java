package application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import db.DB;
import db.DbException;

public class Program {

	public static void main(String[] args) {

		Connection conn = null;
		Statement st = null;

		// Transa��o � uma opera��o que mant�m a consist�ncia do banco de dados, ela
		// deve ter 4 propiedades:
		// Atomocidade, consistente, isolada, dur�vel.
		// Na transa��o ou acontece tudo ou acontece nada, para se manter a consist�ncia
		// no banco de dados.

		try {
			conn = DB.getConnection();
			st = conn.createStatement();

			// N�o � para confirmar as opera��es automaticamente, todas as opera��es por
			// padr�o ficaram pendentes por uma confirma��o explicita do programador
			conn.setAutoCommit(false);

			int rows = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 4000 WHERE DepartmentId = 2");

			// Confirmar a transa��o
			conn.commit();

			System.out.println("Rows: " + rows);
			System.out.println("Rows2: " + rows2);

		} catch (SQLException SQL) {
			try {
				// Desfaz o que foi feito no banco de dados
				conn.rollback();
				throw new DbException("Transaction rolled back! Caused by: " + SQL.getMessage());

			} catch (SQLException e) {
				throw new DbException("Error trying to rollback! Caused by: " + e.getMessage());
			}
		} finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
	}
}
