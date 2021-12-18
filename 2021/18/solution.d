module d2021.d18.solution;

version (unittest)
void main() {}

else
void main()
{
	import std.stdio;

	const Pair[] input = File("input.txt", "r")
							.byLine
							.parse();

	const ulong[2] res = solve(input);

	writeln("A: ", res[0]);
	writeln("B: ", res[1]);
}

Pair[] parse(T)(scope T input)
{
	Pair[] result;

	foreach (const(char)[] line; input)
	{
		result ~= new Pair(line);
	}

	return result;
}

final class Pair
{
	Entry left, right;
	Pair parent;

	this(scope const Pair other)
	{
		if (auto p = other.left.isPair())
		{
			Pair pc = new Pair(p);
			pc.parent = this;
			this.left = Entry(pc);
		}
		else
			this.left = Entry(other.left.value);

		if (auto p = other.right.isPair())
		{
			Pair pc = new Pair(p);
			pc.parent = this;
			this.right = Entry(pc);
		}
		else
			this.right = Entry(other.right.value);
	}

	this(scope const(char)[] entry) pure @safe
	{
		this(entry);
	}

	this(scope ref const(char)[] entry) pure @safe
	{
		import std.ascii : isDigit;
		import std.conv : parse;

		final void pop(const char expected)
		{
			assert(entry[0] == expected);
			entry = entry[1..$];
		}

		final Entry parseEntry()
		{
			if (isDigit(entry[0]))
			{
				const int num = parse!int(entry);
				return Entry(num);
			}
			else
			{
				Pair ptr = new Pair(entry);
				ptr.parent = this;
				return Entry(ptr);
			}
		}

		pop('[');
		this.left = parseEntry();
		pop(',');
		this.right = parseEntry();
		pop(']');
	}

	this(A, B)(A left, B right, Pair parent = null)
	{
		this.left = Entry(left);
		this.right = Entry(right);
		this.parent = parent;
	}

	override string toString() const pure nothrow @safe
	{
		string res;
		toString((ch) { res ~= ch; });
		return res;
	}

	void toString(scope void delegate(const char[]) pure nothrow @safe sink) const pure nothrow @safe
	{
		sink("[");
		left.toString(sink);
		sink(",");
		right.toString(sink);
		sink("]");
	}
}

struct Entry
{
	pure nothrow @safe
	{
	private
	{
		union
		{
			int value_;
			Pair successor_;
		}
		bool isValue_;
	}

	this(int value) @nogc
	{
		this.value = value;
	}

	this(inout Pair succ) inout @nogc
	{
		this.successor_ = succ;
		this.isValue_ = false;
	}

	bool isValue() const @nogc
	{
		return isValue_;
	}

	inout(Pair) isPair() inout @nogc @trusted
	{
		return isValue_ ? null : successor_;
	}

	int value() const @nogc
	in (isValue_)
	{
		return value_;
	}

	int value(int newVal) @nogc
	{
		this.value_ = newVal;
		this.isValue_ = true;
		return newVal;
	}

	inout(Pair) pair() inout @trusted @nogc
	in (!isValue_)
	{
		return successor_;
	}

	Pair pair(Pair newVal) @trusted @nogc
	{
		this.successor_ = newVal;
		this.isValue_ = false;
		return newVal;
	}

	string toString() const pure nothrow @safe
	{
		string res;
		toString((ch) { res ~= ch; });
		return res;
	}

	void toString(scope void delegate(const char[]) pure nothrow @safe sink) const @trusted
	{
		import core.internal.string : signedToTempString;
		if (isValue_)
		{
			sink(signedToTempString(value_));
		}
		else
		{
			assert(successor_);
			successor_.toString(sink);
		}
	}
	}
}

