import std.algorithm, std.stdio, std.math : abs;

void main() {
	version(unittest){} else {

		import std.stdio, std.file : readText;
		import std.array : array, byPair;

		const input = readText!(char[])("day5.in");

		writefln("1: %d", part1(input.dup));
		writefln("2: %d", part2(input));
	}
}

import std.ascii : toLower;

size_t part1(char[] input) {
	

	Collapsed:
	while(input.length > 0) {

		foreach(const i, const c; input[0 .. $ - 1]) {
			const d = input[i+1];

			if(c != d && c.toLower == d.toLower) {

				foreach(const j; i .. input.length - 2) {
					input[j] = input[j+2];
				}

				input.length -= 2;

				continue Collapsed;
			}
		}
		break;
	}

	return input.length;
}

size_t part2(const char[] input) {
	import std.range : iota;
	import std.array : array;
	import std.parallelism : taskPool;

	auto configs = iota('A', cast(char) ('Z' + 1))
		.map!((high) {
			const char low = high.toLower;
		
			return input
				.filter!(c => c != low && c != high)
				.map!"cast(char) a"
				.array;
		});

	return taskPool.map!part1(configs).minElement;
}
/*
import std.array : Appender;

Appender!char buf;

buf.reserve(input.length);

foreach(const char high; 'A' .. 'Z' + 1) {

const char low = high.toLower;

foreach(const c; input) {
if(c != low && c != high) {

}
}
}
*/
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}

unittest {
	void check(string input, const size_t expected) {
		import std.format : format;

		const res = part1(input.dup);
		assert(res == expected, format!"Expected %s but got %s!"(expected, res));
	}

	check("aA", 0);
	check("abBA", 0);
	check("abAB", 4);
	check("aabAAB", 6);
	check("dabAcCaCBAcCcaDA", 10);

	assertEquals(part2("dabAcCaCBAcCcaDA"), 4);
}
