module day09;

void main() {
	import std.file : readText;
	import std.stdio : writefln;

	const text = readText("day09.in");

	writefln!"A: %s"(decompress!false(text));
	writefln!"B: %s"(decompress!true(text));
}

ulong decompress(bool recursive)(scope const(char)[] line) {
	import std.range.primitives;

	typeof(return) count = 0;

	while(!line.empty) {

		if(line.front == '(') {
			import std.conv : parse;

			line.popFront(); // (
			const len = line.parse!uint();

			line.popFront(); // x
			const reps = line.parse!uint();

			line.popFront(); // )

			static if(!recursive) {
				// --- Part One ---
				count += reps * len;
			}
			else {
				// --- Part Two ---
				count += reps * decompress!true(line[0 .. len]);
			}

			line = line[len .. $];
		}
		else {
			line.popFront();
			count++;
		}
	}

	return count;
}

unittest {
	static void check(bool recursive)(immutable uint[string] examples) {
		foreach(const example, const length; examples) {
			assert(decompress!recursive(example) == length);
		}
	}

	check!false([
		"ADVENT": 6,
		"A(1x5)BC": 7,
		"(3x3)XYZ": 9,
		"A(2x2)BCD(2x2)EFG": 11,
		"(6x1)(1x3)A": 6,
		"X(8x2)(3x3)ABCY": 18
	]);

	check!true([
		"ADVENT": 6,
		"A(1x5)BC": 7,
		"(3x3)XYZ": 9,
		"X(8x2)(3x3)ABCY": 20,
		"(27x12)(20x12)(13x14)(7x10)(1x12)A": 241920,
		"(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN": 445
	]);
}