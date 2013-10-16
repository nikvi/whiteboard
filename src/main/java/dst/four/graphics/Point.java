package dst.four.graphics;

import java.awt.Color;
import java.io.Serializable;

public class Point implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int x, y;
	Color col;
	int tool;
	int boarder;
	String text;

	public Point(int x, int y, Color col, int tool, int boarder) {
		this.x = x;
		this.y = y;
		this.col = col;
		this.tool = tool;
		this.boarder = boarder;
	}
}