import std.algorithm;

void main() {
	version(unittest){} else {
		import std.stdio : writefln;
		import std.file : readText;

		const result = readText("day8.in").parse().solve();

		writefln("1: %s\n2: %s", result.a, result.b);
	}
}

int[] parse(const(char)[] input) {
	import std.array : array;
	import std.conv : to;

	return input.splitter.map!(to!int).array;
}

auto solve(const(int)[] numbers) {
	import std.typecons : Tuple;
	debug import std.stdio;

	alias Result = Tuple!(
		int, "a", 
		int, "b"
	);

	alias Node = Tuple!(
		int, "childNodes", 
		int, "metadataEntrys",
		int[], "childWeights"
	);

	Result result;
	Node[] stack = new Node[](numbers.length / 2 + 1)[1 .. $];	// Hidden first node contains result
	Node* cur = stack.ptr - 1;

	void pushNode() {
		cur++;

		cur.childNodes = numbers.next;
		cur.metadataEntrys = numbers.next;
		cur.childWeights.length = 0;
	}

	void popNode() {
		const metadata = numbers[0 .. cur.metadataEntrys];
		const metadataSum = metadata.sum;

		result.a += metadataSum;

		const weight = cur.childNodes == 0
			? metadataSum										// No childs => Same as a)
			: metadata											// Sum the weight of all childs found in metadata
				.filter!(m => 1 <= m && m <= cur.childNodes)	// - Ingore all invalid indices
				.map!(m => cur.childWeights[m - 1])				// - Fetch the childs weight
				.sum;											// - Sum all values

		(cur - 1).childWeights ~= weight;						// Parent node is the predecessor on the stack

		numbers = numbers[metadata.length .. $];
		cur--;
	}
	
	debug void print() {
		writef!"Numbers: %(%d, %)\nResult(%d, %d)\n"(numbers, result.expand);

		for(Node* n = cur; stack.ptr <= n; n--) {
			writef!"  %3d, %3d, %s\n"(n.expand);
		}

		writeln("\n");
	}

	pushNode();	// Fake first node
	debug print();

	while(numbers.length) {

		// Push an existing child node
		if(cur.childWeights.length < cur.childNodes) {
			pushNode();
		}

		// No child nodes, process metadata
		else {
			popNode();
		}

		debug print();
	}

	result.b = (stack.ptr - 1).childWeights[0];		// Fetch the result from hidden first node

	return result;
}

auto next(ref const(int)[] range) {
	import std.range.primitives;

	scope(exit) range.popFront();
	return range.front;
}

unittest {
	import std.array : array;

	static immutable exampleStr = "2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2";

	const result = exampleStr.parse().solve();

	assertEquals(result.a, 138);
	assertEquals(result.b, 66);
}

version(unittest)
void assertEquals(A, B)(const A got, const B expected) {
	import std.format : format;

	assert(got == expected, format!"Expected %s but got %s!"(expected, got));
}