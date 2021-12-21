module y2021.d21.solution;

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
								.byLine
								.parse();

		const ulong[2] res = solve(input);

		writeln("A: ", res[0]);
		writeln("B: ", res[1]);
	}
}
import std.stdio;

uint[2] parse(T)(scope T input)
{
	import std.conv : to;

	enum offset = "Player X starting position: ".length;

	uint[2] result;
	size_t idx;
	foreach (const line; input)
	{
		result[idx++] = line[offset..$].to!uint();
	}

	return result;
}

pure nothrow @safe
{

ulong[2] solve(const uint[2] start)
{
	uint[2] positions = start;
	ulong[2] result;
	uint[2] points;
	uint die = 1;
	uint rolls;

	Game:
	while (true)
	{
		foreach (const player, ref position; positions)
		{
			foreach (_; 0..3)
			{
				position = modAdd(position, die, 10);
				die = modAdd(die, 1, 100);
				rolls++;
			}

			points[player] += position;

			if (points[player] >= 1_000)
			{
				const looser = (player + 1) % 2;
				result[0] = points[looser] * ulong(rolls);
				break Game;
			}
		}
	}

	const wins = countWins(0, start, [0,0]);
	result[1] = wins[0] > wins[1] ? wins[0] : wins[1];

	return result;
}

uint modAdd(uint a, uint b, uint m)
{
	uint v = a + b;
	while (v > m)
	{
		v -= m;
	}
	return v;
}

ulong[2] countWins(const uint player, const uint[2] positions, const uint[2] points)
{
	import std.array;
	import std.algorithm;
	import std.range;

	// Lookup table: (sum of 3 throws + occurence of said sum)
	static immutable offsets =
		cartesianProduct(iota(1, 4), iota(1, 4), iota(1, 4))
			.map!(t => t[0] + t[1] + t[2])
			.array
			.sort
			.group
			.array;

	ulong[2] result;

	const nextPlayer = (player + 1) % 2;
	uint[2] nextPositions = positions;
	uint[2] nextPoints = points;

	foreach (ref entry; offsets)
	{
		const offset = entry[0];
		const occurence = entry[1];

		const nextPos = modAdd(positions[player], offset, 10);
		nextPositions[player] = nextPos;
		nextPoints[player] = points[player] + nextPos;

		if (nextPoints[player] >= 21)
		{
			result[player] += occurence;
			continue;
		}

		result[] += occurence * countWins(nextPlayer, nextPositions, nextPoints)[];
	}

	return result;
}
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`Player 1 starting position: 4`,
			`Player 2 starting position: 8`,
		];

		const res = solve(parse(example));
		assert(res[0] == 739_785);
		assert(res[1] == 444_356_092_776_315);
		return 0;
	}

	test();
	// enum force = test();
}
