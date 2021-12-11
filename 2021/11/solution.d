module y2021.d10.solution;

void main()
{
	import std.stdio;

	ubyte[][] input = File("input.txt", "r")
						.byLine
						.parse();

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ubyte[][] parse(T)(scope T input)
{
	import std.algorithm;
	import std.array;

	return input.map!(line =>
						line.map!(c => cast(ubyte) (c - '0'))
						.array
				).array;
}

ulong[2] solve(scope ubyte[][] field)
{
	ulong[2] result;
	ulong flashes;

	void tryFlash(const size_t x, const size_t y, ref ubyte cell)
	{
		if (cell != 10)
			return;

		cell++;
		flashes++;

		foreach (const xOff; [-1, 0, 1])
		{
			foreach (const yOff; [-1, 0, 1])
			{
				if (xOff || yOff)
				{{
					const xNext = x + xOff;
					const yNext = y + yOff;

					if (
						0 <= xNext &&
						xNext < field.length &&
						0 <= yNext &&
						yNext < field[0].length &&
						field[xNext][yNext] < 10
					)
					{
						field[xNext][yNext]++;
						tryFlash(xNext, yNext, field[xNext][yNext]);
					}
				}}
			}
		}
	}

	for (ulong step = 1; step; step++)
	{
		foreach (row; field)
			++(row[]);

		foreach (const x, row; field)
		{
			foreach (const y, ref cell; row)
			{
				tryFlash(x, y, cell);
			}
		}

		long reset;
		foreach (row; field)
		{
			foreach (ref cell; row)
			{
				if (cell >= 10)
				{
					cell = 0;
					reset++;
				}
			}
		}

		if (reset == (field.length * field[0].length))
		{
			assert(step >= 100);
			result[1] = step;
			break;
		}

		if (step == 100)
			result[0] = flashes;
	}

	return result;
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`5483143223`,
			`2745854711`,
			`5264556173`,
			`6141336146`,
			`6357385478`,
			`4167524645`,
			`2176841721`,
			`6882881134`,
			`4846848554`,
			`5283751526`,
		];

		const res = solve(parse(example));
		assert(res[0] == 1_656);
		assert(res[1] == 195);
		return 0;
	}

	test();
	enum force = test();
}
