module y2021.d13.solution;

void main()
{
	import std.stdio;

	const input = File("input.txt", "r")
									.byLineCopy
									.parse();

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

alias Field = bool[Point];

struct Problem
{
	Field coordinates;
	const(Fold)[] folds;
}

struct Point
{
	uint x, y;
}

struct Fold
{
	char axis;
	uint index;
}

Problem parse(T)(scope T input)
{
	import std.algorithm;
	import std.conv : parse, to;
	import std.range;

	Problem result;
	bool empty;

	foreach (const(char)[] line; input)
	{
		if (!line.length)
		{
			empty = true;
		}
		else if (!empty)
		{
			const x = parse!uint(line);
			line = line[1..$];
			const y = to!uint(line);
			result.coordinates[Point(x,y)] = true;
		}
		else
		{
			const parts = line.splitter()
								.drop(2)
								.front
								.findSplit("=");
			assert(parts);

			Fold fold;
			fold.axis = parts[0][0];
			fold.index = parts[2].to!uint();
			result.folds ~= fold;
		}
	}

	return result;
}

ulong[2] solve(const Problem problem)
{
	import std.algorithm;

	ulong[2] result;
	Field current = cast(Field) problem.coordinates;



	foreach (const foldIdx, const ref fold; problem.folds)
	{
		version (unittest) print(current);

		current = fold.axis == 'x'
				? applyFold!"x"(current, fold.index)
				: applyFold!"y"(current, fold.index);

		if (foldIdx == 0)
		{
			result[0] = current.length;
		}
	}

	print(current);

	return result;
}

Field applyFold(string axis)(const Field current, const uint index)
{
	Field next;

	foreach (const ref point; current.byKey)
	{
		Point p = Point(point.x, point.y);
		const cur = __traits(getMember, point, axis);
		if (cur > index)
		{
			__traits(getMember, p, axis) = index - (cur - index);
		}

		next[p] = true;
	}

	return next;
}

void print(Field current)
{
	import std.algorithm;
	import std.stdio;

	const uint maxX = 1 + current.byKey.map!(c => c.x).maxElement();
	const uint maxY = 1 + current.byKey.map!(c => c.y).maxElement();

	stderr.writeln('\n');
	foreach (const y; 0..maxY)
	{
		foreach (const x; 0..maxX)
		{
			const found = Point(x, y) in current;
			stderr.write(found ? '#' : '.');
		}
		stderr.writeln();
	}
	stderr.writeln('\n');
	stderr.flush();
}


unittest
{
	static int test()
	{
		static immutable string[] example = [
			`6,10`,
			`0,14`,
			`9,10`,
			`0,3`,
			`10,4`,
			`4,11`,
			`6,0`,
			`6,12`,
			`4,1`,
			`0,13`,
			`10,12`,
			`3,4`,
			`3,0`,
			`8,4`,
			`1,10`,
			`2,14`,
			`8,10`,
			`9,0`,
			``,
			`fold along y=7`,
			`fold along x=5`,
		];

		const res = solve(parse(example));
		assert(res[0] == 17);
		assert(res[1] == 0);
		return 0;
	}

	test();
	// enum force = test();
}
