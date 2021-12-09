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

		// Transação é uma operação que mantém a consistência do banco de dados, ela
		// deve ter 4 propiedades:
		// Atomocidade, consistente, isolada, durável.
		// Na transação ou acontece tudo ou acontece nada, para se manter a consistência
		// no banco de dados.

		try {
			conn = DB.getConnection();
			st = conn.createStatement();

			// Não é para confirmar as operações automaticamente, todas as operações por
			// padrão ficaram pendentes por uma confirmação explicita do programador
			conn.setAutoCommit(false);

			int rows = st.executeUpdate("UPDATE seller SET BaseSalary = 2090 WHERE DepartmentId = 1");
			int rows2 = st.executeUpdate("UPDATE seller SET BaseSalary = 4000 WHERE DepartmentId = 2");

			// Confirmar a transação
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