ulong[2] solve(const Pair[] pairs)
{
	import std.algorithm;
	import std.array;

	ulong[2] result;

	const sum = accumulate(pairs.map!(p => new Pair(p)).array);
	result[0] = magnitude(sum);

	void evaluate(scope const Pair a, scope const Pair b)
	{
		scope Pair ac = new Pair(a);
		scope Pair bc = new Pair(b);
		scope Pair acc = accumulate([ac, bc]);
		const mag = magnitude(acc);

		if (result[1] < mag)
			result[1] = mag;
	}

	foreach (const idx, const a; pairs)
	{
		foreach (const b; pairs[idx + 1 .. $])
		{
			evaluate(a, b);
			evaluate(b, a);
		}
	}
	return result;
}

unittest
{
	static immutable string[] input = [
		"[[[0,[5,8]],[[1,7],[9,6]]],[[4,[1,2]],[[1,4],2]]]",
		"[[[5,[2,8]],4],[5,[[9,9],0]]]",
		"[6,[[[6,2],[5,6]],[[7,6],[4,7]]]]",
		"[[[6,[0,7]],[0,9]],[4,[9,[9,0]]]]",
		"[[[7,[6,4]],[3,[1,3]]],[[[5,5],1],9]]",
		"[[6,[[7,3],[3,2]]],[[[3,8],[5,7]],4]]",
		"[[[[5,4],[7,7]],8],[[8,3],8]]",
		"[[9,3],[[9,9],[6,[4,9]]]]",
		"[[2,[[7,7],7]],[[5,8],[[9,3],[0,2]]]]",
		"[[[[5,2],5],[8,[3,7]]],[[5,[7,5]],[4,4]]]",
	];

	static int test()
	{
		auto pairs = parse(input);
		auto res = solve(pairs);

		assert(res[0] == 4140);
		assert(res[1] == 3993);
		return 0;
	}

	test();
	// enum force = test();
}

///
Pair add(Pair left, Pair right)
{
	Pair result = new Pair(left, right);
	left.parent = right.parent = result;
	return result;
}

unittest
{
	void test(const(char)[] leftStr, const(char)[] rightStr, string expected)
	{
		Pair left = new Pair(leftStr);
		Pair right = new Pair(rightStr);
		Pair added = add(left, right);
		assert(added.toString() == expected);
	}
	test("[1,2]", "[[3,4],5]", "[[1,2],[[3,4],5]]");
}

///
bool tryExplode(ref Entry entry, uint depth = 0)
{
	Pair pair = entry.isPair();
	if (!pair)
		return false;

	if (depth < 4)
	{
		if (tryExplode(pair.left, depth + 1))
			return true;

		return tryExplode(pair.right, depth + 1);
	}

	assert(depth == 4);
	assert(pair.left.isValue);

	if (Entry* left = leftNeighbour(pair))
	{
		left.value = left.value + pair.left.value;
	}
	if (Entry* right = rightNeighbour(pair))
	{
		right.value = right.value + pair.right.value;
	}

	entry.value = 0;
	return true;
}

unittest
{
	void test(const(char)[] input, string expected)
	{
		scope pair = new Pair(input);
		scope entry = Entry(pair);
		const done = entry.tryExplode();
		assert(done);
		assert(pair.toString() == expected);
	}
	test("[[[[[9,8],1],2],3],4]", "[[[[0,9],2],3],4]");
	test("[7,[6,[5,[4,[3,2]]]]]", "[7,[6,[5,[7,0]]]]");
	test("[[6,[5,[4,[3,2]]]],1]", "[[6,[5,[7,0]]],3]");
	test("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]");
	test("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]", "[[3,[2,[8,0]]],[9,[5,[7,0]]]]");
}

void split(ref Entry entry, Pair parent = null)
{
	const val = entry.value;
	const leftVal = val / 2;
	const rightVal = leftVal + (val % 2);

	entry.pair =  new Pair(leftVal, rightVal, parent);
}

unittest
{
	void test(int value, int[2] expected)
	{
		Entry entry = Entry(value);
		split(entry);

		Pair p = entry.isPair();
		assert(p);
		assert(p.left.value == expected[0]);
		assert(p.right.value == expected[1]);
	}

	test(10, [5, 5]);
	test(11, [5, 6]);
	test(12, [6, 6]);
}

