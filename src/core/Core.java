package core;

public class Core {
	private int m_width;
	private int m_height;
	private int m_cells[][];
	private int m_newCells[][];

	public Core(int width, int height) {
		m_width = width;
		m_height = height;
		m_cells = new int[width][height];
		m_newCells = new int[width][height];
		clear();
	}

	public int getHeight() {
		return m_height;
	}

	public int getWidth() {
		return m_width;
	}

	public int getCellValue(int x, int y) throws Exception {
		if (!checkCellIndicies(x, y))
			throw new Exception("Invalid cell indicies");

		return m_cells[x][y];
	}

	public void setCellValue(int x, int y, int value) throws Exception {
		if (!checkCellIndicies(x, y))
			throw new Exception("Invalid cell indicies");

		m_cells[x][y] = value;
	}

	public void step() {
		// Calculates the new cell values..
		for (int y = 0; y < m_height; ++y) {
			for (int x = 0; x < m_width; ++x) {
				if (isCellAlive(x, y)) {
					if (isCellSustainable(x, y)) {
						m_newCells[x][y] = m_cells[x][y] + 1;
					} else {
						m_newCells[x][y] = 0;
					}
				} else {
					if (isCellResurectable(x, y)) {
						m_newCells[x][y] = 1;
					} else {
						m_newCells[x][y] = 0;
					}
				}
			}
		}

		// Updates the cell values..
		for (int y = 0; y < m_height; ++y) {
			for (int x = 0; x < m_width; ++x) {
				m_cells[x][y] = m_newCells[x][y];
			}
		}
	}

	public void clear() {
		for (int y = 0; y < m_height; ++y) {
			for (int x = 0; x < m_width; ++x) {
				m_cells[x][y] = 0;
			}
		}
	}

	private boolean checkCellIndicies(int x, int y) {
		if ((x < 0) || (x >= m_width)) {
			return false;
		}
		if ((y < 0) || (y >= m_height)) {
			return false;
		}
		return true;
	}

	private boolean isCellAlive(int x, int y) {
		if (!checkCellIndicies(x, y))
			return false;

		return m_cells[x][y] > 0;
	}

	private boolean isCellSustainable(int x, int y) {
		int numLivingNeighbours = getNumLivingNeighbours(x, y);
		return ((numLivingNeighbours == 2) || (numLivingNeighbours == 3));
	}

	private boolean isCellResurectable(int x, int y) {
		int numLivingNeighbours = getNumLivingNeighbours(x, y);
		return (numLivingNeighbours == 2);
	}

	private int getNumLivingNeighbours(int x, int y) {
		int numLivingNeighbours = 0;

		if (isCellAlive(x - 1, y - 1))
			++numLivingNeighbours;
		if (isCellAlive(x, y - 1))
			++numLivingNeighbours;
		if (isCellAlive(x + 1, y - 1))
			++numLivingNeighbours;
		if (isCellAlive(x - 1, y))
			++numLivingNeighbours;
		if (isCellAlive(x + 1, y))
			++numLivingNeighbours;
		if (isCellAlive(x - 1, y + 1))
			++numLivingNeighbours;
		if (isCellAlive(x, y + 1))
			++numLivingNeighbours;
		if (isCellAlive(x + 1, y + 1))
			++numLivingNeighbours;

		return numLivingNeighbours;
	}
}
