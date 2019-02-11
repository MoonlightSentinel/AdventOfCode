module day18;

import std.algorithm;

void main() {

	version(unittest) {} else {
		import std.stdio : File, writefln;

		const sol = File("day18.in", "r")
					.byLine
					.parse
					.solve();
		
		writefln!"1: %s\n2: %s"(sol.a, sol.b);
	}
}

auto parse(R)(R range) {
	import std.array : array;

	static if(is(R == E[], E)) import std.range.primitives : empty, front, popFront;

	static assert(is(typeof(range.front.dup)), "Invalid range type!");

	return cast(Acre[][]) range.map!"a.dup".array;
}

enum Acre : char {
	open = '.',
	trees = '|',
	lumberyard = '#'
}
auto solve(scope Acre[][] field) {
	struct Result {
		ulong a, b;
	}

	return Result(
		solveA(field.copy),
		solveB(field)
	);
}

Acre[][] copy(scope const Acre[][] field) {
	import std.array : array;

	return field.map!"a.dup".array;
}

auto solveA(scope Acre[][] field) {
	scope next = new Acre[][](field.length, field[0].length);

	foreach(_; 0..10) {
		makeStep(field, next);

		auto tmp = next;
		next = field;
		field = tmp;
	}

	return evaluate(field);
}

auto evaluate(scope const Acre[][] field) {
	ulong wood = 0, yards = 0;

	foreach(const row; field) {
		foreach(const acre; row) {
			final switch(acre) with(Acre) {
				case open:						break;
				case trees:			wood++;		break;
				case lumberyard:	yards++;	break;
			}
		}
	}

	return wood * yards;
}

unittest {
	assertEquals(solveA(example[0].copy), 1147);
}

auto solveB(scope Acre[][] field) {
	enum STEPS = 1000000000;

	immutable xLen = field.length, yLen = field[0].length;

	scope const(Acre[][])[] backlog;

	foreach(const step; 1..STEPS+1) {

		backlog ~= field;
		scope next = new Acre[][](xLen, yLen);

		makeStep(field, next);

		// Next state in the backlog?
		const idx = backlog.countUntil(next);

		// Circle detected
		if(idx != -1) {
			const loopLen = step - idx;
			const remSteps = STEPS - step;
			const target = remSteps % loopLen;

			return evaluate(backlog[idx + target]);
		}

		field = next;
	}

	// In case there is no loop
	return evaluate(field);
}

auto makeStep(scope const Acre[][] current, scope Acre[][] next) {

	foreach(const int x, const line; current) {
		foreach(const int y, const field; line) {
			

			final switch(field) with(Acre) {
				case open:
					next[x][y] = current.count(x,y, Acre.trees) >= 3
								? Acre.trees
								: field;
					break;

				case trees:
					next[x][y] = current.count(x,y, Acre.lumberyard) >= 3
						? Acre.lumberyard
						: field;
					break;

				case lumberyard:
					next[x][y] = (current.count(x,y, Acre.lumberyard) >= 1 && current.count(x,y, Acre.trees) >= 1) 
						? field
						: Acre.open;
					break;
			}
		}
	}
}

uint count(scope const Acre[][] field, const int xBase, const int yBase, const Acre which) {
	immutable	xLen = field.length,
				yLen = field[0].length;

	uint count = 0;

	foreach(const xOff; -1 .. 2) {
		const x = xBase + xOff;

		if(x >= 0 && x < xLen) {
			foreach(const yOff; -1 .. 2) {
			
				const y = yBase + yOff;

				if(y >= 0 
				&& y < field.length
				&& field[x][y] == which 
				&& (xOff != 0 || yOff != 0)
				) {
					count++;
				}
			}
		}
	}

	return count;
}

unittest {
	immutable	xLen = example[0].length, 
				yLen = example[0][0].length;

	foreach(const i, const field; example[0..$-1]) {
		scope tmp = new Acre[][](xLen, yLen);

		makeStep(field, tmp);

		if(tmp != example[i+1]) {
			import std.stdio;

			alias String = immutable(char[][]);
			
			writefln!"%d (Line %d) -> %d (Line %d), expected\n%(%s\n%)\n\nbut got\n\n%(%s\n%)\n"(i, 189 + (i * 12), i+1, 189 + (i+1 * 12), cast(String) example[i+1], cast(String) tmp);

			stdout.flush();

			assert(0, "Example failed!");
		}
	}
}

version(unittest) 
static example = cast(immutable Acre[][][]) [
	[ // Initial state:
		".#.#...|#.",
		".....#|##|",
		".|..|...#.",
		"..|#.....#",
		"#.#|||#|#|",
		"...#.||...",
		".|....|...",
		"||...#|.#|",
		"|.||||..|.",
		"...#.|..|."
	],
	[ // After 1 minute:
		".......##.",
		"......|###",
		".|..|...#.",
		"..|#||...#",
		"..##||.|#|",
		"...#||||..",
		"||...|||..",
		"|||||.||.|",
		"||||||||||",
		"....||..|."
	],
	[ // After 2 minutes:
		".......#..",
		"......|#..",
		".|.|||....",
		"..##|||..#",
		"..###|||#|",
		"...#|||||.",
		"|||||||||.",
		"||||||||||",
		"||||||||||",
		".|||||||||"
	],
	[ // After 3 minutes:
		".......#..",
		"....|||#..",
		".|.||||...",
		"..###|||.#",
		"...##|||#|",
		".||##|||||",
		"||||||||||",
		"||||||||||",
		"||||||||||",
		"||||||||||"
	],
	[ // After 4 minutes:
		".....|.#..",
		"...||||#..",
		".|.#||||..",
		"..###||||#",
		"...###||#|",
		"|||##|||||",
		"||||||||||",
		"||||||||||",
		"||||||||||",
		"||||||||||"
	],
	[ // After 5 minutes:
		"....|||#..",
		"...||||#..",
		".|.##||||.",
		"..####|||#",
		".|.###||#|",
		"|||###||||",
		"||||||||||",
		"||||||||||",
		"||||||||||",
		"||||||||||"
	], 
	[ // After 6 minutes:
		"...||||#..",
		"...||||#..",
		".|.###|||.",
		"..#.##|||#",
		"|||#.##|#|",
		"|||###||||",
		"||||#|||||",
		"||||||||||",
		"||||||||||",
		"||||||||||"
	], 
	[ // After 7 minutes:
		"...||||#..",
		"..||#|##..",
		".|.####||.",
		"||#..##||#",
		"||##.##|#|",
		"|||####|||",
		"|||###||||",
		"||||||||||",
		"||||||||||",
		"||||||||||"
	], 
	[ // After 8 minutes:
		"..||||##..",
		"..|#####..",
		"|||#####|.",
		"||#...##|#",
		"||##..###|",
		"||##.###||",
		"|||####|||",
		"||||#|||||",
		"||||||||||",
		"||||||||||"
	], 
	[ // After 9 minutes:
		"..||###...",
		".||#####..",
		"||##...##.",
		"||#....###",
		"|##....##|",
		"||##..###|",
		"||######||",
		"|||###||||",
		"||||||||||",
		"||||||||||"
	], 
	[ // After 10 minutes:
		".||##.....",
		"||###.....",
		"||##......",
		"|##.....##",
		"|##.....##",
		"|##....##|",
		"||##.####|",
		"||#####|||",
		"||||#|||||",
		"||||||||||"
	]
];

version(unittest)
void assertEquals(A,B)(scope auto ref const A got, scope auto ref const B expected, const size_t line = __LINE__) {
	import std.format : format;

	assert(expected == got, format!"Line %d: Expected %s but got %s"(line, expected, got));
}
