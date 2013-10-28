package ui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import core.Core;

public class Ui {
	public static void main(String[] args) {
		new Ui();
	}

	public Ui() {
		Core core = new Core(75, 75);

		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setText("Game Of Life");

		GridLayout gridLayout = new GridLayout();
		shell.setLayout(gridLayout);
		gridLayout.numColumns = 1;

		Cells cells = new Cells(shell, core, display);
		new Controls(shell, core, display, cells);

		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
