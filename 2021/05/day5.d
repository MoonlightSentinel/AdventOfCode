module y2021.d05.day5;

void main()
{
	import std.stdio;

	const size_t[2] res = File("day5.in", "r")
		.byLine
		.solve();

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

size_t[2] solve(T)(scope T input)
{
	import std.algorithm;
	import std.format : formattedRead;

	uint[uint[2]] fieldA, fieldB;

	foreach (const(char)[] line; input)
	{
		uint[2] start, end;
		line.formattedRead!"%u,%u -> %u,%u"(start[0], start[1], end[0], end[1]);

		const countA = start[0] == end[0] || start[1] == end[1];

		const uint[2] off = [
			start[0] < end[0] ? 1 : (start[0] > end[0] ? -1 : 0),
			start[1] < end[1] ? 1 : (start[1] > end[1] ? -1 : 0),
		];

		while (true)
		{
			if (countA)
				fieldA[start]++;
			fieldB[start]++;

			if (start == end)
				break;

			start[] += off[];
		}
	}

	const size_t a = fieldA.byValue.filter!(v => v > 1).count;
	const size_t b = fieldB.byValue.filter!(v => v > 1).count;
	return [ a, b ];
}

unittest
{
	static int test()
	{
		immutable string[] example = [
			"0,9 -> 5,9",
			"8,0 -> 0,8",
			"9,4 -> 3,4",
			"2,2 -> 2,1",
			"7,0 -> 7,4",
			"6,4 -> 2,0",
			"0,9 -> 2,9",
			"3,4 -> 1,4",
			"0,0 -> 8,8",
			"5,5 -> 8,2",
		];

		const res = solve(example);
		assert(res[0] == 5);
		assert(res[1] == 12);
		return 0;
	}

	test();
	// enum force = test();
}
