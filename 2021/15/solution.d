module y2021.d15.solution;

void main()
{
	import std.array;
	import std.stdio;

	const input = File("input.txt", "r")
						.byLineCopy
						.array();

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

ulong[2] solve(const string[] field)
{
	import std.container.dlist : DList;

	const xSize = field.length;
	const ySize = field[0].length;

	const maxX = 5 * xSize;
	const maxY = 5 * ySize;

	scope costs = new uint[][](maxX, maxY);

	DList!(size_t[2]) positions;
	size_t[2] current;
	positions.insertFront(current);

	while (!positions.empty)
	{
		current = positions.front;
		positions.removeFront();

		const x = current[0];
		const y = current[1];
		const curCost = costs[x][y];

		static immutable offsets = [[1,0], [0, 1], [-1, 0], [0,-1]];
		foreach (const pair; offsets)
		{
			const nextX = x + pair[0];
			const nextY = y + pair[1];

			if (nextX >= maxX || nextY >= maxY)
				continue;

			const fieldX = nextX % xSize;
			const xOff = nextX / xSize;

			const fieldY = nextY % ySize;
			const yOff = nextY / ySize;

			auto fieldCost = (field[fieldX][fieldY] - '0') + xOff + yOff;
			if (fieldCost > 9) fieldCost %= 9;
			const newCost = curCost + fieldCost;
			auto oldCost = &costs[nextX][nextY];

			if (*oldCost == 0 || newCost < *oldCost)
			{
				assert(newCost <= uint.max);
				*oldCost = cast(uint) newCost;
				positions ~= cast(size_t[2]) [nextX, nextY];
			}
		}
	}

	return [
		costs[xSize - 1][xSize - 1],
		costs[$-1][$-1]
	];
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`1163751742`,
			`1381373672`,
			`2136511328`,
			`3694931569`,
			`7463417111`,
			`1319128137`,
			`1359912421`,
			`3125421639`,
			`1293138521`,
			`2311944581`,
		];

		const res = solve(example);
		assert(res[0] == 40);
		assert(res[1] == 315);
		return 0;
	}

	test();
	// enum force = test();
}