///
bool trySplit(ref Entry entry, Pair parent = null)
{
	Pair pair = entry.isPair();
	if (!pair)
	{
		const doSplit = entry.value >= 10;
		if (doSplit)
			split(entry, parent);
		return doSplit;
	}

	if (trySplit(pair.left, pair))
		return true;

	return trySplit(pair.right, pair);
}

void reduce(Pair pair)
{
	Entry entry = Entry(pair);

	while (tryExplode(entry) || trySplit(entry))
	{
		// Another attempt
	}
}

unittest
{
	void test(const(char)[] input, string expected)
	{
		scope Pair pair = new Pair(input);
		reduce(pair);
		assert(pair.toString() == expected);
	}
	test("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]", "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]");
}

Pair accumulate(scope Pair[] pairs)
{
	Pair current = pairs[0];

	foreach (ref pair; pairs[1..$])
	{
		current = add(current, pair);
		reduce(current);
	}

	return current;
}

unittest
{
	static void test(const char[][] input, const string expected)
	{
		auto pairs = parse(input);
		auto res = accumulate(pairs);
		assert(res.toString() == expected);
	}

	test([
			"[1,1]",
			"[2,2]",
			"[3,3]",
			"[4,4]",
		],
		"[[[[1,1],[2,2]],[3,3]],[4,4]]"
	);

	test([
			"[1,1]",
			"[2,2]",
			"[3,3]",
			"[4,4]",
			"[5,5]",
		],
		"[[[[3,0],[5,3]],[4,4]],[5,5]]"
	);

	test([
			"[1,1]",
			"[2,2]",
			"[3,3]",
			"[4,4]",
			"[5,5]",
			"[6,6]",
		],
		"[[[[5,0],[7,4]],[5,5]],[6,6]]"
	);

	test([
			"[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
			"[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
			"[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
			"[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
			"[7,[5,[[3,8],[1,4]]]]",
			"[[2,[2,2]],[8,[8,1]]]",
			"[2,9]",
			"[1,[[[9,3],9],[[9,0],[0,7]]]]",
			"[[[5,[7,4]],7],1]",
			"[[[[4,2],2],6],[8,7]]",
		],
		"[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"
	);
}

uint magnitude(scope const Pair pair)
{
	const Entry entry = const Entry(pair);

	static typeof(return) magnitude(scope ref const Entry entry)
	{
		if (auto pair = entry.isPair())
		{
			const left = magnitude(pair.left);
			const right = magnitude(pair.right);
			return 3 * left + 2 * right;
		}
		else
			return entry.value;
	}

	return magnitude(entry);
}

unittest
{
	void test(const(char)[] input, const uint expected)
	{
		scope pair = new Pair(input);
		assert(pair.magnitude() == expected);
	}
	test("[9,1]", 29);
	test("[[1,2],[[3,4],5]]", 143);
	test("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]", 1384);
	test("[[[[1,1],[2,2]],[3,3]],[4,4]]", 445);
	test("[[[[3,0],[5,3]],[4,4]],[5,5]]", 791);
	test("[[[[5,0],[7,4]],[5,5]],[6,6]]", 1137);
	test("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]", 3488);
}

Entry* findNeighbour(string from, string to)(Pair current)
in(current)
{
	while (true)
	{
		if (!current || !current.parent)
			return null;

		auto toPtr = &__traits(getMember, current.parent, to);
		auto toPair = toPtr.isPair();
		if (toPair && toPair !is current)
		{
			current = toPair;
			break;
		}
		else if (toPtr.isValue)
			return toPtr;

		current = current.parent;
	}

	while (true)
	{
		auto ptr = &__traits(getMember, current, from);
		auto right  = ptr.isPair();
		if (!right)
			return ptr;
		current = right;
	}
}

alias rightNeighbour = findNeighbour!("left", "right");
alias leftNeighbour = findNeighbour!("right", "left");
