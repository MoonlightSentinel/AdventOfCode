module aoc.y2021.d01.day1;

void main()
{
	import std.array;
	import std.algorithm;
	import std.stdio;

	const input = File("day3.in", "r")
		.byLine
		.map!(l => cast(immutable char[12]) l[0 .. 12])
		.array;

	const uint[2] res = solve!12(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

uint[2] solve(size_t width)(scope immutable char[width][] range) pure nothrow @safe
{
	// A
	uint[width] counters;

	foreach (const idx, const entry; range)
	{
		// Sobs in integer overflow
		// counters[] += (cast(ubyte[]) entry)[0..width] - cast(ubyte) '0';

		static foreach (const bit; 0 .. width)
		{
			counters[bit] += entry[bit] - '0';
		}
	}

	// Determine most common value per bit
	const uint half = cast(uint)(range.length / 2);
	counters[] = counters[] / half; // counters € [0..lines)

	// Construct binary number from array
	uint gamma;
	foreach (const idx; 0 .. width)
	{
		gamma = (gamma << 1) | counters[idx];
	}

	// Epsilon is the complement limited to width bits
	const uint epsilon = ~gamma & ((1 << width) - 1);

	// B
	scope bool[] rejected = new bool[](range.length);
	const uint oxygen = remainingValue!width(range, rejected, true);
	rejected[] = false;
	const uint co2 = remainingValue!width(range, rejected, false);

	return [gamma * epsilon, oxygen * co2];
}

uint remainingValue(size_t width)(
	scope ref immutable char[width][] range,
	scope bool[] rejected,
	bool min
)
{
	char expected(const uint bit)
	{
		int count, ones;
		foreach (const idx, const entry; range)
		{
			const rej = !rejected[idx];
			count += rej;
			ones += (entry[bit] - '0') & rej;
		}

		const res = ((ones >= (count - ones)) == min);
		immutable char[2] choices =  ['0', '1'];
		return choices[res];
	}

	auto remaining = range.length;
	Outer:
	while (true)
	{
		foreach (const bit; 0 .. width)
		{
			const char exp = expected(bit);

			foreach (const idx, const entry; range)
			{
				const miss = entry[bit] != exp;
				remaining -= (!rejected[idx]) & miss;
				rejected[idx] |= miss;
			}

			if (remaining <= 1)
				break Outer;
		}
	}

	foreach (const idx, const rej; rejected)
	{
		if (!rej)
		{
			const val = range[idx];
			uint value;

			foreach (const bit; 0 .. width)
			{
				value = (value << 1) | (val[bit] == '1');
			}

			return value;
		}
	}

	assert(false);
}

unittest
{
	static int test()
	{
		immutable char[5][] example = [
			"00100",
			"11110",
			"10110",
			"10111",
			"10101",
			"01111",
			"00111",
			"11100",
			"10000",
			"11001",
			"00010",
			"01010",
		];

		const res = solve!5(example);
		assert(res[0] == 198);
		assert(res[1] == 230);
		return 0;
	}
	test();
	enum force = test();
}
