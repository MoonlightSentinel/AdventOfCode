module y2021.d20.solution;

version (unittest)
{
	void main() {}
}
else version (Benchmark)
{
	version(LDC)
	{
		pragma(inline, true):
		pragma(LDC_no_typeinfo):
		pragma(LDC_no_moduleinfo);
	}

	// "Benchmark" version ;-)
	extern(C) int main()
	{
		static immutable string sol = ()
		{
			import std.conv : text;
			import std.string : lineSplitter;
			immutable txt = import("input.txt");
			immutable input = parse(txt.lineSplitter);
			immutable sol = solve(input);
			return text("A: ", sol[0], "\nB: ", sol[1], "\0");
		}();

		import core.stdc.stdio;
		puts(sol.ptr);
		return 0;
	}
}
else
{
	void main()
	{
		import std.stdio;

		immutable input = cast(immutable) File("input.txt", "r")
								.byLineCopy
								.parse();

		const ulong[2] res = solve(input);

		writeln("A: ", res[0]);
		writeln("B: ", res[1]);
	}
}

struct Problem
{
	const(char)[] filter;
	const(char[])[] image;
}

Problem parse(T)(scope T input)
{
	import std.array : array;
	import std.range.primitives;

	Problem result;

	result.filter = input.front();
	input.popFront();
	input.popFront();
	result.image = input.array();

	return result;
}

ulong[2] solve(immutable Problem problem)
{
	ulong[2] result;
	const(char[])[] image = problem.image;

	foreach (const step; 0..50)
	{
		const xDim = image.length;
		const yDim = image[0].length;

		auto newImage = new char[][](xDim + 2, yDim + 2);

		foreach (const x, line; newImage)
		{
			foreach (const y, ref cell; line)
			{
				uint sum;
				foreach (const xOff; [-1, 0, 1])
				{
					foreach (const yOff; [-1, 0, 1])
					{
						sum <<= 1;
						const xSource = -1 + x + xOff;
						const ySource = -1 + y + yOff;

						if (xSource < xDim && ySource < yDim)
						{
							sum |= image[xSource][ySource] == '#';
						}
						else
						{
							// OOB pixels start as '.' but might toggle between
							// assert(problem.filter[$-1] == '.'); // Could deal with that but meh
							sum |= (step % 2) * (problem.filter[0] == '#');
						}
					}
				}
				cell = problem.filter[sum];
			}
		}
		image = newImage;

		if (step == 1 || step == 49)
		{
			foreach (const line; image)
				foreach (const cell; line)
					result[step == 49] += cell == '#';
		}
	}


	return result;
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`..#.#..#####.#.#.#.###.##.....###.##.#..###.####..#####..#....#..#..##..##` ~
			`#..######.###...####..#..#####..##..#.#####...##.#.#..#.##..#.#......#.###` ~
			`.######.###.####...#.##.##..#..#..#####.....#.#....###..#.##......#.....#.` ~
			`.#..#..##..#...##.######.####.####.#.#...#.......#..#.#.#...####.##.#.....` ~
			`.#..#...##.#.##..#...##.#.##..###.#......#.#.......#.#.#.####.###.##...#..` ~
			`...####.#..#..#.##.#....##..#.####....##...##..#...#......#.#.......#.....` ~
			`..##..####..#...#.#.#...##..#.#..###..#####........#..####......#..#`,
			``,
			`#..#.`,
			`#....`,
			`##..#`,
			`..#..`,
			`..###`,
		];

		const res = solve(parse(example));
		assert(res[0] == 35);
		assert(res[1] == 3351);
		return 0;
	}

	test();
	enum force = test();
}
