import std.algorithm;

void main() {
	import std.stdio;
	
	auto input = File("day2.in", "r");
	
	writefln("1: %d", task1(input.byLine));
	input.rewind();
	writefln("2: %s", task2(input.byLineCopy));
}
	
	
uint task1(Range)(Range input) {
	uint count2 = 0, count3 = 0;

	foreach(line; input) {
		uint[char] found;
		
		foreach(c; line) found[c]++;
		
		if(found.values.canFind(2)) count2++;
		if(found.values.canFind(3)) count3++;
	}
	
	return count2 * count3;
}

string task2(Range)(Range input) {	
	string[] old;
	
	foreach(left; input) {
		foreach(right; old) {
		
			const diff = levenshteinDistance(left, right);
			
			if(diff == 1) {
				const start = commonPrefix(left, right);
				
				return start ~ left[start.length + 1 .. $];
			}
		}
		
		old ~= left;
	}
	
	return "No such element!";
}

unittest {
	import std.conv : to;
	
	const res1 = task1([
		"abcdef",
		"bababc",
		"abbcde",
		"abcccd",
		"aabcdd",
		"abcdee",
		"ababab"		
	]);

	assert(res1 == 12, "Failed example 1: " ~ res1.to!string);

	const res2 = task2([
		"abcde",
		"fghij",
		"klmno",
		"pqrst",
		"fguij",
		"axcye",
		"wvxyz"		
	]);

	assert(res2 == "fgij", "Failed example 2: " ~ res2);
}