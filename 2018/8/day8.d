import std.algorithm;

void main() {
	version(unittest){} else {

		import std.stdio;

		const input = File("day8.in", "r").byLine.front.parse;

		writefln("1: %s", part1(input));
		writefln("2: %s", part2(input));
	}
}

int[] parse(const(char)[] input) {
	import std.array : array;
	import std.conv : to;

	return input.splitter.map!(to!int).array;
}

auto part1(const(int)[] numbers) {

	int result = 0;

	void parseNode() {
		const childNodes = numbers.next(),
			metadataEntrys = numbers.next();

		foreach(const i; 0 .. childNodes) {
			parseNode();
		}

		result += numbers[0 .. metadataEntrys].sum;
		numbers = numbers[metadataEntrys .. $];
	}
	
	parseNode();

	return result;
}

auto part2(const(int)[] numbers) {

	auto parseNode() {
		const childNodes = numbers.next(),
			metadataEntrys = numbers.next();

		if(childNodes == 0) {
			const weight = numbers[0 .. metadataEntrys].sum;
			numbers = numbers[metadataEntrys .. $];
			return weight;
		} 
		else {
			auto weights = new int[childNodes];

			foreach(ref weight; weights) {
				weight = parseNode();
			}

			int weight = 0;

			foreach(const meta; numbers[0 .. metadataEntrys]) {

				if(1 <= meta && meta <= childNodes) {
					weight += weights[meta-1];
				}
			}

			numbers = numbers[metadataEntrys .. $];
			return weight;
		}
	}

	return parseNode();
}

auto next(ref const(int)[] range) {
	import std.range.primitives;

	scope(exit) range.popFront();
	return range.front;
}

unittest {
	import std.array : array;

	static immutable exampleStr = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

	const example = exampleStr.parse;

	assertEquals(part1(example), 138);
	assertEquals(part2(example), 66);
}
version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}