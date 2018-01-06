#include <string>
#include <iostream>
#include <fstream>

const std::string NUMPAD_A[] = {
  "     ",
  " 123 ",
  " 456 ",
  " 789 ",
  "     ",
},

NUMPAD_B[] = {
  "       ",
  "   1   ",
  "  234  ",
  " 56789 ",
  "  ABC  ",
  "   D   ",
  "       ",
};

void apply(int& x, int&y, const char dir, const auto grid) {
  int xnew = x, ynew = y;

  switch(dir) {
    case 'U': xnew--; break;
    case 'D': xnew++; break;
    case 'L': ynew--; break;
    case 'R': ynew++; break;
  }

  if(grid[xnew][ynew] != ' ') {
    x = xnew;
    y = ynew;
  }
}

void solve(const char* filename) {
  std::ifstream file(filename);

  std::string line, codeA, codeB;
  int xA = 2, yA = 2, xB = 3, yB = 1;

  while(file >> line) {
    for(const char dir : line) {
      apply(xA, yA, dir, NUMPAD_A);
      apply(xB, yB, dir, NUMPAD_B);
    }

    codeA += NUMPAD_A[xA][yA];
    codeB += NUMPAD_B[xB][yB];
  }

  std::cout << "A: " << codeA << "\nB: " << codeB << std::endl;
}

int main() {
  constexpr auto FILES = {"Example.in", "Task.in"};

  for(const auto file : FILES) {
    std::cout << file << std::endl;
    solve(file);
  }
}
