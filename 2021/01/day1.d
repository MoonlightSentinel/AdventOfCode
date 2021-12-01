module aoc.y2021.d01.day1;

void main()
{
	import std.stdio;

	const int[2] res = File("day1.in", "r")
		.byLine
		.solve();

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

int[2] solve(T)(scope T range)
{
	import std.algorithm;
	import std.conv : to;

	int singleCount = -1;
	int windowCount = -3;

	int lastSingle, lastWindow;
	int[3] buffer;

	foreach (entry; range.map!(to!int))
	{
		// Part A
		if (entry > lastSingle)
			singleCount++;
		lastSingle = entry;

		// Part B
		buffer[0] = buffer[1];
		buffer[1] = buffer[2];
		buffer[2] = entry;

		const s = sum(buffer[]);
		if (s > lastWindow)
			windowCount++;
		lastWindow = s;
	}

	return [singleCount, windowCount];
}

unittest
{
	immutable string[] example = [
		"199",
		"200",
		"208",
		"210",
		"200",
		"207",
		"240",
		"269",
		"260",
		"263"
	];

	const res = solve(example);
	assert(res[0] == 7);
}

unittest
{
	immutable string[] example = [
		"607",
		"618",
		"618",
		"617",
		"647",
		"716",
		"769",
		"792",
	];

	const res = solve(example);
	assert(res[1] == 5);
}
