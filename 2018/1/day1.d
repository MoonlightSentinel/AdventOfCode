import std.algorithm;

void main() {
	import std.stdio, std.array, std.conv : to;
	
	const input = File("day1.in", "r").byLine.map!(to!int).array;
	
	writefln("1: %d", part1(input));
	writefln("2: %d", part2(input));
}

int part1(const int[] values) {
	return values.sum;
}

int part2(const int[] values) {
	import std.range : cycle;
	
	int[int] found = [0: 1]; // cumulativeFold discards seed
	
	return values
		.cycle
		.cumulativeFold!"a + b"
		.find!(num => (found[num]++) == 1)
		.front;
}

unittest {
	import std.format;

	void validate(alias func)(immutable int[int[]] values) {
		foreach(input, output; values) {
			const res = func(input);
			
			assert(res == output, format!"%s => %d instead of %d"(input, res, output));
		}
	}

	validate!part1([
		[+1, +1, +1]: 3,
		[+1, +1, -2]: 0,
		[-1, -2, -3]: -6
	]);

	validate!part2([
		[+1, -1]: 0,
		[+3, +3, +4, -2, -4]: 10,
		[-6, +3, +8, +5, -6]: 5,
		[+7, +7, -2, -7, -4]: 14
	]);
}