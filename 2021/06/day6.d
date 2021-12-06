module y2021.d06.day6;

void main()
{
	import std.stdio;

	const ulong[2] res = File("day6.in", "r")
		.byLine
		.front
		.solve();

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ulong[2] solve(scope const(char)[] input)
{
	import std.algorithm;
	import std.conv : to;

	typeof(return) result;
	ulong fishCount;
	ulong[9] fishes;

	foreach (entry; input.splitter(','))
	{
		const cd = to!uint(entry);
		fishes[cd]++;
		fishCount++;
	}

	foreach (const day; 1..257)
	{
		const newFish = fishes[0];
		fishCount += newFish;

		foreach (const i; 1..fishes.length)
			fishes[i - 1] = fishes[i];

		fishes[6] += newFish;
		fishes[8] = newFish;

		if (day == 80)
			result[0] = fishCount;
	}
	result[1] = fishCount;

	return result;
}

unittest
{
	static int test()
	{
		immutable string example = "3,4,3,1,2";

		const res = solve(example);
		assert(res[0] == 5_934);
		assert(res[1] == 26_984_457_539);
		return 0;
	}

	test();
	enum force = test();
}
