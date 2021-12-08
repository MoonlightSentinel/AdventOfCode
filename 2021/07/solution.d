module y2021.d07.solution;

void main()
{
	import std.stdio;

	const char[] input = File("input.txt", "r")
							.byLine
							.front;
	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ulong[2] solve(scope const(char)[] input)
{
	import std.array : array;
	import std.algorithm;
	import std.conv : to;
	import std.range : iota;

	const uint[] crabs = input.splitter(',')
						.map!(to!uint)
						.array;

	const minCrab = minElement(crabs);
	const maxCrab = maxElement(crabs);

	ulong[2] result = [ulong.max, ulong.max];

	foreach (const val; minCrab .. maxCrab + 1)
	{
		// A
		{
			ulong cost;
			foreach (crab; crabs)
				cost += crab < val ? val - crab : crab - val;

			if (cost < result[0])
				result[0] = cost;
		}
		// B
		{
			ulong cost;
			foreach (crab; crabs)
			{
				const diff = crab < val ? val - crab : crab - val;
				cost += iota(1, diff + 1).sum();
			}

			if (cost < result[1])
				result[1] = cost;
		}
	}

	return result;
}

unittest
{
	static int test()
	{
		immutable string example = "16,1,2,0,4,2,7,1,2,14";

		const res = solve(example);
		assert(res[0] == 37);
		assert(res[1] == 168);
		return 0;
	}

	test();
	enum force = test();
}
