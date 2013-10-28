package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import core.Core;

public class Controls {
	private enum State {
		RUNNING, STOPPED,
	};

	private Core m_core;
	private Display m_display;
	private Cells m_cells;
	private State m_state;
	private int m_generationCount;
	private Text m_generations;
	private Scale m_speed;

	Controls(Shell shell, Core core, Display display, Cells cells) {
		m_core = core;
		m_display = display;
		m_cells = cells;
		m_state = State.STOPPED;
		m_generationCount = 0;
		createLowerPane(shell);
	}

	private void createLowerPane(Shell shell) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Composite composite = new Composite(shell, SWT.BORDER);
		composite.setLayoutData(gridData);

		GridLayout gridLayout = new GridLayout();
		composite.setLayout(gridLayout);
		gridLayout.numColumns = 2;

		createLowerLeftComposite(composite);
		createLowerRightComposite(composite);
	}

	private void createLowerLeftComposite(Composite composite) {
		GridData gridData = new GridData();

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(gridData);
		container.setLayout(new RowLayout());

		createButton(container, "Go", new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				executeGo();
			}
		});
		createButton(container, "Stop", new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				executeStop();
			}
		});
		createButton(container, "Clear", new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				executeClear();
			}
		});

		createGenerationsControl(container);
	}

	private void createLowerRightComposite(Composite composite) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		Composite container = new Composite(composite, SWT.NONE);
		container.setLayoutData(gridData);
		container.setLayout(new FillLayout());

		createSpeedControl(container);
	}

	private Button createButton(Composite composite, String name,
			SelectionListener listener) {
		Button button = new Button(composite, SWT.PUSH);
		RowData rowData = new RowData();
		rowData.width = 50;
		button.setLayoutData(rowData);
		button.setText(name);
		button.addSelectionListener(listener);
		return button;
	}

	private void createGenerationsControl(Composite composite) {
		m_generations = new Text(composite, SWT.BORDER);
		m_generations.setEditable(false);
		RowData rowData = new RowData();
		rowData.width = 50;
		m_generations.setLayoutData(rowData);
		updateGenerationCount();
	}

	private void createSpeedControl(Composite composite) {
		m_speed = new Scale(composite, SWT.NONE);
		m_speed.setMinimum(0);
		m_speed.setMaximum(1000);
		m_speed.setPageIncrement(10);
		m_speed.setSelection(250);
	}

	private void executeGo() {
		if (m_state != State.STOPPED)
			return;

		new Thread() {
			public void run() {
				m_state = Controls.State.RUNNING;
				while (m_state == Controls.State.RUNNING) {
					m_core.step();
					incrementGenerationCount();
					m_display.syncExec(new Runnable() {
						public void run() {
							m_cells.refresh();
							try {
								sleep(m_speed.getSelection());
								// sleep(500);
							} catch (InterruptedException e) {
							}
						}
					});
				}
			}
		}.start();
	}

	private void executeStop() {
		m_state = State.STOPPED;
	}

	private void executeClear() {
		if (m_state != State.STOPPED)
			return;

		m_core.clear();
		m_cells.refresh();
		clearGenerationCount();
	}

	private void incrementGenerationCount() {
		++m_generationCount;
		updateGenerationCount();
	}

	private void clearGenerationCount() {
		m_generationCount = 0;
		updateGenerationCount();
	}

	private void updateGenerationCount() {
		m_display.syncExec(new Runnable() {
			public void run() {
				m_generations.setText(String.format("%d", m_generationCount));
			}
		});
	}
}
