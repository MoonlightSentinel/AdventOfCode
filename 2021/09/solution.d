module y2021.d09.solution;

void main()
{
	import std.array;
	import std.stdio;

	const string[] input = File("input.txt", "r")
						.byLineCopy
						.array;

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ulong[2] solve(scope const char[][] input)
{
	ulong[2] result;
	size_t[3] basins;

	foreach (const x, const line; input)
	{
		foreach (const y, const num; line)
		{

			if (x && num >= input[x - 1][y])
				continue;

			if (y && num >= line[y - 1])
				continue;

			if (x + 1 < input.length && num >= input[x + 1][y])
				continue;

			if (y + 1 < line.length && num >= line[y + 1])
				continue;

			result[0] += 1 + (num - '0');

			scope visited = new bool[][](input.length, input[0].length);
			const size = basinSize(input, x, y, visited);

			if (size > basins[0])
			{
				import std.algorithm : sort;
				basins[0] = size;
				sort(basins[]);
			}
		}

		result[1] = basins[0] * basins[1] * basins[2];
	}

	return result;
}

size_t basinSize(
	scope const char[][] input,
	const size_t x,
	const size_t y,
	scope bool[][] visited,
)
{
	visited[x][y] = true;
	const num = input[x][y];
	size_t count = 1;

	static foreach (const off; [[-1, 0], [1, 0], [0, -1], [0, 1]])
	{{
		const nextX = x + off[0];
		const nextY = y + off[1];
		if (
			0 <= nextX && nextX < input.length &&
			0 <= nextY && nextY < input[x].length &&
			!visited[nextX][nextY]
		)
		{
			const next = input[nextX][nextY];
			if (next != '9' && next > num)
			{
				count += basinSize(input, nextX, nextY, visited);
			}
		}
	}}
	return count;
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`2199943210`,
			`3987894921`,
			`9856789892`,
			`8767896789`,
			`9899965678`,
		];

		const res = solve(example);
		assert(res[0] == 15);
		assert(res[1] == 1134);
		return 0;
	}

	test();
	// enum force = test();
}
