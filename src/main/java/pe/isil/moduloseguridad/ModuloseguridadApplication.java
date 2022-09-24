package pe.isil.moduloseguridad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class ModuloseguridadApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(ModuloseguridadApplication.class, args);

		String jdbcUrl = "jdbc:mysql://localhost:3306/moduloseg2";

		// Cargar el driver
		Class.forName("com.mysql.cj.jdbc.Driver");

		// Crear la conexion
		Connection conexion = DriverManager.getConnection(jdbcUrl, "root", "root");

		// Ejecutar metodos
//		testStatement(conexion);
//		testPreparedStatement(conexion);
//		testPreparedStatementResult(conexion);
//		getAllUsers(conexion);
//		loginByUsernameAndPass(conexion, "PAS123456543", "123456");
		updatePassByUsername(conexion, "PAS123456543", "Boby");

		// Cerrar conexion
		conexion.close();
	}

	public static void testStatement(Connection connection) throws Exception {
		// Crear statement
		Statement statement = connection.createStatement();

		// Ejecutar sentencia
		int affectedRows = statement.executeUpdate("UPDATE USERS SET name = 'Jose' WHERE id = 1");
		System.out.println("Filas afectadas: " + affectedRows);

		ResultSet resultSet = statement.executeQuery("SELECT * FROM Users");

		while (resultSet.next()){
			System.out.println(resultSet.getString("id") + " " +
					resultSet.getString("name") + " " +
					resultSet.getString("lastname") + " " +
					resultSet.getString("username") + " " +
					resultSet.getString("pass") + " " +
					resultSet.getString("enable"));
		}
	}

	public static void testPreparedStatement(Connection connection) throws Exception {
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE USERS SET name = ? WHERE id = ?");
		preparedStatement.setString(1, "Eduardo");
		preparedStatement.setInt(2, 1);

		int affectedRows = preparedStatement.executeUpdate();

		System.out.println("Filas afectadas: " + affectedRows);
	}

	public static void testPreparedStatementResult(Connection connection) throws Exception {
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USERS WHERE username = ?");
		preparedStatement.setString(1, "DNI73267572");

		ResultSet resultSet = preparedStatement.executeQuery();

		while (resultSet.next()){
			System.out.println(resultSet.getString("name") + " " + resultSet.getString("lastname"));
		}
	}

	public static void getAllUsers(Connection connection) throws Exception {
		CallableStatement cs = connection.prepareCall("{ call getAllUsers() }");
		ResultSet rs = cs.executeQuery();

		while (rs.next()){
			System.out.println(rs.getString("name") + " " +
					rs.getString("lastname") + " " +
					rs.getString("username") + " " +
					rs.getString("pass") + " " +
					rs.getString("enable"));
		}
	}

	public static void loginByUsernameAndPass(Connection connection, String username, String pass) throws Exception {
		CallableStatement callableStatement = connection.prepareCall("{ call login(?, ?) }");
		callableStatement.setString(1, username);
		callableStatement.setString(2, pass);

		ResultSet resultSet = callableStatement.executeQuery();

		while (resultSet.next())  {
			System.out.println(resultSet.getString(1));
		}
	}

	public static void updatePassByUsername(Connection connection, String username, String pass) throws Exception {
		CallableStatement callableStatement = connection.prepareCall("{ call updatePassByUsername(?, ?, ?) }");
		callableStatement.setString(1, username);
		callableStatement.setString(2, pass);
		callableStatement.registerOutParameter(3, Types.INTEGER);

		callableStatement.execute();

		int affectedRows = callableStatement.getInt(3);

		System.out.println("Filas afectadas: " + affectedRows);
	}
}
