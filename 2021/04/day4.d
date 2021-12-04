module aoc.y2021.d01.day1;

import std.algorithm;

void main()
{
	import std.stdio;

	const uint[2] res = File("day4.in", "r")
		.byLine
		.solve();

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

uint[2] solve(T)(scope T input)
{
	import std.conv;
	import std.range;
	import std.string;

	// A
	const int[] nums = input.front
		.splitter(',')
		.map!(to!int)
		.array;

	input.popFront();

	int[5][5][] fields;

	while (!input.empty)
	{
		input.popFront();

		int[5][5] field;
		foreach (ref row; field)
		{
			const(char)[] txt = input.front;

			foreach (ref cell; row)
			{
				txt = txt.stripLeft();
				cell = txt.parse!int();

			}

			input.popFront();
		}
		fields ~= field;
	}

	return solve(nums, fields);
}

uint[2] solve(const int[] nums, int[5][5][] fields)
{
	uint[2] result;
	scope bool[] won = new bool[](fields.length);

	foreach (const number; nums)
	{
		foreach (const fieldIdx, ref field; fields)
		{
			foreach (ref row; field)
			{
				foreach (ref cell; row)
					if (cell == number)
						cell = int.max;
			}

			if (!won[fieldIdx] && hasWon(field))
			{
				won[fieldIdx] = true;
				const sum = calculateResult(number, field);

				if (!result[0])
					result[0] = sum;
				else
					result[1] = sum;
			}
		}
	}

	return result;
}

bool hasWon(ref const int[5][5] field)
{
	foreach (const i; 0 .. 5)
	{
		if (field[i][].all!(i => i == int.max))
		{
			return true;
		}

		bool all = true;
		foreach (const j; 0..5)
		{
			if (field[j][i] != int.max)
			{
				all = false;
				break;
			}
		}
		if (all)
			return true;
	}
	return false;
}

uint calculateResult(const int number, ref const int[5][5] field)
{
	// dump(field);
	const int remSum = field[]
						.map!((ref r) => r[])
						.joiner
						.filter!(n => n != int.max)
						.sum();

	return number * remSum;
}

void dump(ref const int[5][5] field)
{
	import std.stdio;

	stderr.writeln("");
	foreach (ref row; field)
	{
		foreach (ref cell; row)
		{
			if (cell == int.max)
				stderr.write(" X");
			else
				stderr.writef!"%2s"(cell);
			stderr.write(' ');
		}
		stderr.writeln();
	}
	stderr.writeln("\n");
	stderr.flush();
}

unittest
{
	static int test()
	{
		immutable string[] example = [
			"7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1",
			"",
			"22 13 17 11  0",
			"8  2 23  4 24",
			"21  9 14 16  7",
			"6 10  3 18  5",
			"1 12 20 15 19",
			"",
			"3 15  0  2 22",
			"9 18 13 17  5",
			"19  8  7 25 23",
			"20 11 10 24  4",
			"14 21 16 12  6",
			"",
			"14 21 17 24  4",
			"10 16 15  9 19",
			"18  8 23 26 20",
			"22 11 13  6  5",
			"2  0 12  3  7",
		];

		const res = solve(example);
		assert(res[0] == 4512);
		assert(res[1] == 1924);
		return 0;
	}

	test();
	enum force = test();
}
