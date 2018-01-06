import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Day1 {

  public static void main(String[] args) throws Exception {
    try(Scanner in = new Scanner(new File("day1.in"))) {
      while(in.hasNextLine()) {
        String input = in.nextLine();
        System.out.println("\nInput: " +input);
        System.out.println("A: " + solve_A(input));
        System.out.println("B: " + solve_B(input));
      }
    }
  }

  private static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

  private static int solve_A(final String input) {
    int x = 0,
        y = 0,
        direction = NORTH;

    for(String action : input.split(", ")) {
        direction = (direction + (action.charAt(0) == 'R' ? 1 : 3)) % 4;
        int distance = Integer.parseInt(action.substring(1));

        switch(direction) {
          case NORTH: x += distance; break;
          case WEST: y += distance; break;
          case SOUTH: x -= distance; break;
          case EAST: y -= distance; break;
        }
    }

    return distance(x,y);
  }

  private static int solve_B(final String input) {
    ArrayList<int[]> positions = new ArrayList<>();
    int x = 0,
        y = 0,
        direction = NORTH;

    for(String action : input.split(", ")) {
      direction = (direction + (action.charAt(0) == 'R' ? 1 : 3)) % 4;
      int distance = Integer.parseInt(action.substring(1)),
        x_dir = 0,
        y_dir = 0;

      switch(direction) {
        case NORTH: x_dir = 1; break;
        case WEST: y_dir = 1; break;
        case SOUTH: x_dir = -1; break;
        case EAST: y_dir = -1; break;
      }

      for(int i = 0; i < distance; i++) {

        for(int[] old : positions) {
          if(old[0] == x && old[1] == y) {
            return distance(old[0], old[1]);
          }
        }
        positions.add(new int[]{x,y});

        x += x_dir;
        y += y_dir;
      }
    }

    return -1;
  }

  private static int distance(final int x, final int y) {
    return Math.abs(x) + Math.abs(y);
  }
}
