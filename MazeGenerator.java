import java.util.*;

public class MazeGenerator {

    static final char WALL = '#';
    static final char PATH = '.';
    static final char START = 'S';
    static final char END = 'E';

    static int rows, cols;
    static char[][] maze;
    static Random rand = new Random();

    static final int[][] DIRECTIONS = {
            {0, 2}, {2, 0}, {0, -2}, {-2, 0}
    };

    public static void main(String[] args) {
        rows = 7;
        cols = 7;

        if (rows % 2 == 0) rows++;
        if (cols % 2 == 0) cols++;

        generateMaze();
        maze[1][1] = START;
        maze[rows - 2][cols - 2] = END;

        printMaze();
    }

    static void generateMaze() {
        maze = new char[rows][cols];
        for (char[] row : maze)
            Arrays.fill(row, WALL);

        carvePath(1, 1);
    }

    static void carvePath(int r, int c) {
        maze[r][c] = PATH;

        List<int[]> dirs = new ArrayList<>(Arrays.asList(DIRECTIONS));
        Collections.shuffle(dirs);

        for (int[] dir : dirs) {
            int nr = r + dir[0], nc = c + dir[1];
            if (isInBounds(nr, nc) && maze[nr][nc] == WALL) {
                maze[r + dir[0]/2][c + dir[1]/2] = PATH;
                carvePath(nr, nc);
            }
        }
    }

    static boolean isInBounds(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
    }

    static void printMaze() {
        for (char[] row : maze) {
            for (char cell : row)
                System.out.print(cell);
            System.out.println();
        }
    }
}
