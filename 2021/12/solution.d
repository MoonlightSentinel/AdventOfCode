module y2021.d12.solution;

void main()
{
	import std.stdio;

	const string[][string] input = File("input.txt", "r")
									.byLineCopy
									.parse();

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

string[][string] parse(T)(scope T input)
{
	import std.algorithm;

	typeof(return) result;

	foreach (const line; input)
	{
		const parts = line.findSplit("-");
		assert(parts);
		const from = parts[0];
		const to = parts[2];
		result.require(from) ~= to;
		result.require(to) ~= from;
	}

	// Improved debugging
	// foreach (_, ref value; result)
	// {
	// 	value.sort();
	// }

	return result;
}

ulong[2] solve(const string[][string] caveSystem)
{
	import std.algorithm;
	import std.ascii;

	ulong[2] result;
	ubyte[string] visited;

	void visit(const string cur, bool once = true)
	{
		if (cur == "end")
		{
			if (once)
				result[0]++;
			result[1]++;
			return;
		}

		const isSmall = cur.all!isLower();

		typeof(&visited[cur]) ptr;
		if (isSmall)
		{
			ptr = &visited.require(cur);
			if (*ptr == 1)
			{
				if (!once)
					return;
				once = false;
			}
			else if (*ptr >= 2)
				return;

			(*ptr)++;
		}

		scope (exit) if (isSmall) (*ptr)--;

		foreach (const suc; caveSystem.get(cur, []))
		{
			if (suc != "start")
				visit(suc, once);
		}
	}

	visit("start");
	return result;
}

unittest
{
	static int test()
	{
		static immutable string[] example = [
			`start-A`,
			`start-b`,
			`A-c`,
			`A-b`,
			`b-d`,
			`A-end`,
			`b-end`,
		];

		const res = solve(parse(example));
		assert(res[0] == 10);
		assert(res[1] == 36);
		return 0;
	}

	test();
	// enum force = test();
}
