module aoc.y2021.d01.day1;

void main()
{
	import std.stdio;

	const int[2] res = File("day2.in", "r")
		.byLine
		.solve();

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

int[2] solve(T)(scope T range)
{
	import std.algorithm : findSplit;
	import std.conv : to;

	int horizontalA, depthA, horizontalB, depthB, aimB;

	foreach (entry; range)
	{
		const parts = entry.findSplit(" ");
		assert(parts);

		const char[] cmd = parts[0];
		const int value = parts[2].to!int();
		final switch (cmd)
		{
			case "forward":
				horizontalA += value;

				horizontalB += value;
				depthB += aimB * value;
				break;

			case "up":
				depthA -= value;

				aimB -= value;
				break;

			case "down":
				depthA += value;

				aimB += value;
				break;
		}
	}

	return [ horizontalA * depthA, horizontalB * depthB ];
}

unittest
{
	immutable string[] example = [
		"forward 5",
		"down 5",
		"forward 8",
		"up 3",
		"down 8",
		"forward 2",
	];

	const res = solve(example);
	assert(res[0] == 150);
	assert(res[1] == 900);
}
