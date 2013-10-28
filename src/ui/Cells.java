package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import core.Core;

public class Cells {
	private Core m_core;
	private Composite m_cells;
	private Display m_display;

	Cells(Shell shell, Core core, Display display) {
		m_core = core;
		m_display = display;
		createUpperPane(shell);
	}

	void refresh() {
		m_cells.redraw();
	}

	private void createUpperPane(Shell shell) {
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;

		m_cells = new Composite(shell, SWT.BORDER);
		m_cells.setLayoutData(gridData);

		m_cells.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent event) {
				refresh(event);
			}
		});

		m_cells.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent arg0) {
				int xIndex = getXIndexFromPixels(arg0.x);
				int yIndex = getYIndexFromPixels(arg0.y);

				try {
					m_core.setCellValue(xIndex, yIndex, 1);
				} catch (Exception e) {
					assert (false);
				}
				m_cells.redraw();
			}
		});
	}

	private void refresh(PaintEvent event) {
		Rectangle rect = m_cells.getClientArea();

		// Sets the background to white...
		event.gc.setBackground(m_display.getSystemColor(SWT.COLOR_WHITE));
		event.gc.fillRectangle(rect);

		// Fill in live cells..
		for (int yIndex = 0; yIndex < m_core.getHeight(); ++yIndex) {
			for (int xIndex = 0; xIndex < m_core.getWidth(); ++xIndex) {
				try {
					int cellValue = m_core.getCellValue(xIndex, yIndex);
					if (cellValue > 0) {
						int x1 = getPixelsFromXIndex(xIndex);
						int y1 = getPixelsFromYIndex(yIndex);
						int x2 = getPixelsFromXIndex(xIndex + 1);
						int y2 = getPixelsFromYIndex(yIndex + 1);

						setCellColor(event, cellValue);
						event.gc.fillRectangle(x1, y1, x2 - x1, y2 - y1);
					}
				} catch (Exception e) {
					assert (false);
				}
			}
		}

		// Draws the vertical cell separators...
		for (int i = 1; i < m_core.getWidth(); ++i) {
			int x = getPixelsFromXIndex(i);
			event.gc.drawLine(x, 0, x, rect.height);
		}

		// Draws the horizontal cell separators...
		for (int i = 1; i < m_core.getHeight(); ++i) {
			int y = getPixelsFromYIndex(i);
			event.gc.drawLine(0, y, rect.width, y);
		}
	}

	private double getXPixelsPerCell() {
		return (double) m_cells.getClientArea().width
				/ (double) m_core.getWidth();
	}

	private double getYPixelsPerCell() {
		return (double) m_cells.getClientArea().height
				/ (double) m_core.getHeight();
	}

	private int getXIndexFromPixels(int pixels) {
		return (int) ((double) pixels / getXPixelsPerCell());
	}

	private int getYIndexFromPixels(int pixels) {
		return (int) ((double) pixels / getYPixelsPerCell());
	}

	private int getPixelsFromXIndex(int xIndex) {
		return (int) ((double) xIndex * getXPixelsPerCell());
	}

	private int getPixelsFromYIndex(int yIndex) {
		return (int) ((double) yIndex * getYPixelsPerCell());
	}

	private void setCellColor(PaintEvent event, int value) {
		int greyShade = value * 50;
		if (greyShade > 250)
			greyShade = 250;

		Color grey = new Color(m_display, greyShade, greyShade, greyShade);
		event.gc.setBackground(grey);
		grey.dispose();
	}
}
