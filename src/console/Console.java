package console;

import java.util.Scanner;

import core.Core;

public class Console {
	private Core m_core;

	public static void main(String[] argv) {
		Console console = new Console();
		console.run();
	}

	public Console() {
		m_core = new Core(10, 10);
	}

	public void run() {
		Scanner scanner = new Scanner(System.in);
		String line = "";

		boolean status = true;
		while (status) {
			System.out.printf(">");
			line = scanner.nextLine();

			String[] tokens = line.split("\\s+");
			if (tokens.length == 0)
				continue;

			if (tokens[0].equals("c") || tokens[0].equals("clear")) {
				status = doClear(tokens);

			} else if (tokens[0].equals("s") || tokens[0].equals("step")) {
				status = doStep(tokens);

			} else if (tokens[0].equals("e") || tokens[0].equals("exit")) {
				status = doExit(tokens);

			} else if (tokens[0].equals("d") || tokens[0].equals("display")) {
				status = doDisplay(tokens);

			} else if (tokens[0].equals("f") || tokens[0].equals("fix")) {
				status = doFix(tokens);

			} else if (tokens[0].equals("h") || tokens[0].equals("help")) {
				status = doHelp(tokens);

			} else {
				System.out.printf("Invalid command\n");
			}
		}
		scanner.close();
	}

	private boolean doClear(String[] args) {
		if (args.length != 1) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}
		m_core.clear();
		return true;
	}

	private boolean doStep(String[] args) {
		if (args.length != 1) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}
		m_core.step();
		return true;
	}

	private boolean doExit(String[] args) {
		if (args.length != 1) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}
		return false;
	}

	private boolean doDisplay(String[] args) {
		if (args.length != 1) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}

		try {
			for (int y = 0; y < m_core.getHeight(); ++y) {
				for (int x = 0; x < m_core.getWidth(); ++x) {
					System.out.printf("%d ", m_core.getCellValue(x, y));
				}
				System.out.printf("\n");
			}
		} catch (Exception e) {
			assert (false);
		}
		return true;
	}

	private boolean doFix(String[] args) {
		if (args.length != 4) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}
		try {
			int y = Integer.parseInt(args[1]);
			int x = Integer.parseInt(args[2]);
			int value = Integer.parseInt(args[3]);
			m_core.setCellValue(x, y, value);

		} catch (Exception e) {
			System.out.printf("Invalid arguments to command\n");
		}
		return true;
	}

	private boolean doHelp(String[] args) {
		if (args.length != 1) {
			System.out.printf("Invalid arguments to command\n");
			return true;
		}
		System.out.printf("Commands:\n");
		System.out.printf("    clear|c:\n");
		System.out.printf("    step|s:\n");
		System.out.printf("    exit|e:\n");
		System.out.printf("    display|d:\n");
		System.out.printf("    fix|f: <x> <y> <value>\n");
		System.out.printf("    help|h:\n");
		return true;
	}
}
