module y2021.d10.solution;

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
	import std.stdio;
	ulong[2] result;
	char[] buffer;
	buffer.reserve(input[0].length);

	ulong[] incompleteScores;

	Lines:
	foreach (const line; input)
	{
		scope (exit) buffer = buffer[0..0];

		foreach (const ch; line)
		{
			bool invalid;
			Switch:
			final switch (ch)
			{
				static foreach (const pair; ["()", "[]", "{}", "<>"])
				{
					case pair[0]:
						assumeSafeAppend(buffer) ~= pair[1];
						break Switch;
				}

				static foreach (const end, const score; [')': 3, ']': 57, '}': 1197, '>': 25_137])
				{
					case end:
						if (buffer.length && buffer[$ - 1] == ch)
						{
							buffer = buffer[0..$-1];
							break Switch;
						}
						else
						{

							result[0] += score;
							continue Lines;
						}
				}
			}
		}

		if (!buffer.length)
			continue;

		ulong res = 0;
		foreach_reverse(const ch; buffer)
		{
			Switch2:
			final switch (ch)
			{
				static foreach (const end, const score; [')': 1, ']': 2, '}': 3, '>': 4])
				{
					case end:
						res = 5 * res + score;
						break Switch2;
				}
			}
		}

		incompleteScores ~= res;
	}

	import std.algorithm;
	sort(incompleteScores);
	result[1] = incompleteScores[$ / 2];
	return result;
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`[({(<(())[]>[[{[]{<()<>>`,
			`[(()[<>])]({[<{<<[]>>(`,
			`{([(<{}[<>[]}>{[]{[(<()>`,
			`(((({<>}<{<{<>}{[]{[]{}`,
			`[[<[([]))<([[{}[[()]]]`,
			`[{[{({}]{}}([{[{{{}}([]`,
			`{<[[]]>}<{[{[{[]{()[[[]`,
			`[<(<(<(<{}))><([]([]()`,
			`<{([([[(<>()){}]>(<<{{`,
			`<{([{{}}[<[[[<>{}]]]>[]]`,
		];

		const res = solve(example);
		assert(res[0] == 26_397);
		assert(res[1] == 288_957);
		return 0;
	}

	test();
	// enum force = test();
}
