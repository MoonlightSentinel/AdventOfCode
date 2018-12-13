module day13;

import std.algorithm;

void main() {
	version(unittest) {} else {
		import std.stdio, std.array : array;

		const input = File("day13.in", "r").byLineCopy.array.parse;
		
		with(part1(input)) {
			writefln!"1: %d,%d"(x, y);
		}

		with(part2(input)) {
			writefln!"2: %d,%d"(x, y);
		}
	}
}

enum Tile : short {
	empty,		// ' '
	horizontal, // '|'
	vertical,	// '-'
	curveRight,	// '/',
	curveLeft,	// '\',
	intersection, // '+'
}

struct Config {
	Cart[] carts;
	Tile[][] grid;
}

Config parse(R)(R range) {
	import std.range.primitives;
	
	Config config;
	{
		const rows = range.save.walkLength,
			cols = range.front.length;

		config.grid = new Tile[][](rows, cols);
	}

	foreach(const x, const line; range) {

		Tile[] row = config.grid[x];

		foreach(const y, const cell; line) {
			Tile tile;
			Direction dir = cast(Direction) 0;

			final switch(cell) with(Tile) {

				case ' ':	tile = empty;		break;
				case '|':	tile = horizontal;	break;
				case '-':	tile = vertical;	break;
				case '/':	tile = curveRight;	break;
				case '\\':	tile = curveLeft;	break;
				case '+':	tile = intersection; break;

				case '<':	dir = Direction.left;	goto case '-';
				case '>':	dir = Direction.right;	goto case '-';
				case '^':	dir = Direction.up;		goto case '|';
				case 'v':	dir = Direction.down;	goto case '|';
			}

			row[y] = tile;

			if(dir) {
				config.carts ~= Cart(x, y, dir);
			}
		}
	}

	return config;
}

enum Direction {
	up 	= 1 << 0,
	left 	= 1 << 1,
	down 	= 1 << 2,
	right 	= 1 << 3
}

Direction leftOf(const Direction dir) {
	return dir == Direction.right ? Direction.up : dir << 1;
}

Direction rightOf(const Direction dir) {
	return dir == Direction.up ? Direction.right : dir >> 1;
}

struct Position {
	uint x,y;
}

struct Cart {
	Position pos;
	alias pos this;
	uint interCount;
	Direction dir;

	this(const int x, const int y, const Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.interCount = 0;
	}

	void move(const Tile[][] grid) {
		debug import std.stdio : writef;

		debug writef!"(%d, %d)[%s]"(x, y, dir);

		final switch(this.dir) with(Direction) {
			case up:	x--;	break;
			case down:	x++;	break;
			case left:	y--;	break;
			case right:	y++;	break;
		}

		assert(0 <= x && x < grid.length, "x-Position out of bounds!");
		assert(0 <= y && y < grid[x].length, "y-Position out of bounds!");

		switch(grid[x][y]) with(Tile) {

			case curveLeft: 
				final switch(dir) with(Direction) {
					case up, down:		dir = leftOf(dir);	break;
					case left, right:	dir = rightOf(dir);	break;
				}
				break;

			case curveRight:
				final switch(dir) with(Direction) {
					case up, down:		dir = rightOf(dir);	break;
					case left, right:	dir = leftOf(dir);	break;
				}
				break;


			case intersection:
				final switch(this.interCount) {
					case 0: this.dir = leftOf(dir);		break;
					case 1: /* this.dir = dir; */		break;
					case 2:	this.dir = rightOf(dir);	break;
					case 3: this.interCount = 0;		goto case 0;
				}
				this.interCount++;
				break;

			case vertical, horizontal: /* Ignore */ break;

			default: assert(0, "Unexpected tile!");
		}

	debug writef!" => (%d, %d)[%s]\n"(x, y, dir);
	}
}

auto part1(const Config config) {
	auto carts = config.carts.dup;
	debug print(config.grid, carts);

	while(true) {
		
		foreach(ref cart; carts) {
			cart.move(config.grid);

			foreach(const i, ref const a; carts) {
				foreach(ref const b; carts[0 .. i]) {
					if(a.x == b.x && a.y == b.y) {

						return Position(a.y, a.x);
					}
				}
			}
		}
	}
}

unittest {
	static immutable input = [
		`/->-\        `,
		`|   |  /----\`,
		`| /-+--+-\  |`,
		`| | |  | v  |`,
		`\-+-/  \-+--/`,
		`  \------/   `
	];

	const config = parse(input);

	with(part1(config)) {
		assertEquals(x, 7);
		assertEquals(y, 3);
	}
}

auto part2(const Config config) {
	auto carts = config.carts.dup;
	debug print(config.grid, carts);

	bool[] toRemove = new bool[carts.length];

	while(carts.length > 1) {
		carts.sort!"a.x < b.x || (a.x == b.x && a.y < b.y)";

		for(size_t idx = 0; idx < carts.length; idx++) {

			Cart* cur = &(carts[idx]);

			cur.move(config.grid);

			toRemove[] = false;
			uint remCounter = 0;

			foreach(const otherIdx, ref other; carts) if(idx != otherIdx) {
				if(cur.x == other.x && cur.y == other.y) {
					toRemove[otherIdx] = true;
					remCounter++;
				} 
			}

			if(remCounter) {
				// Current element gets removed as well
				remCounter++;
				toRemove[idx] = true;

				size_t copyIdx = 0;

				foreach(const otherIdx, const rem; toRemove) {
					if(rem == false) {
						carts[copyIdx++] = carts[otherIdx];
					}
				}

				
				foreach(const rem; toRemove[0..idx+1]) {
					if(rem) idx--;
				}

				carts.length -= remCounter;
				toRemove = toRemove[0 .. carts.length];
			}
		}

		debug print(config.grid, carts);
	}

	with(carts[0]) return Position(y, x);
}

unittest {
	static immutable input = [
		`/>-<\  `,
		`|   |  `,
		`| /<+-\`,
		`| | | v`,
		`\>+</ |`,
		`  |   ^`,
		`  \<->/`,
	];

	const config = parse(input);

	with(part2(config)) {
		assertEquals(x, 6);
		assertEquals(y, 4);
	}
}

debug void print(const Tile[][] grid, const Cart[] carts) {
	import std.stdio : write;

	immutable char[Tile] mapping = [
		Tile.vertical: '-',
		Tile.horizontal: '|',
		Tile.curveLeft: '\\',
		Tile.curveRight: '/',
		Tile.intersection: '+',
		Tile.empty: ' '
	];

	foreach(const x, const line; grid) {
Cell:	foreach(const y, const cell; line) {
			foreach(ref const cart; carts) {
				if(cart.x == x && cart.y == y) {

					with(Direction) {
						write([
							up: '^',
							down:'v',
							left: '<',
							right: '>'
						][cart.dir]);
					}

					continue Cell;
				}
			}

			write(mapping[cell]);
		}
		write('\n');
	}

	write('\n');
}

void assertEquals(A,B)(scope auto ref const A a, scope auto ref const B b) {
	import std.format : format;

	assert(a == b, format!"Expected %s but got %s"(b, a));
}