module day17;

import std.algorithm;

void main() {
	version(unittest) {} else {
		import std.stdio, std.array : array;

		auto input = File("day17.in", "r").byLine.parse;
		
		with(solve(input)) {
			writefln!"1: %d\n2: %d"(wet, water);
		}
	}
}

enum Tile : short {
	sand,	//Allows flow
	clay,	// Blocks water
	water,	// Water
	flow
}

auto parse(R)(R range) {
	import std.range;
	import std.array : array;

	const input = range.map!((line) {
		import std.format : formattedRead;

		char fixed, range;
		int val, min, max;

		line.formattedRead!"%c=%d, %c=%d..%d"(fixed, val, range, min, max);

		if(fixed == 'x') {
			return cartesianProduct(only(val), iota(min, max + 1)).array;
		}
		else {
			return cartesianProduct(iota(min, max + 1), only(val)).array;
		}
	})
	.joiner
	.array;

	auto allX = input.map!"a[0]",
		allY = input.map!"a[1]";

	auto grid = Grid(
		allX.minElement,
		allX.maxElement,
		allY.minElement,
		allY.maxElement
	);

	foreach(const clayPos; input) {
		grid[clayPos[0], clayPos[1]] = Tile.clay;
	}

	return grid;
}

struct Grid {
	private {
		const uint xMin, xMax, yMin, yMax;
		Tile[][] grid;
	}

	this(const uint xMin, const uint xMax, const uint yMin, const uint yMax) {

		this.xMin = xMin - 1;	// Leave 1 space to the left and right
		this.xMax = xMax + 1;
		this.yMin = yMin;
		this.yMax = yMax;

		this.grid = new Tile[][](
			this.xMax - this.xMin + 1,
			this.yMax + 2
		);
	}

	bool isInfinite(const uint y) const {
		return y > yMax;
	}

	ref inout(Tile) opIndex(const uint x, const uint y) inout {
		return grid[x - xMin][y];
	}

	bool isBlocked(const uint x, const uint y) const {
		final switch(this[x,y]) with(Tile) {
			case water, clay: return true;
			case sand, flow: return false;
		}
	}

	auto countWater() const {
		struct Result { uint wet, water; }

		Result res;

		foreach(const line; grid) {
			foreach(const cell; line[yMin .. $-1]) {
				switch(cell) with(Tile) {
					case water:	res.water++;	goto case;
					case flow:	res.wet++;		break;
					default:					break;
				}
			}
		}

		return res;
	}

	debug void print() const {
		import std.stdio : write, writeln;

		foreach(const y; 0 .. grid[0].length - 1) {
			foreach(const x; 0 .. grid.length) {
				char c = void;

				final switch(grid[x][y]) with(Tile) {
					case clay: c = '#'; break;
					case sand: c = '.'; break;
					case water: c = '~'; break;
					case flow: c = '|'; break;
				}

				write(c);
			}
			write('\n');
		}

		version(unittest) writeln('\n');
		else write("\n\n\n\n\n\n\n\n\n\n\n\n\n");
	}
}

auto solve(Grid grid, const uint xStart = 500, const uint yStart = 0) {
	enum FlowResult : short {
		Blocked,
		Reservoir,
		Infinite
	}

	FlowResult flow(const uint x, const uint y) {

		if(grid.isInfinite(y)) {
			return FlowResult.Infinite;
		}
		
		grid[x,y] = Tile.flow;

		if(!grid.isBlocked(x, y + 1)) {
			return flow(x, y + 1);
		}

		FlowResult result = FlowResult.Blocked;

		uint[2] stop = [0, 0];

		Side:
		foreach(const i, const OFF; [-1, 1]) {
			uint nextX = x;

			while(true) {
				nextX += OFF;

				if(grid.isBlocked(nextX, y)) {
					nextX -= OFF;
					stop[i] = nextX;
					break;
				}
				
				grid[nextX, y] = Tile.flow;

				if(!grid.isBlocked(nextX, y + 1)) {
					const cur = flow(nextX, y + 1);

					if(cur > result) result = cur;

					break;
				}
			}
		}

		if(stop[0] && stop[1]) {	// No path availiable, build reservoir
			foreach(const cur; stop[0] .. stop[1] + 1) {
				grid[cur,y] = Tile.water;
			}

			result = FlowResult.Reservoir;
		}

		return result;
	}

	do {	// Repeat drops of water
		debug grid.print();

	} while(flow(xStart, yStart) != FlowResult.Infinite);

	debug grid.print();

	return grid.countWater();
}

unittest {
	static immutable inputStr = [
		"x=495, y=2..7",
		"y=7, x=495..501",
		"x=501, y=3..7",
		"x=498, y=2..4",
		"x=506, y=1..2",
		"x=498, y=10..13",
		"x=504, y=10..13",
		"y=13, x=498..504"
	];

	auto input = parse(inputStr);

	with(solve(input)) {
		assertEquals(wet, 57);
		assertEquals(water, 29);
	}
}

version(unittest)
void assertEquals(A,B)(scope auto ref const A a, scope auto ref const B b) {
	import std.format : format;

	assert(a == b, format!"Expected %s but got %s"(b, a));
}