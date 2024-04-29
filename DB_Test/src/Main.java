import java.sql.Connection;
import java.util.Scanner;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// note: this is a 'modified' file for the bugFix branch!!!!!

public class Main {

	private static int id;

	public static void main(String[] args) {
		// Datenbankadresse und Anmeldedaten
		String url = "jdbc:mariadb://huber03.com:3306/recipes";
		String user = "Gast";
		String pass = "Gast_1001-2345!";

		id = 0;

		Connection con = null;

		try {
			// Verbindung aufbauen
			con = DriverManager.getConnection(url, user, pass);
			System.out.println("Verbindung erfolgreich hergestellt\n");
			System.out.println("+====================================================");
			System.out.println("| " + con.getCatalog());
			System.out.println("+====================================================\n");

		} catch (SQLException e) {
			System.out.println("Error (connect): " + e.getMessage());
		}

		// Rezepte abfragen
		printRecipes(con);
		System.out.println("\nRezepte abgefragt\n");

		// Rezept hinzufügen
		String input;
		System.out.println("Wollen Sie ein neues Rezept hinzufügen? (y/n)");
		Scanner scanner = new Scanner(System.in);
		do {
			input = scanner.nextLine();
			if (input.equals("y")) {
				insertRecipe(con, id);
				System.out.println("aktualisierte Rezepte: ");
				printRecipes(con);
			} else if (input.equals("n")) {
				System.out.println("\nProgramm beendet");
			} else {
				System.out.println("Ungültige Eingabe");
			}
		} while (!input.equals("y") && !input.equals("n"));
		scanner.close();

		// Verbindung schließen
		if (con != null) {
			try {
				con.close();
				System.out.println("\nVerbindung geschlossen");

			} catch (SQLException e) {
				System.out.println("Error (disconnect): " + e.getMessage());
			}
		}
	}

	private static void printRecipes(Connection con) {
		id = 0;

		try (PreparedStatement statement = con.prepareStatement("""
				SELECT title, quantity, ingredients, instructions
				FROM recipes_1""")) {

			ResultSet resultSet = statement.executeQuery();

			while (resultSet.next()) {
				id = resultSet.getRow();
				String title = resultSet.getString("title"); // by column index
				String quant = resultSet.getString("quantity"); // by column index
				String ingred = resultSet.getString("ingredients"); // by column index
				String instr = resultSet.getString("instructions"); // by column name

				System.out.println("+----------------------------------------------------");
				System.out.println("| ID:           " + id);
				System.out.println("| Title:        " + title);
				System.out.println("| Quantity:     " + quant);
				System.out.println("| Ingredients:  " + ingred);
				System.out.println("| Instruction:  " + instr);
			}
			System.out.println("+----------------------------------------------------");

		} catch (SQLException e) {
			System.out.println("Error (query): " + e.getMessage());
			// e.printStackTrace();
		}
	}

	private static void insertRecipe(Connection con, int id) {
		System.out.println("\nNeues Rezept hinzufügen");
		id += 1;
		String title;
		Integer quantity;
		String ingredients;
		String instructions;

		Scanner scanner = new Scanner(System.in);

		System.out.println("Titel: ");
		title = scanner.nextLine();
		System.out.println("Anzahl Personen/Portionen: ");
		quantity = Integer.valueOf(scanner.nextLine());
		System.out.println("Zutaten: ");
		ingredients = scanner.nextLine();
		System.out.println("Anleitung: ");
		instructions = scanner.nextLine();

		scanner.close();

		try (PreparedStatement statement = con.prepareStatement("""
				INSERT INTO recipes (id, title, quantity, ingredients, instructions)
				VALUES (?, ?, ?, ?, ?)""")) {

			statement.setInt(1, id);
			statement.setString(2, title);
			statement.setInt(3, quantity);
			statement.setString(4, ingredients);
			statement.setString(5, instructions);
			statement.executeUpdate();
			System.out.println("\nNeues Rezept hinzugefügt");

		} catch (SQLException e) {
			System.out.println("Error (insert): " + e.getMessage());
		}
	}
	
	public void thisIsANewMethod() { 
		int x = 1;
		int y = 2;
		int w = x*y;
		
	}
}
