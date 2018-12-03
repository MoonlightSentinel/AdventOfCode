import std.algorithm, std.format : formattedRead;

struct Request {

	uint x, y, xSize, ySize;

	// #1 @ 808,550: 12x22
	this(scope const(char)[] line) {
		uint _r;

		line.formattedRead!`#%d @ %d,%d: %dx%d`(_r, x, y, xSize, ySize);
	}
}

pragma(msg, "Array size: ", (uint.sizeof * 1000 * 100) / 1024, " KB");

void main() {
	import std.stdio;
	import std.array : array;

	const auto input = File("day3.in", "r").byLine.map!(a => Request(a)).array;
	
	auto grid = new uint[][1000];
	foreach(ref row; grid) row.length = 1000;

	foreach(req; input) with(req) {

		foreach(row; grid[x .. x + xSize]) {
			foreach(ref cell; row[y .. y + ySize]) {
				cell++;
			}
		}
	}

	writeln(grid.joiner.filter!"a > 1".count);

	Outer:
	foreach(id, req; input) with(req) {

		foreach(row; grid[x .. x + xSize]) {
			foreach(cell; row[y .. y + ySize]) {
				if(cell != 1) continue Outer;
			}
		}

		writeln("ID: ", id + 1);
		break Outer;
	}
}