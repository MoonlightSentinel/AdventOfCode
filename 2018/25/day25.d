module day25;

import std.algorithm;

void main() {

	version(unittest) {} else {
		import std.stdio : File, writefln;

		const input = File("day25.in", "r").byLine.parse;
		
		writefln!"1: %s"(solve(input));
	}
}

struct Point {
	int[4] coordinates;
	alias coordinates this;

	this(scope const int[4] coordinates) {
		this.coordinates = coordinates;
	}

	this(scope const(char)[] line) {
		import std.format : formattedRead;

		line.formattedRead!`%(%d,%)`(coordinates);
	}

	int distanceTo(scope ref const Point other) const {
		import std.math : abs;

		int res = 0;

		static foreach(i; 0 .. coordinates.length) {
			res += abs(this[i] - other[i]); 
		}

		return res;
	}

	unittest {
		immutable Point[] points = [
			Point([0,0,0,0]),
			Point([3,0,0,0]),
			Point([0,3,0,0]),
			Point([0,0,3,0]),
			Point([0,0,0,3]),
			Point([0,0,0,6]),
			Point([9,0,0,0]),
			Point([12,0,0,0])
		];

		foreach(const ref other; points[1 .. 5]) {
			assertEquals(points[0].distanceTo(other), 3);
		}

		assertEquals(points[4].distanceTo(points[5]), 3);
		assertEquals(points[5].distanceTo(points[6]), 15);
		assertEquals(points[2].distanceTo(points[7]), 15);
	}

	void toString(W)(ref W w) const {
		import std.format : formattedWrite;

		w.formattedWrite!`(%(%2d, %))`(this.coordinates);
	}
}

auto parse(R)(R range) {
	import std.array : array;

	static if(is(R == E[], E)) import std.range.primitives : empty, front, popFront;

	return range.map!(l => Point(l)).array;
}

auto solve(const Point[] points) {
	debug import std.stdio;

	Point[][] constellations;

	foreach(const i, const ref point; points) {
		debug writeln(point);

		size_t[] matches;

		foreach(const id, const cons; constellations) {
			foreach(const ref other; cons) {
				const dist = point.distanceTo(other);

				debug writef!" | -> %s = %d\n"(other, dist);

				if(dist <= 3) {
					matches ~= id;
					break;
				}
			}
		}

		// No match => new constellation
		if(matches.length == 0) {
			constellations ~= [point];
		}
		else {
			// At least one match => append to existing constellation
			constellations[matches[0]] ~= point;

			// Multiple matches => join all constellations
			if(matches.length > 1) {

				debug writef!"\nJoining %(%d, %)!\n"(matches);

				// Append other constellations
				constellations.joinAll(matches);
			}
		}

		debug {
			write("\n");

			foreach(const id, ref cons; constellations) {
				writef!"%3d: %(%s, %)\n"(id, cons);
			}

			writeln("\n\n");
		}
	}

	return constellations.length;
}

void joinAll(T)(ref T[][] constellations, const size_t[] matches) {
	auto first = &(constellations[matches[0]]);

	foreach(const id; matches[1 .. $]) {
		*first ~= constellations[id];
	}

	// Remove the old ones
	size_t empty = matches[1],
		next = empty + 1;

	foreach(const id; matches[2 .. $]) {
		while(next < id) {
			constellations[empty] = constellations[next];
			empty++;
			next++;
		}
		next++;
	}

	while(next < constellations.length) {
		constellations[empty] = constellations[next];
		empty++;
		next++;
	}

	constellations.length -= matches.length - 1;
}

unittest {
	int[][] test = [
		[1],
		[],
		[2],
		[3],
		[5],
		[4]
	];

	joinAll(test, [2,3]);

	assertEquals(test, [
		[1],
		[],
		[2,3],
		[5],
		[4]
	]);

	joinAll(test, [1,2,4]);

	assertEquals(test, [
		[1],
		[2,3,4],
		[5]
	]);
}


unittest {
	debug scope(failure) {
		import std.stdio : stdout;

		stdout.flush();
	}
	const examples = [
		
		2: [
			"0,0,0,0",
			"3,0,0,0",
			"0,3,0,0",
			"0,0,3,0",
			"0,0,0,3",
			"0,0,0,6",
			"9,0,0,0",
			"12,0,0,0"
		],
		
		4: [
			"-1,2,2,0",
			"0,0,2,-2",
			"0,0,0,-2",
			"-1,2,0,0",
			"-2,-2,-2,2",
			"3,0,2,-1",
			"-1,3,2,2",
			"-1,0,-1,0",
			"0,2,1,-2",
			"3,0,0,0"
		],
		
		3: [
			"1,-1,0,1",
			"2,0,-1,0",
			"3,2,-1,0",
			"0,0,3,1",
			"0,0,-1,-1",
			"2,3,-2,0",
			"-2,2,0,0",
			"2,-2,0,-1",
			"1,-1,0,-1",
			"3,2,0,2"
		],
		
		8: [
			"1,-1,-1,-2",
			"-2,-2,0,1",
			"0,2,1,3",
			"-2,3,-2,1",
			"0,2,3,-2",
			"-1,-1,1,-2",
			"0,-2,-1,0",
			"-2,2,3,-1",
			"1,2,2,0",
			"-1,-2,0,-2"
		]
	];

	foreach(const expected, const input; examples) {
		assertEquals(input.parse().solve(), expected);
	}
}

version(unittest)
void assertEquals(A,B)(scope auto ref const A got, scope auto ref const B expected, const size_t line = __LINE__) {
	import std.format : format;

	assert(expected == got, format!"Line %d: Expected %s but got %s"(line, expected, got));
}